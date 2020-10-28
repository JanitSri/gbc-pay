package com.example.formvalidiation.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;


@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Password and Confirm Password does not match";
    Class<?>[] groups() default {};
    public abstract Class<? extends Payload>[] payload() default {};

    String first();
    String second();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        PasswordMatches[] value();
    }
}
