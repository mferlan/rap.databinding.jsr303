package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.databinding.validation.jsr303.samples.HibernateValidatorDelegate;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.rap.rwt.RWT;
import org.slf4j.LoggerFactory;

public class ResourceBundleMessageApplier {

    private static final String s_sLABEL_SUFFIX = "Label";

    private static void applyText(ResourceBundle rb, String messageKey, Object control) {
        try {
            String message = rb.getString( messageKey );
            WidgetProperties.text().setValue( control, message );
        }
        catch ( MissingResourceException e ) {
            // ignore
            WidgetProperties.text().setValue( control, messageKey );
        }
    }

    public static void applyTextToLabels(ClassLoader classLoader, UIControlContainer container,
                                         String resourceBundleName) {
        ResourceBundle rb = ResourceBundleMessageApplier.loadBundle( classLoader,
                        resourceBundleName, RWT.getLocale() );
        if ( rb != null ) {
            for ( String bindingProperty : container.keySet() ) {
                int nLength = ResourceBundleMessageApplier.s_sLABEL_SUFFIX.length();
                Object control = container.getControl( bindingProperty );
                if ( bindingProperty.endsWith( ResourceBundleMessageApplier.s_sLABEL_SUFFIX ) ) {
                    String bindingPropertyCore = bindingProperty.substring( 0,
                                    bindingProperty.length() - nLength );
                    ResourceBundleMessageApplier.applyText( rb, bindingPropertyCore + ".label",
                                    control );
                }
                else {
                    ResourceBundleMessageApplier.applyTooltip( rb, bindingProperty + ".tooltip",
                                    control );
                }
            }
        }
    }

    private static void applyTooltip(ResourceBundle rb, String messageKey, Object control) {
        try {
            String message = rb.getString( messageKey );
            WidgetProperties.tooltipText().setValue( control, message );
        }
        catch ( MissingResourceException e ) {
            // ignore
        }
    }

    private static ResourceBundle loadBundle(ClassLoader classLoader, String bundleName,
                                             Locale locale) {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle( bundleName, locale, classLoader );
        }
        catch ( MissingResourceException e ) {
            LoggerFactory.getLogger( HibernateValidatorDelegate.class )
                            .trace( "Unable to find resource bundle " + bundleName );
        }
        return rb;
    }

}
