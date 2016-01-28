package org.eclipse.core.databinding.validation.jsr303;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 * An {@link IValidator} which validates a single property of an entity using JSR 303 bean
 * validation.
 *
 * <p>
 * This class acts as an adapter/bridge between JSR 303 Bean Validation and <code>IValidator</code>
 * as used by Eclipse.
 *
 * <p>
 * This class is entirely decoupled from the UI. To show a <code>ControlDecoration</code> next to a
 * bound UI element which depends on the returned {@link IStatus}, you can use this validator in the
 * <code>UpdateValueStrategy</code> of the binding, and use for example
 *
 * <pre>
 * ControlDecorationSupport.create( binding, SWT.TOP | SWT.LEFT );
 * </pre>
 *
 * where <code>binding</code> is the <code>Binding</code> returned by
 * <code>DataBindingContext.bindValue(..)</code>.
 *
 *
 */
public class Jsr303PropertyValidator implements IValidator {

    private static final String VALIDATION_ERRORS = "Validation errors";
    private static final String PLUGIN_ID = "org.eclipse.rap.databinding.validation.jsr303";
    private boolean multiStatus;
    private Class beanType;
    private String propertyName;

    public Jsr303PropertyValidator(Class< ? > beanType, String sPropertyName,
                                   boolean isMultiStatus) {
        super();
        // validateConstructorArgument( beanType, sPropertyName );
        this.beanType = beanType;
        this.propertyName = sPropertyName;
        this.multiStatus = isMultiStatus;
    }

    public ValidatorFactory getValidatorFactory() {
        return Jsr303PropertyValidationSupport.getValidatorFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang .Object)
     */
    public IStatus validate(Object value) {
        // Validate value for the propertyName of the beanType.
        Set< ConstraintViolation > violations = validateConstraints( value );
        if ( violations.size() > 0 ) {
            if ( this.multiStatus ) {
                // Create a multi status with list of errors
                List< IStatus > statusList = new ArrayList< IStatus >();
                for ( ConstraintViolation< Object > cv : violations ) {
                    statusList.add( ValidationStatus.error( cv.getMessage() ) );

                }
                return new MultiStatus( Jsr303PropertyValidator.PLUGIN_ID, IStatus.ERROR,
                                statusList.toArray( new IStatus[ statusList.size() ] ),
                                Jsr303PropertyValidator.VALIDATION_ERRORS, null );

            }
            else {
                // Create a simple status with the first error.
                for ( ConstraintViolation< Object > cv : violations ) {
                    return ValidationStatus.error( cv.getMessage() );
                }

            }
        }
        return Status.OK_STATUS;
    }

    /**
     * Validate the value for the propertyName of the beanType.
     *
     * @param value
     * @return
     */
    protected Set< ConstraintViolation > validateConstraints(Object value) {
        Set validatedValue = getValidatorFactory().getValidator().validateValue( this.beanType,
                        this.propertyName, value );
        return validatedValue;
    }

    /**
     *
     * @param beanClass
     * @param propertyName
     */
    private void validateConstructorArgument(Class< ? > beanClass, String propertyName) {
        try {
            beanClass.getDeclaredField( propertyName );
        }
        catch ( NoSuchFieldException e ) {
            throw new IllegalArgumentException(
                            "Field " + propertyName + " not found on class " + beanClass );
        }
    }

}
