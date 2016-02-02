package org.eclipse.core.databinding.validation.jsr303.samples.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

import org.eclipse.core.databinding.validation.jsr303.samples.util.PasswordComplexity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Account {

    @NotNull
    @NotBlank
    @Email
    private String accountName;

    @NotNull
    @NotBlank
    @Pattern.List({ //
                    @Pattern(regexp = PasswordComplexity.APLHABET_NUMBER, groups = {
                                    PasswordComplexity.AlphabetNumber.class,
                                    Default.class }, message = "{de.atron.constraints.password.alphabetnumber}"), //
                    @Pattern(regexp = PasswordComplexity.ALFABET_NUMBER_SPECIALCHAR, groups = PasswordComplexity.AlphabetNumberSpecialChar.class, message = "{de.atron.constraints.password.alphabetnumberspecial}"), //
                    @Pattern(regexp = PasswordComplexity.UPPERCASE_LOWERCASE_NUMBER, groups = PasswordComplexity.UppercaseLowercaseNumber.class, message = "{de.atron.constraints.password.upperlowercasenumber}"), //
                    @Pattern(regexp = PasswordComplexity.UPPERCASE_LOWERCASE_NUMBER_SPECIALCHAR, groups = PasswordComplexity.UppercaseLowercaseNumberSpecial.class, message = "{de.atron.constraints.password.upperlowercasenumberspecial}")//
    })
    private String password;

    @NotNull
    private String confirmPassword;

    public String getAccountName() {
        return this.accountName;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAccountName(String oAccountName) {
        this.accountName = oAccountName;
    }

    public void setConfirmPassword(String oConfirmPassword) {
        this.confirmPassword = oConfirmPassword;
    }

    public void setPassword(String oPassword) {
        this.password = oPassword;
    }

    @Override
    public String toString() {
        return "Registration [accountName=" + this.accountName + ", password=" + this.password
                        + ", confirmPassword=" + this.confirmPassword + "]";
    }

}
