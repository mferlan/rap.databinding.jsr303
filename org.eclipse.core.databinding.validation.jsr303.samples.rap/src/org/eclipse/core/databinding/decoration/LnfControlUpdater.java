package org.eclipse.core.databinding.decoration;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Control;

public class LnfControlUpdater implements ControlUpdater {

    private static final String ORIGINAL_STYLE = "original.lnf.variant";
    private static final String NO_STYLE = "**no-style**";

    private Object getLnfStyle(Control control) {
        return control.getData( RWT.CUSTOM_VARIANT );
    }

    /**
     *
     * @param control
     */
    private void preserveOriginalStyle(Control control) {
        // first time updating
        Object style = getLnfStyle( control );
        if ( style != null ) {
            // backup the original style
            control.setData( LnfControlUpdater.ORIGINAL_STYLE, style );
        }
        else {
            control.setData( LnfControlUpdater.ORIGINAL_STYLE, LnfControlUpdater.NO_STYLE );
        }
    }

    /**
     *
     * @param control
     * @param controlsVariant
     */
    private void restoreOriginalStyle(Control control, Object controlsVariant) {
        if ( controlsVariant.equals( LnfControlUpdater.NO_STYLE ) ) {
            setLnfStyle( control, null );
        }
        else {
            setLnfStyle( control, controlsVariant );
        }
    }

    private void setLnfStyle(Control control, Object style) {
        control.setData( RWT.CUSTOM_VARIANT, style );
    }

    /*
     * (non-Javadoc)
     *
     * @see de.atron.ui.rap.validation.ControlLnfUpdater#update(org.eclipse.swt.widgets.Control,
     * org.eclipse.core.runtime.IStatus)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.databinding.validation.jsr303.samples.util.ControlUpdater#update(org.eclipse
     * .swt.widgets.Control, org.eclipse.core.runtime.IStatus)
     */
    public void update(Control control, IStatus status) {
        Object controlsVariant = control.getData( LnfControlUpdater.ORIGINAL_STYLE );
        if ( controlsVariant == null ) {
            preserveOriginalStyle( control );
            controlsVariant = control.getData( LnfControlUpdater.ORIGINAL_STYLE );
        }

        if ( ( ( status == null ) || status.isOK() ) ) {
            // restore original lnf style
            restoreOriginalStyle( control, controlsVariant );
        }
        else {
            switch ( status.getSeverity() ) {
                case IStatus.WARNING :
                    setLnfStyle( control, "WarningStatus" );
                    break;
                case IStatus.INFO :
                    setLnfStyle( control, "InfoStatus" );
                    break;
                case IStatus.ERROR :
                case IStatus.CANCEL :
                default :
                    setLnfStyle( control, "ErrorStatus" );
                    break;
            }
        }
    }

}
