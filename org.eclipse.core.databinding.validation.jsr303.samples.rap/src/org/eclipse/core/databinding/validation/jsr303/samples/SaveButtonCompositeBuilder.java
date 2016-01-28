package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.validation.jsr303.samples.util.UIControlContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SaveButtonCompositeBuilder extends CompositeBuilder {

    public SaveButtonCompositeBuilder() {
        super();
    }

    public SaveButtonCompositeBuilder(UIControlContainer oUiControlContainer) {
        super( oUiControlContainer );
    }

    @Override
    public Composite createContent(Composite parent) {
        Composite buttonComposite = new Composite( parent, SWT.NONE );
        buttonComposite.setLayout( new GridLayout( 2, false ) );
        Label label = new Label( buttonComposite, SWT.NONE );
        label.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        Button saveButton = new Button( parent, SWT.NONE );
        saveButton.setText( "save" );
        addUIControl( saveButton, "save" );
        return buttonComposite;
    }

}
