package org.eclipse.core.databinding.validation.jsr303.samples;

import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.decoration.UpdateControlSupport;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Account;
import org.eclipse.core.databinding.validation.jsr303.samples.model.AccountFieldDescriptor;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Address;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.model.PersonFieldDescriptor;
import org.eclipse.core.databinding.validation.jsr303.samples.util.AggregateValidationStatusProvider;
import org.eclipse.core.databinding.validation.jsr303.samples.util.DatabindingConfiguration;
import org.eclipse.core.databinding.validation.jsr303.samples.util.FieldMatchesDatabindingValidator;
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

public class Jsr303RegistrationEntryPoint1 extends AbstractEntryPoint {

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
        // MODEL
        final Person person = new Person();
        person.setAddress( new Address() );

        final Account account = new Account();

        Realm realm = SWTObservables.getRealm( getShell().getDisplay() );
        DataBindingContext dataBindingContext = new DataBindingContext( realm );
        // mark labels of controls as required
        Jsr303RequiredControlDecoratorSupport.create( uiControlContainer, Person.class, true,
                        SWT.TOP | SWT.RIGHT );
        Jsr303RequiredControlDecoratorSupport.create( uiControlContainer, Account.class, true,
                        SWT.TOP | SWT.RIGHT );

        /*
         * generating and configuring information used for databinding
         */
        Jsr303DatabindingConfigurator personDatabindingBuilder = new Jsr303DatabindingConfigurator(
                        realm, uiControlContainer, Person.class );
        personDatabindingBuilder.setValidationGroup( PersonFieldDescriptor.FIELD_ADDRESS,
                        Address.MainAddress.class, Address.FiveDigitPostalCode.class );
        personDatabindingBuilder.setValidationGroup( PersonFieldDescriptor.FIELD_SHIPPINGADDRESS,
                        Address.FiveDigitPostalCode.class );
        personDatabindingBuilder.initialize();

        Jsr303DatabindingConfigurator accountDatabindingBuilder = new Jsr303DatabindingConfigurator(
                        realm, uiControlContainer, Account.class );
        accountDatabindingBuilder.initialize();

        String passwordFieldPath = AccountFieldDescriptor.FIELD_PASSWORD;
        String confirmPasswordFieldPath = AccountFieldDescriptor.FIELD_CONFIRMPASS;
        // cross constraint validator - binding to a target because model is a pojo and there is no
        // property change notifications
        DatabindingConfiguration passwordMetadata = accountDatabindingBuilder
                        .getConfiguration( AccountFieldDescriptor.FIELD_PASSWORD );
        IObservableValue passwordTarget = passwordMetadata.getTargetObservable();
        DatabindingConfiguration confirmPasswordMetadata = accountDatabindingBuilder
                        .getConfiguration( AccountFieldDescriptor.FIELD_CONFIRMPASS );
        IObservableValue confirmPasswordTarget = confirmPasswordMetadata.getTargetObservable();

        FieldMatchesDatabindingValidator validatorStatusProvider = new FieldMatchesDatabindingValidator(
                        passwordTarget, confirmPasswordTarget );

        // binding
        personDatabindingBuilder.bind( dataBindingContext, person );
        // do not set control updater for account bindings
        Map< String, Binding > accountBindings = accountDatabindingBuilder.bind( dataBindingContext,
                        account );

        AggregateValidationStatusProvider passwordAVSP = new AggregateValidationStatusProvider(
                        accountBindings.get( passwordFieldPath ), validatorStatusProvider );
        UpdateControlSupport.create( passwordAVSP, SWT.TOP | SWT.RIGHT );
        AggregateValidationStatusProvider confirmPsswordAVSP = new AggregateValidationStatusProvider(
                        accountBindings.get( confirmPasswordFieldPath ), validatorStatusProvider );
        UpdateControlSupport.create( confirmPsswordAVSP, SWT.TOP | SWT.RIGHT );
        UpdateControlSupport.create(
                        accountBindings.get( AccountFieldDescriptor.FIELD_ACCOUNTNAME ),
                        SWT.TOP | SWT.RIGHT );

        // apply localized messages
        ResourceBundleMessageApplier.applyTextToLabels( getClass().getClassLoader(),
                        uiControlContainer, "registration" );

        // add items to combo salution
        Combo combo = uiControlContainer.getControl( PersonFieldDescriptor.FIELD_SALUTATION,
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
        NewAccountCompositeBuilder newAccountBuilder = new NewAccountCompositeBuilder(
                        uiControlContainer );
        newAccountBuilder.createContent( parent );

        PersonCompositeBuilder builder = new PersonCompositeBuilder( uiControlContainer );
        builder.createContent( parent );

        SaveButtonCompositeBuilder buttonBuilder = new SaveButtonCompositeBuilder(
                        uiControlContainer );
        buttonBuilder.createContent( parent )
                        .setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        return uiControlContainer;
    }

}
