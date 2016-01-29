package org.eclipse.core.databinding.validation.jsr303;

import javax.validation.ValidatorFactory;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.service.ApplicationContext;
import org.eclipse.rap.validation.jsr303.ValidatorFactoryBuilder;

/**
 *
 * Helper class which retrieves the instance of {@link ValidatorFactory} to use to validate and
 * gives state of the JSR-303 Bean Validator implementation used.
 */
public class Jsr303PropertyValidationSupport {

    public static final ValidatorFactory getValidatorFactory() {
        ValidatorFactory validationFactory = (ValidatorFactory) RWT.getApplicationContext()
                        .getAttribute( ValidatorFactory.class.getName() );
        if ( validationFactory == null ) {
            ApplicationContext applicationContext = RWT.getApplicationContext();
            synchronized ( applicationContext ) {
                validationFactory = (ValidatorFactory) RWT.getApplicationContext()
                                .getAttribute( ValidatorFactory.class.getName() );
                if ( validationFactory == null ) {
                    validationFactory = new ValidatorFactoryBuilder().build();
                    applicationContext.setAttribute( ValidatorFactory.class.getName(),
                                    validationFactory );
                }
            }

        }
        return validationFactory;
    }
}
