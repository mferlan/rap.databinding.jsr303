package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Address;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.model.PersonFieldDescriptor;
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

public class Jsr303PersonEntryPoint extends AbstractEntryPoint {

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
        final Person model = new Person();
        model.setAddress( new Address() );

        Realm realm = SWTObservables.getRealm( getShell().getDisplay() );
        DataBindingContext dataBindingContext = new DataBindingContext( realm );
        // mark labels of controls as required
        Jsr303RequiredControlDecoratorSupport.create( uiControlContainer, model.getClass(), true,
                        SWT.TOP | SWT.RIGHT );

        /*
         * generating and configuring information used for databinding
         */
        Jsr303DatabindingConfigurator databindingBuilder = new Jsr303DatabindingConfigurator(
                        realm, uiControlContainer, Person.class );
        databindingBuilder.setValidationGroup( PersonFieldDescriptor.FIELD_ADDRESS,
                        Address.MainAddress.class, Address.FiveDigitPostalCode.class );
        databindingBuilder.setValidationGroup( PersonFieldDescriptor.FIELD_SHIPPINGADDRESS,
                        Address.FiveDigitPostalCode.class );
        // binding done
        databindingBuilder.bind( dataBindingContext, model );

        // apply localized messages
        ResourceBundleMessageApplier.applyTextToLabels( getClass().getClassLoader(),
                        uiControlContainer, "person" );

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
                System.out.println( model );
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
        parent.setLayout( new GridLayout( 2, false ) );
        parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        PersonCompositeBuilder builder = new PersonCompositeBuilder();
        builder.createContent( parent );
        UIControlContainer uiControlContainer = builder.getUiControlContainer();

        SaveButtonCompositeBuilder buttonBuilder = new SaveButtonCompositeBuilder(
                        uiControlContainer );
        Composite buttonComposite = buttonBuilder.createContent( parent );
        buttonComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        return uiControlContainer;
    }

}
