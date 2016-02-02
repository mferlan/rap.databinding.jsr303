package org.eclipse.core.databinding.validation.jsr303.samples.model;

public class RegistrationFieldDescriptor {

    public static final String FIELD_USER = "userData";
    public static final String FIELD_ACCOUNT = "account";

    public static final String FIELD_ACCOUNTNAME = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_ACCOUNT,
                    AccountFieldDescriptor.FIELD_ACCOUNTNAME );
    public static final String FIELD_PASSWORD = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_ACCOUNT,
                    AccountFieldDescriptor.FIELD_PASSWORD );
    public static final String FIELD_CONFIRMPASS = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_ACCOUNT,
                    AccountFieldDescriptor.FIELD_CONFIRMPASS );

    public static final String FIELD_ADDRESS = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_USER, PersonFieldDescriptor.FIELD_ADDRESS );
    public static final String FIELD_SHIPPINGADDRESS = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_USER,
                    PersonFieldDescriptor.FIELD_SHIPPINGADDRESS );
    public static final String FIELD_SALUTATION = RegistrationFieldDescriptor.constructPath(
                    RegistrationFieldDescriptor.FIELD_USER,
                    PersonFieldDescriptor.FIELD_SALUTATION );

    public static String constructPath(String... fields) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < fields.length; i++ ) {
            if ( i != 0 ) {
                sb.append( "." );
            }
            sb.append( fields[ i ] );
        }
        return sb.toString();
    }
}
