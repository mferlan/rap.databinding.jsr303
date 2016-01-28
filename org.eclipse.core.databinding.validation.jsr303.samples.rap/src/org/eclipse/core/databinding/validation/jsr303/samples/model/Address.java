package org.eclipse.core.databinding.validation.jsr303.samples.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.eclipse.core.databinding.validation.jsr303.samples.util.PostalCode;
import org.hibernate.validator.constraints.NotBlank;

public class Address {

    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-z-A-Z]*")
    private String street;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\d+[a-zA-Z]*$")
    private String number;
    @NotNull
    @PostalCode
    private String postalCode;
    @NotNull
    @NotBlank
    @Pattern(regexp = "[a-z-A-Z]*")
    private String city;
    @Pattern(regexp = "[a-z-A-Z]*")
    private String country;

    /**
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * @param oCity
     *            the city to set
     */
    public void setCity(String oCity) {
        this.city = oCity;
    }

    /**
     * @param oCountry
     *            the country to set
     */
    public void setCountry(String oCountry) {
        this.country = oCountry;
    }

    /**
     * @param oNumber
     *            the number to set
     */
    public void setNumber(String oNumber) {
        this.number = oNumber;
    }

    /**
     * @param oPostalCode
     *            the postalCode to set
     */
    public void setPostalCode(String oPostalCode) {

        this.postalCode = oPostalCode;
    }

    /**
     * @param oStreet
     *            the street to set
     */
    public void setStreet(String oStreet) {
        this.street = oStreet;
    }

    @Override
    public String toString() {
        return "Address [street=" + this.street + ", number=" + this.number + ", postalCode="
                        + this.postalCode + ", city=" + this.city + ", country=" + this.country
                        + "]";
    }

}
