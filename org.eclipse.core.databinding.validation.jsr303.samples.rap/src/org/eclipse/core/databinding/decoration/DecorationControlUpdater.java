package org.eclipse.core.databinding.decoration;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

public class DecorationControlUpdater extends ControlDecorationUpdater implements ControlUpdater {

    private int position = SWT.TOP | SWT.LEFT;

    public DecorationControlUpdater() {
    }

    public DecorationControlUpdater(int oPosition) {
        super();
        this.position = oPosition;
    }

    /**
     *
     * @param control
     * @return
     */
    protected ControlDecoration getControlDecoration(Control control) {
        ControlDecoration controlDecoration = (ControlDecoration) control
                        .getData( ControlDecoration.class.getName() );
        if ( controlDecoration == null ) {
            synchronized ( control ) {
                controlDecoration = (ControlDecoration) control
                                .getData( ControlDecoration.class.getName() );
                if ( controlDecoration == null ) {
                    controlDecoration = new ControlDecoration( control, this.position );
                    control.setData( ControlDecoration.class.getName(), controlDecoration );
                }
            }
        }
        return controlDecoration;
    }

    public void update(Control control, IStatus status) {
        ControlDecoration controlDecoration = getControlDecoration( control );
        super.update( controlDecoration, status );
    }

}
