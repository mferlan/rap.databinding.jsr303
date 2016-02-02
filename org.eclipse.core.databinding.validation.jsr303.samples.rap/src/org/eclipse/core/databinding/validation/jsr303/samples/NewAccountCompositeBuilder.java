package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.validation.jsr303.samples.model.AccountFieldDescriptor;
import org.eclipse.core.databinding.validation.jsr303.samples.util.UIControlContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import net.miginfocom.swt.MigLayout;

public class NewAccountCompositeBuilder extends CompositeBuilder {

    public NewAccountCompositeBuilder() {
        super();
    }

    public NewAccountCompositeBuilder(UIControlContainer oUiControlContainer) {
        super( oUiControlContainer );
    }

    @Override
    public Composite createContent(Composite parent) {
        Composite compositeForm = createFormComposite( parent );
        compositeForm.setLayout(
                        new MigLayout( "ins 10, gapx 20, gapy 10, wrap 2", "[][fill, grow]", "" ) );
        createLabelWithText( compositeForm, AccountFieldDescriptor.FIELD_ACCOUNTNAME );
        createLabelWithText( compositeForm, AccountFieldDescriptor.FIELD_PASSWORD,
                        SWT.BORDER | SWT.PASSWORD );
        createLabelWithText( compositeForm, AccountFieldDescriptor.FIELD_CONFIRMPASS,
                        SWT.BORDER | SWT.PASSWORD );

        return compositeForm;
    }

}
