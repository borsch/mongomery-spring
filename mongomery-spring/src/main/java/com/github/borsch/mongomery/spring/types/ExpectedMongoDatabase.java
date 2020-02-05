package com.github.borsch.mongomery.spring.types;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExpectedMongoDatabase {

    /**
     *
     * @return locations of file(s) which will be used to assert database state
     */
    String[] value();

    /**
     *
     * @return array of fields to be excluded from matching process
     */
    String[] excludeFields() default { };

    /**
     *
     * @return array of fields that should almost every time be excluded
     */
    String[] defaultExcludeFields() default { "_id", "_class" };

}
