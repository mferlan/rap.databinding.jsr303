package org.eclipse.core.databinding.validation.jsr303.samples.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RegistrationData {
    @NotNull
    @Valid
    private Account account;
    @NotNull
    @Valid
    private Person userData;

    public Account getAccount() {
        return this.account;
    }

    public Person getUserData() {
        return this.userData;
    }

    public void setAccount(Account oAccount) {
        this.account = oAccount;
    }

    public void setUserData(Person oUserData) {
        this.userData = oUserData;
    }

}
