package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class FieldValidationStatus extends Status {
    public static final int INCOMPLETE = 16;

    public static final int INVALID = 32;

    public static final int REQUIRED = 64;

    public static FieldValidationStatus incomplete(String sMessage) {
        return new FieldValidationStatus( IStatus.ERROR, FieldValidationStatus.INCOMPLETE,
                        sMessage );
    }

    public static FieldValidationStatus invalid(String sMessage) {
        return new FieldValidationStatus( IStatus.ERROR, FieldValidationStatus.INVALID, sMessage );
    }

    public static IStatus ok() {
        return Status.OK_STATUS;
    }

    public static FieldValidationStatus required(String sMessage) {
        return new FieldValidationStatus( IStatus.ERROR, FieldValidationStatus.REQUIRED, sMessage );
    }

    private FieldValidationStatus(int severity, int code, String sMessage) {
        super( severity, "de.atron.ui.rap", code, sMessage, null );
    }
}
