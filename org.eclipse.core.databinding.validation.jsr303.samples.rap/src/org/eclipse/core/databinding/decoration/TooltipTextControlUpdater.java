package org.eclipse.core.databinding.decoration;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Control;

public class TooltipTextControlUpdater implements ControlUpdater {

    /**
     * Holds two tooltip values (Strings): (a) the original tooltip of the ridget and (b) the
     * compute message tooltip
     */
    private static final class Tooltip {
        /**
         * Tooltip value to use when the 'message' tooltip is removed. May be null.
         */
        private String originalTooltip;
        /** Tooltip value for the 'message' tooltip. */
        private String messageTooltip;

        Tooltip(final String originalTooltip, final String messageTooltip) {
            this.originalTooltip = originalTooltip;
            this.messageTooltip = messageTooltip;
        }

        @Override
        public String toString() {
            return String.format( "[%s,%s]", this.originalTooltip, this.messageTooltip ); //$NON-NLS-1$
        }
    }

    /**
     * Creates a string with all messages of the given status.
     *
     * @param status
     *            a status; can be multistatus
     * @param separator
     *            a String for separating the status messages; never null
     * @return string with all messages
     */
    public static String constructMessage(final IStatus status, final String separator) {
        Assert.isNotNull( separator );
        final StringWriter sw = new StringWriter();
        return TooltipTextControlUpdater.constructMessage( sw, status, separator );
    }

    /**
     *
     * @param sw
     * @param status
     * @param separator
     * @return
     */
    private static String constructMessage(StringWriter sw, final IStatus status,
                                           final String separator) {
        if ( status != null ) {
            if ( !status.isMultiStatus() ) {
                return status.getMessage();
            }
            for ( final IStatus nextMarker : status.getChildren() ) {
                if ( nextMarker.isMultiStatus() ) {
                    TooltipTextControlUpdater.constructMessage( sw, nextMarker, separator );
                }
                else {
                    if ( sw.toString().trim().length() > 0 ) {
                        sw.write( separator );
                    }
                    if ( nextMarker.getMessage() != null ) {
                        sw.write( nextMarker.getMessage() );
                    }
                }
            }
        }

        return sw.toString().trim();

    }

    private final HashMap< Control, Tooltip > tooltips = new LinkedHashMap< Control, Tooltip >();

    protected String getMessageSeparator() {
        return "\n"; //$NON-NLS-1$
    }

    protected void hideStatusMessage(final Control control) {
        if ( this.tooltips.containsKey( control ) ) {
            final String tooltip = this.tooltips.get( control ).originalTooltip;
            control.setToolTipText( tooltip );
            this.tooltips.remove( control );
        }
    }

    /**
     *
     * @param control
     * @param status
     */
    protected void showStatusMessage(Control control, final IStatus status) {
        String message = TooltipTextControlUpdater.constructMessage( status,
                        getMessageSeparator() );
        // show the message only if there is something to show
        final String current = control.getToolTipText();
        if ( message.length() > 0 ) {
            if ( !message.equals( current ) ) {
                Tooltip tooltip = this.tooltips.get( control );
                if ( tooltip == null ) {
                    tooltip = new Tooltip( current, message );
                    this.tooltips.put( control, tooltip );
                }
                else {
                    tooltip.messageTooltip = message;
                }
                control.setToolTipText( message );
            }
        }
        else {
            hideStatusMessage( control );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.databinding.decoration.ControlUpdater#update(org.eclipse.swt.widgets.
     * Control, org.eclipse.core.runtime.IStatus)
     */
    public void update(Control control, IStatus status) {
        // 1. check is status ok
        if ( ( ( status == null ) || status.isOK() ) ) {
            hideStatusMessage( control );
        }
        else {
            // construct the message
            showStatusMessage( control, status );
        }
    }

}
