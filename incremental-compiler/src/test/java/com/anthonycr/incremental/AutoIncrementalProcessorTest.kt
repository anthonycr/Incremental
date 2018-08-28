package com.anthonycr.incremental

import com.google.testing.compile.Compiler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import test_case.AggregatingExample
import test_case.DynamicExample
import test_case.IsolatingExample
import java.io.BufferedReader
import java.io.File
import javax.tools.DiagnosticCollector
import javax.tools.JavaFileObject
import javax.tools.StandardJavaFileManager
import javax.tools.ToolProvider
import kotlin.reflect.KClass

/**
 * Unit tests for [AutoIncrementalProcessor].
 */
class AutoIncrementalProcessorTest {

    private val compiler = Compiler.javac().withProcessors(AutoIncrementalProcessor())
    private val fileManager = ToolProvider.getSystemJavaCompiler().getStandardFileManager(DiagnosticCollector(), null, null)
    private val classpathRoot = File(Thread.currentThread().contextClassLoader.getResource("").path)
    private val projectRoot = classpathRoot.parentFile.parentFile.parentFile.parentFile
    private val javaRoot = File(File(File(projectRoot, "src"), "test"), "java")

    @Test
    fun validateAutoAggregating() {
        compiler
                .compile(fileManager.createJavaFileObjects(AggregatingExample::class))
                .generatedFiles()
                .findIncrementalResourcesFiles()
                .asSingleElement()
                .assertContents("test_case.AggregatingExample,aggregating")
    }

    @Test
    fun validateAutoIsolating() {
        compiler
                .compile(fileManager.createJavaFileObjects(IsolatingExample::class))
                .generatedFiles()
                .findIncrementalResourcesFiles()
                .asSingleElement()
                .assertContents("test_case.IsolatingExample,isolating")
    }

    @Test
    fun validateAutoDynamic() {
        compiler
                .compile(fileManager.createJavaFileObjects(DynamicExample::class))
                .generatedFiles()
                .findIncrementalResourcesFiles()
                .asSingleElement()
                .assertContents("test_case.DynamicExample,dynamic")
    }

    @Test
    fun validateMultipleAutoAnnotations() {
        compiler
                .compile(fileManager.createJavaFileObjects(DynamicExample::class, AggregatingExample::class, IsolatingExample::class))
                .generatedFiles()
                .findIncrementalResourcesFiles()
                .asSingleElement()
                .assertContents("""
                    test_case.AggregatingExample,aggregating
                    test_case.IsolatingExample,isolating
                    test_case.DynamicExample,dynamic
                """.trimIndent())
    }

    /**
     * Creates a list of [JavaFileObject] from the provided [KClass] list. Returns the objects as an
     * [Iterable] for convenience.
     */
    private fun StandardJavaFileManager.createJavaFileObjects(vararg kClass: KClass<*>): Iterable<JavaFileObject> {
        return getJavaFileObjects(*kClass.map { it.asResourcePath().asClassPathFile(javaRoot) }.toTypedArray())
    }
}

/**
 * Asserts that the trimmed contents of a [JavaFileObject] are the parameter provided by
 * [content].
 */
private fun JavaFileObject.assertContents(content: String) {
    assertThat(openInputStream().bufferedReader().use(BufferedReader::readText).trim()).isEqualTo(content)
}

/**
 * Asserts that the list contains a single element
 */
private fun <T> List<T>.asSingleElement(): T {
    if (size != 1) {
        throw IllegalStateException("List must be of size 1")
    }
    return last()
}

/**
 * Returns all [JavaFileObject] that are incremental.annotation.processors files.
 */
private fun List<JavaFileObject>.findIncrementalResourcesFiles(): List<JavaFileObject> {
    return filter {
        it.kind == JavaFileObject.Kind.OTHER
                && it.name == "/CLASS_OUTPUT/META-INF/gradle/incremental.annotation.processors"
    }
}

/**
 * Converts the [KClass] to a java resource path.
 */
private fun KClass<*>.asResourcePath() = "${this.java.name.replace('.', '/')}.java"

/**
 * Converts a [String] representing a resource path to a [File].
 */
private fun String.asClassPathFile(root: File): File {
    val ind = this.indexOf('$')

    return if (ind < 0) {
        File(root, this)
    } else {
        File(root, "${this.substring(0, ind)}.java")
    }
}
