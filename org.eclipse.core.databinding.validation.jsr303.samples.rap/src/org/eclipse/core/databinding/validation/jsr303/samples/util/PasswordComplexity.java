package org.eclipse.core.databinding.validation.jsr303.samples.util;

public enum PasswordComplexity {
    // At least 1 Alphabet and 1 Number:
    APLHABET_NUMBER,
    // At least 1 Alphabet and 1 Number and 1 Special Character:
    ALFABET_NUMBER_SPECIALCHAR,
    // 1 Uppercase Alphabet, 1 Lowercase Alphabet and 1 Number:
    UPPERCASE_LOWERCASE_NUMBER,
    // At least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character:
    UPPERCASE_LOWERCASE_NUMBER_SPECIALCHAR;
}