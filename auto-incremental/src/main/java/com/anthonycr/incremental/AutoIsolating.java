package com.anthonycr.incremental;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an isolating processor.
 * <p>
 * Created by anthonycr on 4/26/18.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface AutoIsolating {}
