package org.eclipse.core.databinding.validation.jsr303.samples.util;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PostalCodeValidator.class)
@Documented
public @interface PostalCode {

    Class< ? >[]groups() default {};

    String message() default "{de.atron.constraints.postalcode}";

    Class< ? extends Payload >[]payload() default {};

}