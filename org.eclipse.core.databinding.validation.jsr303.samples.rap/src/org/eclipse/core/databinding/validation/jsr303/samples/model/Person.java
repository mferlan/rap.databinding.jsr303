package org.eclipse.core.databinding.validation.jsr303.samples.model;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Person {
    private String salutation;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-z-A-Z]*")
    private String firstName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-z-A-Z]*")
    private String lastName;
    @NotNull
    @Past
    private Date birthDate;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Valid
    private Address address;

    @NotNull
    @Valid
    private Address shippingAddress;

    @Pattern(regexp = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$", message = "{person.validation.invalidphone}")
    private String phone;

    @Pattern(regexp = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$", message = "{person.validation.invalidphone}")
    private String mobile;
    @Pattern(regexp = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$", message = "{person.validation.invalidphone}")
    private String faxNr;

    /**
     * @return the address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return this.birthDate;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @return the faxNr
     */
    public String getFaxNr() {
        return this.faxNr;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return this.mobile;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return this.phone;
    }

    public String getSalutation() {
        return this.salutation;
    }

    public Address getShippingAddress() {
        return this.shippingAddress;
    }

    /**
     * @param oAddress
     *            the address to set
     */
    public void setAddress(Address oAddress) {
        this.address = oAddress;
    }

    /**
     * @param oBirthDate
     *            the birthDate to set
     */
    public void setBirthDate(Date oBirthDate) {
        this.birthDate = oBirthDate;
    }

    /**
     * @param oEmail
     *            the email to set
     */
    public void setEmail(String oEmail) {
        this.email = oEmail;
    }

    /**
     * @param oFaxNr
     *            the faxNr to set
     */
    public void setFaxNr(String oFaxNr) {
        this.faxNr = oFaxNr;
    }

    /**
     * @param oFirstName
     *            the firstName to set
     */
    public void setFirstName(String oFirstName) {
        this.firstName = oFirstName;
    }

    /**
     * @param oLastName
     *            the lastName to set
     */
    public void setLastName(String oLastName) {
        this.lastName = oLastName;
    }

    /**
     * @param oMobile
     *            the mobile to set
     */
    public void setMobile(String oMobile) {
        this.mobile = oMobile;
    }

    /**
     * @param oPhone
     *            the phone to set
     */
    public void setPhone(String oPhone) {
        this.phone = oPhone;
    }

    public void setSalutation(String oSalutation) {
        this.salutation = oSalutation;
    }

    public void setShippingAddress(Address oShippingAddress) {
        this.shippingAddress = oShippingAddress;
    }

    @Override
    public String toString() {
        return "Person [salutation=" + this.salutation + ", firstName=" + this.firstName
                        + ", lastName=" + this.lastName + ", birthDate=" + this.birthDate
                        + ", email=" + this.email + ", address=" + this.address
                        + ", shippingAddress=" + this.shippingAddress + ", phone=" + this.phone
                        + ", mobile=" + this.mobile + ", faxNr=" + this.faxNr + "]";
    }

}
