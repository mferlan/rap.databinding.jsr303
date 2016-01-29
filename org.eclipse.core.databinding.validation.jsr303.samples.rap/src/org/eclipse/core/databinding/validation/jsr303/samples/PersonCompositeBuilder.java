package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.validation.jsr303.samples.model.PersonFieldDescriptor;
import org.eclipse.swt.widgets.Composite;

import net.miginfocom.swt.MigLayout;

/**
 * @author Korisnik
 * @author Abu Zoha
 *
 */
public class PersonCompositeBuilder extends CompositeBuilder {
    @Override
    public Composite createContent(Composite parent) {
        Composite compositeForm = createFormComposite( parent );
        compositeForm.setLayout(
                        new MigLayout( "ins 10, gapx 20, gapy 10, wrap 2", "[][fill, grow]", "" ) );
        // compositeForm.setEnabled( false );
        addUIControl( compositeForm, PersonFieldDescriptor.class.getName() );
        createLabelWithCombo( compositeForm, PersonFieldDescriptor.FIELD_SALUTATION );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_FIRSTNAME );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_LASTNAME );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_ADDRESS + "."
                        + PersonFieldDescriptor.FIELD_STREET );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_ADDRESS + "."
                        + PersonFieldDescriptor.FIELD_NUMBER );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_ADDRESS + "."
                        + PersonFieldDescriptor.FIELD_POSTALCODE );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_ADDRESS + "."
                        + PersonFieldDescriptor.FIELD_CITY );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_PHONE );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_MOBILE );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_FAXNR );
        createLabelWithDate( compositeForm, PersonFieldDescriptor.FIELD_BIRTHDATE );

        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_SHIPPINGADDRESS + "."
                        + PersonFieldDescriptor.FIELD_STREET );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_SHIPPINGADDRESS + "."
                        + PersonFieldDescriptor.FIELD_NUMBER );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_SHIPPINGADDRESS + "."
                        + PersonFieldDescriptor.FIELD_POSTALCODE );
        createLabelWithText( compositeForm, PersonFieldDescriptor.FIELD_SHIPPINGADDRESS + "."
                        + PersonFieldDescriptor.FIELD_CITY );

        return compositeForm;
    }

}
