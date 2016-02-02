package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * http://eclipsesource.com/blogs/2009/02/27/databinding-crossvalidation-with-a-multivalidator/
 *
 * @author mferlan
 *
 */
public class FieldMatchesDatabindingValidator extends MultiValidator {

    private final IObservableValue start;
    private final IObservableValue end;

    public FieldMatchesDatabindingValidator(final IObservableValue start,
                                            final IObservableValue end) {
        super( start.getRealm() );
        this.start = start;
        this.end = end;
    }

    @Override
    protected IStatus validate() {
        Object first = this.start.getValue();
        Object second = this.end.getValue();
        IStatus status = ValidationStatus.ok();
        if ( ( first != null ) && ( second != null ) ) {
            if ( !first.equals( second ) ) {
                status = ValidationStatus.error( "Fields must match" );
            }
        }

        return status;
    }

}
