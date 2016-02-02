package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class CompoundValidator implements IValidator {
    private final IValidator[] validators;

    public CompoundValidator(final IValidator... validators) {
        this.validators = validators;
    }

    public IStatus validate(final Object value) {
        IStatus result = ValidationStatus.ok();
        for ( IValidator validator : this.validators ) {
            IStatus status = validator.validate( value );

            if ( status.getSeverity() > result.getSeverity() ) {
                result = status;
            }
        }
        return result;
    }
}