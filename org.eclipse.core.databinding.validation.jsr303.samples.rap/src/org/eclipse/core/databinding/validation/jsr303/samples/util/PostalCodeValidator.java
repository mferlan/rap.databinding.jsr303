package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PostalCodeValidator implements ConstraintValidator< PostalCode, String > {

    /**
     * Pattern for Sweden and German postal codes.
     */
    protected final static String DIGIT_PATTERN = "\\d{5}";
    /**
     * Pattern for other countries.
     */
    protected final static String PATTERN = "[a-zA-Z0-9\\s]{2,5}";

    public void initialize(PostalCode postalCode) {
    }

    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }

        boolean zipCodeFiveDigits = SystemPreferences.getInstance().isCheckZipFiveDigits();

        if ( object.length() < 5 ) {
            return false;
        }
        if ( zipCodeFiveDigits ) {
            return validatePostalCode( object, PostalCodeValidator.DIGIT_PATTERN );
        }
        else {
            return validatePostalCode( object, PostalCodeValidator.DIGIT_PATTERN );
        }
    }

    /**
     * Validates a given string against a regex expression.
     *
     * @param toValidate
     *            the string to validate
     * @param pattern
     *            the regex pattern
     * @return true if string matches
     */
    protected boolean validatePostalCode(String toValidate, String pattern) {
        Pattern mask = Pattern.compile( pattern );
        Matcher matcher = mask.matcher( toValidate.trim() );
        if ( matcher.matches() ) {
            return true;
        }
        return false;
    }
}