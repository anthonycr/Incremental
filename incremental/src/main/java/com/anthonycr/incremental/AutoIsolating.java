package com.anthonycr.incremental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an isolating incremental processor.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface AutoIsolating {}
