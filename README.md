# Incremental
A resource generator for incremental annotation processors

## Abstract

Incremental annotation processing is supported by the Gradle build system after [version 4.7](https://docs.gradle.org/4.7/userguide/java_plugin.html#sec:incremental_annotation_processing). In order for annotation processors to participate in incremental compilation, they must register with the compiler through resources. An entry for each incremental processor, specifying its qualified name and behavior (isolating or aggregating), must be made in the `META-INF/gradle/incremental.annotation.processors` file. The Incremental library makes this easier by automatically managing these entries for you.

## Usage

Annotation processors should include both the annotations and the compiler

```groovy
implementation project(':incremental')
annotationProcessor project(':incremental-compiler')
```

Isolating processors should use the `@AutoIsolating` annotation.

```kotlin
package com.example

@AutoIsolating
class IsolatingProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        ...
    }

}
```

Incremental will then generate the following resource entry.

```
com.example.IsolatingProcessor,isolating
```

Aggregating processors should use the `@AutoAggregating` annotation.

```kotlin
package com.example

@AutoAggregating
class AggregatingProcessor : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        ...
    }

}
```

Incremental will then generate the following resource entry.

```
com.example.AggregatingProcessor,aggregating
```

Processors that determine if they are isolating or aggregating at runtime should use the `@AutoDynamic` annotation.

```kotlin
@AutoDynamic
class DynamicProcessor : AbstractProcessor() {

    override fun getSupportedOptions(): Set<String> = setOf(
        "org.gradle.annotation.processing.aggregating" // "org.gradle.annotation.processing.isolating"
    )

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        ...
    }

}
```

Incremental will then generate the following resource entry.

```
com.example.DynamicProcessor,dynamic
```

## Debugging

Turn on debug logging by enabling a compiler option in the gradle build file.

```gradle
kapt {
    arguments {
        arg("incremental.debug", true)
    }
}
```

## License
```
Copyright 2018 Anthony Restaino

Licensed under the Apache License, Version 2.0 (the "License"); you may 
not use this file except in compliance with the License. You may obtain 
a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
License for the specific language governing permissions and limitations 
under the License.
```
