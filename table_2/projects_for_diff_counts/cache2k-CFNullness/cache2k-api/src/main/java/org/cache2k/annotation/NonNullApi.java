package org.cache2k.annotation;
@Target(value = {ElementType.PACKAGE, ElementType.TYPE})
@Retention(value = RetentionPolicy.CLASS)
@Documented
@TypeQualifierDefault(value = {ElementType.METHOD, ElementType.PARAMETER})
@UnderMigration(status = MigrationStatus.STRICT)
public @interface NonNullApi { }
