package org.eclipse.core.databinding.validation.jsr303.samples;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.validation.MessageInterpolator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.ResourceBundleLocator;
import org.slf4j.LoggerFactory;

/**
 * Inner class to avoid a hard-coded Hibernate Validator 4.1 dependency.
 */
public class HibernateValidatorDelegate {

    public static MessageInterpolator buildMessageInterpolator(final String bundleName,
                                                               final Class< ? > clazz) {
        return new ResourceBundleMessageInterpolator( new ResourceBundleLocator() {

            public ResourceBundle getResourceBundle(Locale locale) {
                ClassLoader classLoader = clazz.getClassLoader();
                return loadBundle( classLoader, locale, bundleName + " cannot be found" );
            }

            private ResourceBundle loadBundle(ClassLoader classLoader, Locale locale,
                                              String message) {
                ResourceBundle rb = null;
                try {
                    rb = ResourceBundle.getBundle( bundleName, locale, classLoader );
                }
                catch ( MissingResourceException e ) {
                    LoggerFactory.getLogger( HibernateValidatorDelegate.class ).trace( message );
                }
                return rb;
            }
        } );
    }
}