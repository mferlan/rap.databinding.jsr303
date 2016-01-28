package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Address;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.model.PersonFieldDescriptor;
import org.eclipse.core.databinding.validation.jsr303.samples.util.Jsr303BindSupport;
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
import org.eclipse.swt.widgets.Display;

public class Jsr303SampleEntryPoint extends AbstractEntryPoint {

    /**
     *
     */
    private static final long serialVersionUID = -618048721577106082L;

    @Override
    protected void createContents(Composite shell) {
        Display display = getShell().getDisplay();

        Realm realm = SWTObservables.getRealm( display );
        DataBindingContext dataBindingContext = new DataBindingContext( realm );

        // UI
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

        // MODEL
        WritableValue value = new org.eclipse.core.databinding.observable.value.WritableValue(
                        realm, null, Person.class );
        final Person model = new Person();
        model.setAddress( new Address() );

        value.setValue( model );
        // CONTROLLER

        // mark labels of controls as required
        Jsr303RequiredControlDecoratorSupport.create( uiControlContainer, Person.class, true,
                        SWT.TOP | SWT.RIGHT );

        // bind ui to pojo
        // Jsr303BindSupport.bindPojo( dataBindingContext, uiControlContainer, model,
        // SWT.TOP | SWT.RIGHT );
        // bind ui to writeable value
        Jsr303BindSupport.bindWriteableValue( dataBindingContext, uiControlContainer, value,
                        SWT.TOP | SWT.RIGHT );

        // apply localized messages
        ResourceBundleMessageApplier.applyTextToLabels( getClass().getClassLoader(),
                        uiControlContainer, "person" );
        // add items to combo salution
        Combo combo = uiControlContainer.getControl( PersonFieldDescriptor.FIELD_SALUTATION,
                        Combo.class );
        combo.add( "Mrs." );
        combo.add( "Ms." );

        // bind enable save button to aggregated status in databinding
        Button saveButton = uiControlContainer.getControl( "save" );
        Jsr303BindSupport.bindEnabledToAggregateStatus( dataBindingContext, saveButton );

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

}
