package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.validation.jsr303.samples.util.UIControlContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class CompositeBuilder {

    private UIControlContainer uiControlContainer;

    public CompositeBuilder() {
        this( new UIControlContainer() );
    }

    public CompositeBuilder(UIControlContainer oUiControlContainer) {
        super();
        this.uiControlContainer = oUiControlContainer;
    }

    protected void addUIControl(Control control, String sBindingId) {
        getUiControlContainer().put( sBindingId, control );
    }

    protected void addUILabel(Label lbl, String sBindingId) {
        getUiControlContainer().put( sBindingId + "Label", lbl );
    }

    abstract public Composite createContent(Composite parent);

    protected Composite createFormComposite(Composite parent) {
        return createFormComposite( parent, SWT.DOUBLE_BUFFERED );
    }

    protected Composite createFormComposite(Composite parent, int style) {
        Composite composite = new Composite( parent, style );
        composite.setBackgroundMode( SWT.INHERIT_DEFAULT );
        return composite;
    }

    protected void createLabelWithCombo(Composite composite, String bindingId) {
        Label lbl = new Label( composite, SWT.NONE );
        lbl.setText( bindingId );
        Combo combo = new Combo( composite, SWT.NONE );
        addUIControl( combo, bindingId );
        addUILabel( lbl, bindingId );
    }

    protected void createLabelWithDate(Composite composite, String bindingId) {
        Label lbl = new Label( composite, SWT.NONE );
        lbl.setText( bindingId );
        Control dateTime = new DateTime( composite,
                        SWT.DROP_DOWN | SWT.DATE | SWT.MEDIUM | SWT.BORDER );
        addUIControl( dateTime, bindingId );
        addUILabel( lbl, bindingId );
    }

    protected void createLabelWithText(Composite composite, String bindingId) {
        Label lbl = new Label( composite, SWT.NONE );
        lbl.setText( bindingId );
        Text txt = new Text( composite, SWT.BORDER );
        addUIControl( txt, bindingId );
        addUILabel( lbl, bindingId );
    }

    public UIControlContainer getUiControlContainer() {
        return this.uiControlContainer;
    }

}