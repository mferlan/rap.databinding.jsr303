/*******************************************************************************
 * Copyright (c) 2011 Angelo Zerr and Pascal Leclercq.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     Pascal Leclercq <pascal.leclercq@gmail.com> - Initial API and implementation
 *******************************************************************************/
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
