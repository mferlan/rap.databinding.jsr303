package org.eclipse.rap.validation.jsr303;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * This is the central class for <code>javax.validation</code> (JSR-303) setup as a local bean. It
 * bootstraps a <code>javax.validation.ValidationFactory</code> and exposes it through the
 * {@link ValidatorFactory} interface
 *
 * @see javax.validation.ValidatorFactory
 * @see javax.validation.Validator
 * @see javax.validation.Validation#buildDefaultValidatorFactory()
 * @see javax.validation.ValidatorFactory#getValidator()
 */
public class ValidatorFactoryBuilder {

    /**
     * Merge the given Properties instance into the given Map, copying all properties (key-value
     * pairs) over.
     * <p>
     * Uses <code>Properties.propertyNames()</code> to even catch default properties linked into the
     * original Properties instance.
     *
     * @param props
     *            the Properties instance to merge (may be <code>null</code>)
     * @param map
     *            the target Map to merge the properties into
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void mergePropertiesIntoMap(Properties props, Map map) {
        if ( map == null ) {
            throw new IllegalArgumentException( "Map must not be null" );
        }
        if ( props != null ) {
            for ( Enumeration en = props.propertyNames(); en.hasMoreElements(); ) {
                String key = (String) en.nextElement();
                Object value = props.getProperty( key );
                if ( value == null ) {
                    // Potentially a non-String value...
                    value = props.get( key );
                }
                map.put( key, value );
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private Class providerClass;

    private MessageInterpolator messageInterpolator;

    private TraversableResolver traversableResolver;

    private ConstraintValidatorFactory constraintValidatorFactory;

    private final Map< String, String > validationPropertyMap = new HashMap< String, String >();

    public ValidatorFactory build() {
        @SuppressWarnings("rawtypes")
        Configuration configuration = ( this.providerClass != null
                        ? Validation.byProvider( this.providerClass ).configure()
                        : Validation.byDefaultProvider().configure() );

        MessageInterpolator targetInterpolator = this.messageInterpolator;
        if ( targetInterpolator == null ) {
            targetInterpolator = configuration.getDefaultMessageInterpolator();
        }
        configuration.messageInterpolator(
                        new UISessionLocaleMessageInterpolator( targetInterpolator ) );

        if ( this.traversableResolver != null ) {
            configuration.traversableResolver( this.traversableResolver );
        }

        ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
        if ( targetConstraintValidatorFactory != null ) {
            configuration.constraintValidatorFactory( targetConstraintValidatorFactory );
        }

        for ( Map.Entry< String, String > entry : this.validationPropertyMap.entrySet() ) {
            configuration.addProperty( entry.getKey(), entry.getValue() );
        }

        return configuration.buildValidatorFactory();
    }

    /**
     * Allow Map access to the bean validation properties to be passed to the validation provider,
     * with the option to add or override specific entries.
     * <p>
     * Useful for specifying entries directly, for example via "validationPropertyMap[myKey]".
     */
    public Map< String, String > getValidationPropertyMap() {
        return this.validationPropertyMap;
    }

    /**
     * Specify a custom ConstraintValidatorFactory to use for this ValidatorFactory.
     */
    public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
        this.constraintValidatorFactory = constraintValidatorFactory;
    }

    /**
     * Specify a custom MessageInterpolator to use for this ValidatorFactory and its exposed default
     * Validator.
     */
    public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
    }

    /**
     * Specify the desired provider class, if any.
     * <p>
     * If not specified, JSR-303's default search mechanism will be used.
     *
     * @see javax.validation.Validation#byProvider(Class)
     * @see javax.validation.Validation#byDefaultProvider()
     */
    @SuppressWarnings("rawtypes")
    public void setProviderClass(Class providerClass) {
        this.providerClass = providerClass;
    }

    /**
     * Specify a custom TraversableResolver to use for this ValidatorFactory and its exposed default
     * Validator.
     */
    public void setTraversableResolver(TraversableResolver traversableResolver) {
        this.traversableResolver = traversableResolver;
    }

    /**
     * Specify bean validation properties to be passed to the validation provider.
     * <p>
     * Can be populated with a String "value" (parsed via PropertiesEditor) or a "props" element in
     * XML bean definitions.
     *
     * @see javax.validation.Configuration#addProperty(String, String)
     */
    public void setValidationProperties(Properties jpaProperties) {
        ValidatorFactoryBuilder.mergePropertiesIntoMap( jpaProperties, this.validationPropertyMap );
    }

    /**
     * Specify bean validation properties to be passed to the validation provider as a Map.
     * <p>
     * Can be populated with a "map" or "props" element in XML bean definitions.
     *
     * @see javax.validation.Configuration#addProperty(String, String)
     */
    public void setValidationPropertyMap(Map< String, String > validationProperties) {
        if ( validationProperties != null ) {
            this.validationPropertyMap.putAll( validationProperties );
        }
    }

}
