package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Account;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Address;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.model.RegistrationData;
import org.eclipse.core.databinding.validation.jsr303.samples.model.RegistrationFieldDescriptor;
import org.eclipse.core.databinding.validation.jsr303.samples.util.Jsr303DatabindingConfigurator;
import org.eclipse.core.databinding.validation.jsr303.samples.util.Jsr303RequiredControlDecoratorSupport;
import org.eclipse.core.databinding.validation.jsr303.samples.util.ResourceBundleMessageApplier;
import org.eclipse.core.databinding.validation.jsr303.samples.util.UIControlContainer;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class Jsr303RegistrationEntryPoint2 extends AbstractEntryPoint {

    /**
     *
     */
    private static final long serialVersionUID = -618048721577106082L;

    @Override
    protected void createContents(Composite shell) {

        // UI
        UIControlContainer uiControlContainer = createView( shell );

        // CONTROLLER

        createController( uiControlContainer );

    }

    /**
     *
     * @param uiControlContainer
     * @param model
     */
    private void createController(UIControlContainer uiControlContainer) {
        // create MODEL - make sure to initialize all objects
        RegistrationData registration = new RegistrationData();
        final Person person = new Person();
        person.setAddress( new Address() );
        person.setShippingAddress( new Address() );
        final Account account = new Account();
        registration.setAccount( account );
        registration.setUserData( person );

        // create realm and databinding context
        Realm realm = SWTObservables.getRealm( getShell().getDisplay() );
        DataBindingContext dataBindingContext = new DataBindingContext( realm );

        // mark labels of controls as required
        Jsr303RequiredControlDecoratorSupport.create( uiControlContainer, RegistrationData.class,
                        true, SWT.TOP | SWT.RIGHT );
        /*
         * generating and configuring information used for databinding
         */
        Jsr303DatabindingConfigurator databindingBuilder = new Jsr303DatabindingConfigurator( realm,
                        uiControlContainer, RegistrationData.class );
        databindingBuilder.setValidationGroup( RegistrationFieldDescriptor.FIELD_ADDRESS,
                        Address.MainAddress.class, Address.FiveDigitPostalCode.class );
        databindingBuilder.setValidationGroup( RegistrationFieldDescriptor.FIELD_SHIPPINGADDRESS,
                        Address.FiveDigitPostalCode.class );

        // cross constraint validator - binding to a target because model is a pojo and there is no
        // property change notifications
        databindingBuilder.addFieldMatchValidation( RegistrationFieldDescriptor.FIELD_PASSWORD,
                        RegistrationFieldDescriptor.FIELD_CONFIRMPASS );

        // binding done
        databindingBuilder.bind( dataBindingContext, registration );

        // apply localized messages
        ResourceBundleMessageApplier.applyTextToLabels( getClass().getClassLoader(),
                        uiControlContainer, "registration2" );

        // add items to combo salution
        Combo combo = uiControlContainer.getControl( RegistrationFieldDescriptor.FIELD_SALUTATION,
                        Combo.class );
        combo.add( "Mrs." );
        combo.add( "Ms." );

        Button saveButton = uiControlContainer.getControl( "save" );
        // bind enable save button to aggregated status in databinding
        Jsr303DatabindingConfigurator.bindEnabledToAggregateStatus( dataBindingContext,
                        saveButton );

        saveButton.addSelectionListener( new SelectionAdapter() {

            /**
             *
             */
            private static final long serialVersionUID = 9063114721996211315L;

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                System.out.println( account );
                System.out.println( person );
            };
        } );
    }

    /**
     *
     * @param shell
     * @return
     */
    private UIControlContainer createView(Composite shell) {
        Composite parent = new Composite( shell, SWT.NONE );
        parent.setLayout( new GridLayout( 1, false ) );
        parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        UIControlContainer uiControlContainer = new UIControlContainer();
        NewAccountCompositeBuilder newAccountBuilder = new NewAccountCompositeBuilder();
        newAccountBuilder.createContent( parent );
        uiControlContainer.merge( RegistrationFieldDescriptor.FIELD_ACCOUNT,
                        newAccountBuilder.getUiControlContainer() );

        PersonCompositeBuilder builder = new PersonCompositeBuilder();
        builder.createContent( parent );
        uiControlContainer.merge( RegistrationFieldDescriptor.FIELD_USER,
                        builder.getUiControlContainer() );

        SaveButtonCompositeBuilder buttonBuilder = new SaveButtonCompositeBuilder(
                        uiControlContainer );
        buttonBuilder.createContent( parent )
                        .setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        return uiControlContainer;
    }

}
