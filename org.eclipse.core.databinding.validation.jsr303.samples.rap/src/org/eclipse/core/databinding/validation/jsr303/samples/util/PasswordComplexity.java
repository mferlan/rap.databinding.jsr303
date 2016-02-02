package org.eclipse.core.databinding.validation.jsr303.samples.util;

import javax.validation.groups.Default;

public class PasswordComplexity {
    public interface AlphabetNumber extends Default {
    }

    public interface AlphabetNumberSpecialChar extends Default {
    }

    public interface UppercaseLowercaseNumber extends Default {
    }

    public interface UppercaseLowercaseNumberSpecial extends Default {
    }

    // At least 1 Alphabet and 1 Number:
    public static final String APLHABET_NUMBER = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";

    // At least 1 Alphabet and 1 Number and 1 Special Character:
    public static final String ALFABET_NUMBER_SPECIALCHAR = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$";

    // 1 Uppercase Alphabet, 1 Lowercase Alphabet and 1 Number:
    public static final String UPPERCASE_LOWERCASE_NUMBER = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$";

    // At least 1 Uppercase Alphabet, 1 Lowercase Alphabet, 1 Number and 1 Special Character:
    public static final String UPPERCASE_LOWERCASE_NUMBER_SPECIALCHAR = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,}$";
}