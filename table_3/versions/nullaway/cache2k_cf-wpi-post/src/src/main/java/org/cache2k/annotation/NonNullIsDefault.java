package org.cache2k.annotation;

import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Documented
@TypeQualifierNickname
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, PARAMETER, FIELD, TYPE_USE, ElementType.PACKAGE})

public @interface NonNullIsDefault {}
