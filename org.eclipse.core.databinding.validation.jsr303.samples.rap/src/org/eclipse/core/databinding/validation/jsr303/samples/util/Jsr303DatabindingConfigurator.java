package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.decoration.ControlUpdater;
import org.eclipse.core.databinding.decoration.DecorationControlUpdater;
import org.eclipse.core.databinding.decoration.UpdateControlSupport;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.jsr303.Jsr303PropertyValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303PropertyValidator;
import org.eclipse.core.databinding.validation.jsr303.Jsr303UpdateValueStrategyFactory;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.LoggerFactory;

public class Jsr303DatabindingConfigurator {

    /**
     *
     * @param dataBindingContext
     * @param control
     */
    public static void bindEnabledToAggregateStatus(DataBindingContext dataBindingContext,
                                                    Object control) {
        Realm validationRealm = dataBindingContext.getValidationRealm();
        final IObservableValue buttonEnabledObservable = WidgetProperties.enabled()
                        .observe( validationRealm, control );
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setConverter( new StatusToBooleanConverter() );
        // This one listenes to all changes
        IObservableList validationStatusProviders = dataBindingContext.getBindings();
        AggregateValidationStatus aggregateValidationStatus = new AggregateValidationStatus(
                        validationRealm, validationStatusProviders,
                        AggregateValidationStatus.MAX_SEVERITY );
        Binding bindValue = dataBindingContext.bindValue( aggregateValidationStatus,
                        buttonEnabledObservable, strategy, null );
        bindValue.updateTargetToModel();
    }

    private Map< String, DatabindingConfiguration > m_mapConfiguration;
    private UIControlContainer m_oUiContainer;
    private Class< ? > m_classPojo;
    private Realm m_oRealm;

    private WritableValue m_oWritableValue;
    private Class< ? >[] validationGroups;
    private Map< String, Class< ? >[] > mapValidationGroups;
    private Map< String, ValidationStatusProvider > mapExtraValidationStatusProvier;
    private boolean initialized = false;

    public Jsr303DatabindingConfigurator(Realm realm, UIControlContainer oUiContainer,
                                         Class< ? > oPojoClass) {
        super();
        this.m_oRealm = realm;
        this.m_oUiContainer = oUiContainer;
        this.m_classPojo = oPojoClass;
        Set< String > setPojoProperties = filter( oUiContainer.keySet(), oPojoClass );
        this.m_mapConfiguration = new HashMap< String, DatabindingConfiguration >(
                        setPojoProperties.size() );
        for ( String property : setPojoProperties ) {
            this.m_mapConfiguration.put( property, new DatabindingConfiguration( realm ) );
        }
        this.m_oWritableValue = new WritableValue( this.m_oRealm, null, this.m_classPojo );
        this.mapValidationGroups = new HashMap< String, Class< ? >[] >();
        this.mapExtraValidationStatusProvier = new HashMap< String, ValidationStatusProvider >();
    }

    /**
     * This method invocation will invoke {@link #initialize()} method.
     * FieldMatchesDatabindingValidator requires target observable for properties passed as
     * arguments
     *
     * @param firstProperty
     * @param secondProperty
     */
    public void addFieldMatchValidation(String firstProperty, String secondProperty) {
        if ( !this.initialized ) {
            initialize();
        }
        ValidationStatusProvider provider = new FieldMatchesDatabindingValidator(
                        getTargetObservable( firstProperty ),
                        getTargetObservable( secondProperty ) );
        addValidationStatusProvider( firstProperty, provider );
        addValidationStatusProvider( secondProperty, provider );
    }

    public void addValidationStatusProvider(String key, ValidationStatusProvider provider) {
        this.mapExtraValidationStatusProvier.put( key, provider );
    }

    /**
     * TODO: see if needed
     *
     * @param validationGroups
     */
    private void applyValidationGroup(Class< ? >... validationGroups) {
        applyValidationGroup( null, validationGroups );
    }

    /**
     * TODO: see if needed
     *
     * @param propertyPath
     * @param validationGroups
     */
    private void applyValidationGroup(String propertyPath, Class< ? >... validationGroups) {
        final Validator jsr303Validator = Jsr303PropertyValidationSupport.getValidatorFactory()
                        .getValidator();

        Class< ? > pojoClass = this.getPojoType();
        final BeanDescriptor pojoClassDescriptor = jsr303Validator
                        .getConstraintsForClass( pojoClass );

        for ( final String uiBinding : keySet() ) {
            if ( ( propertyPath == null ) || uiBinding.startsWith( propertyPath ) ) {
                final BeanDescriptor beanDescriptor;
                final String property;
                if ( uiBinding.contains( "." ) ) {
                    final String[] arrayProperties = uiBinding.split( "\\." );
                    BeanDescriptor nestedBeanDescriptor = null;
                    Class< ? > nestedClass = pojoClass;
                    for ( int i = 0; i < ( arrayProperties.length - 1 ); i++ ) {
                        Field field = null;
                        String pojoField = arrayProperties[ 0 ];
                        try {
                            field = nestedClass.getDeclaredField( pojoField );
                        }
                        catch ( NoSuchFieldException e ) {
                            throw new IllegalArgumentException(
                                            pojoField + " cannot be found in " + nestedClass, e );
                        }
                        catch ( SecurityException e ) {
                            throw new IllegalArgumentException(
                                            pojoField + " cannot be found in " + nestedClass, e );
                        }
                        nestedClass = field.getType();
                        nestedBeanDescriptor = jsr303Validator
                                        .getConstraintsForClass( nestedClass );
                    }
                    beanDescriptor = nestedBeanDescriptor;
                    property = arrayProperties[ arrayProperties.length - 1 ];
                }
                else {
                    property = uiBinding;
                    beanDescriptor = pojoClassDescriptor;
                }

                final PropertyDescriptor propertyDescriptor = beanDescriptor
                                .getConstraintsForProperty( property );
                if ( propertyDescriptor == null ) {
                    continue;
                }
                final Set< ConstraintDescriptor< ? > > constraints = propertyDescriptor
                                .findConstraints().unorderedAndMatchingGroups( validationGroups )
                                .getConstraintDescriptors();
                if ( !constraints.isEmpty() ) {
                    final DatabindingConfiguration config = getConfiguration( uiBinding );
                    final IValidator validator = new Jsr303PropertyValidator(
                                    beanDescriptor.getElementClass(),
                                    propertyDescriptor.getPropertyName(), true, validationGroups );
                    config.getUpdateValueStrategy().setAfterConvertValidator( validator );
                }

            }

        }
    }

    public Map< String, Binding > bind(DataBindingContext databindingContext, Object pojo) {
        return bind( databindingContext, pojo,
                        new DecorationControlUpdater( SWT.TOP | SWT.RIGHT ) );
    }

    /**
     *
     * @param databindingContext
     * @param pojo
     * @return
     */
    public Map< String, Binding > bind(DataBindingContext databindingContext, Object pojo,
                                       ControlUpdater controlUpdater) {
        if ( !this.initialized ) {
            initialize();
        }
        Map< String, Binding > mapBindings = new HashMap< String, Binding >();
        Class< ? > modelType = (Class< ? >) this.m_oWritableValue.getValueType();
        if ( modelType.isAssignableFrom( pojo.getClass() ) ) {
            this.m_oWritableValue.setValue( pojo );
        }
        else {
            throw new IllegalArgumentException(
                            "Model " + pojo + " does not match declared model type" + modelType );
        }
        for ( Entry< String, DatabindingConfiguration > entry : this.m_mapConfiguration
                        .entrySet() ) {
            String propertyPath = entry.getKey();
            DatabindingConfiguration config = entry.getValue();
            Binding binding = databindingContext.bindValue( config.getTargetObservable(),
                            config.getModelObservable(), config.getUpdateValueStrategy(), null );
            mapBindings.put( propertyPath, binding );

            if ( controlUpdater != null ) {
                final ValidationStatusProvider controlValidationStatusProvider;
                if ( this.mapExtraValidationStatusProvier.containsKey( propertyPath ) ) {
                    controlValidationStatusProvider = new AggregateValidationStatusProvider(
                                    binding,
                                    this.mapExtraValidationStatusProvier.get( propertyPath ) );
                }
                else {
                    controlValidationStatusProvider = binding;
                }
                UpdateControlSupport.create( controlValidationStatusProvider, controlUpdater );
            }
        }

        return mapBindings;

    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildModel(String pojoProperty, DatabindingConfiguration metadata) {
        IBeanValueProperty beanValueProperty = PojoProperties.value( pojoProperty );
        IObservableValue model = beanValueProperty.observeDetail( this.m_oWritableValue );
        metadata.setModelObservable( model );
    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildStrategy(String pojoProperty, DatabindingConfiguration metadata) {
        IBeanObservable modelObservable = (IBeanObservable) metadata.getModelObservable();
        final Class< ? > beanType;
        final String beanPropertyName;

        if ( ( modelObservable.getObserved() != null )
                        && ( modelObservable.getPropertyDescriptor() != null ) ) {
            // model observable contains bean type and proper property name
            beanPropertyName = modelObservable.getPropertyDescriptor().getName();
            beanType = modelObservable.getObserved().getClass();
        }
        else {
            // one information cannot be discovered from model observable. calculate based on
            // propretypath and pojo class
            final Class< ? > calculatedBeanType;
            final String calculatedPropertyName;
            if ( pojoProperty.contains( "." ) ) {
                String[] splitt = pojoProperty.split( "\\." );
                String fieldName = null;
                Class< ? extends Object > innerPojoClass = this.m_classPojo;
                for ( int i = 0; i < ( splitt.length ); i++ ) {
                    fieldName = splitt[ i ];
                    try {
                        Field field = innerPojoClass.getDeclaredField( fieldName );
                        Class< ? > fieldClass = field.getType();
                        // last entry
                        if ( i != ( splitt.length - 1 ) ) {
                            innerPojoClass = fieldClass;
                        }
                    }
                    catch ( NoSuchFieldException e ) {
                        throw new IllegalArgumentException(
                                        fieldName + " cannot be found in " + innerPojoClass, e );
                    }
                    catch ( SecurityException e ) {
                        throw new IllegalArgumentException(
                                        fieldName + " cannot be found in " + innerPojoClass, e );
                    }
                }
                if ( fieldName == null ) {
                    throw new IllegalArgumentException(
                                    pojoProperty + " cannot be found in " + this.m_classPojo );
                }
                calculatedBeanType = innerPojoClass;
                calculatedPropertyName = fieldName;
            }
            else {
                calculatedBeanType = this.m_classPojo;
                calculatedPropertyName = pojoProperty;
            }

            if ( modelObservable.getObserved() == null ) {
                beanType = calculatedBeanType;
            }
            else {
                beanType = modelObservable.getObserved().getClass();
            }

            if ( modelObservable.getPropertyDescriptor() != null ) {
                beanPropertyName = modelObservable.getPropertyDescriptor().getName();
            }
            else {
                beanPropertyName = calculatedPropertyName;
            }
        }

        Class< ? >[] beanValidationGroups = this.validationGroups;
        for ( String prefix : this.mapValidationGroups.keySet() ) {
            if ( pojoProperty.startsWith( prefix + "." ) ) {
                beanValidationGroups = this.mapValidationGroups.get( prefix );
                break;
            }
        }
        if ( beanValidationGroups == null ) {
            metadata.setUpdateValueStrategy( Jsr303UpdateValueStrategyFactory.create( beanType,
                            beanPropertyName, false ) );
        }
        else {
            metadata.setUpdateValueStrategy( Jsr303UpdateValueStrategyFactory.create( beanType,
                            beanPropertyName, false, beanValidationGroups ) );
        }
    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildTarget(String pojoProperty, DatabindingConfiguration metadata) {
        Object control = this.m_oUiContainer.getControl( pojoProperty );
        IObservableValue target = null;
        if ( control instanceof Control ) {
            if ( ( control instanceof Text ) || ( control instanceof Label )
                            || ( control instanceof Button ) ) {
                target = WidgetProperties.text( SWT.Modify ).observe( getRealm(), control );
            }
            else {
                if ( ( control instanceof DateTime ) ) {
                    target = WidgetProperties.selection().observe( getRealm(), control );
                }

                if ( ( control instanceof CCombo ) || ( control instanceof Combo ) ) {
                    target = WidgetProperties.selection().observe( getRealm(), control );
                    // TODO: bind items, question what items
                }
            }

        }
        else {
            LoggerFactory.getLogger( Jsr303DatabindingConfigurator.class ).debug(
                            control.getClass() + " not supported for property  " + pojoProperty );
            // do nothing
        }
        metadata.setTargetObservable( target );
    }

    /**
     *
     */
    protected void buildTargets() {
        for ( Entry< String, DatabindingConfiguration > entry : this.m_mapConfiguration
                        .entrySet() ) {
            String pojoProperty = entry.getKey();
            DatabindingConfiguration metadata = entry.getValue();
            buildTarget( pojoProperty, metadata );
        }
    }

    /**
     * Filter properties, keep those properties present as field in pojoClass
     *
     * @param setBindingProperties
     * @param pojoClass
     * @return
     */
    protected Set< String > filter(Set< String > setBindingProperties,
                                   Class< ? extends Object > pojoClass) {
        Set< String > filtered = new HashSet< String >();
        outer:
        for ( String bindingProperty : setBindingProperties ) {
            if ( bindingProperty.contains( "." ) ) {
                String[] splitt = bindingProperty.split( "\\." );
                Class< ? extends Object > innerPojoClass = pojoClass;
                for ( String fieldName : splitt ) {
                    try {
                        Field field = innerPojoClass.getDeclaredField( fieldName );
                        Class< ? > fieldClass = field.getType();
                        innerPojoClass = fieldClass;
                    }
                    catch ( NoSuchFieldException e ) {
                        // ignore
                        continue outer;
                    }
                    catch ( SecurityException e ) {
                        throw new RuntimeException( "Unable to inspect " + innerPojoClass, e );
                    }
                }
                filtered.add( bindingProperty );
            }
            else {
                try {
                    pojoClass.getDeclaredField( bindingProperty );
                    filtered.add( bindingProperty );
                }
                catch ( NoSuchFieldException e ) {
                    // ignore
                }
                catch ( SecurityException e ) {
                    throw new RuntimeException( "Unable to inspect " + pojoClass, e );
                }
            }
        }
        return filtered;
    }

    public DatabindingConfiguration getConfiguration(String pojoProperty) {
        return this.m_mapConfiguration.get( pojoProperty );
    }

    public Class< ? > getPojoType() {
        return this.m_classPojo;
    }

    public Realm getRealm() {
        return this.m_oRealm;
    }

    public IObservableValue getTargetObservable(String pojoProperty) {
        DatabindingConfiguration databindingMetadata = this.m_mapConfiguration.get( pojoProperty );
        if ( databindingMetadata == null ) {
            throw new IllegalArgumentException(
                            "Databinding information cannot be found for property "
                                            + pojoProperty );
        }
        return databindingMetadata.getTargetObservable();
    }

    /**
     *
     */
    public void initialize() {
        for ( Entry< String, DatabindingConfiguration > entry : this.m_mapConfiguration
                        .entrySet() ) {
            String pojoProperty = entry.getKey();
            DatabindingConfiguration config = entry.getValue();
            buildTarget( pojoProperty, config );
            buildModel( pojoProperty, config );
            buildStrategy( pojoProperty, config );
        }
        this.initialized = true;
    }

    public Set< String > keySet() {
        return this.m_mapConfiguration.keySet();
    }

    public void setValidationGroup(String propertyPath, Class< ? >... validationGroups) {
        this.mapValidationGroups.put( propertyPath, validationGroups );
    }

    public void setValidationGroups(Class< ? >[] oValidationGroups) {
        this.validationGroups = oValidationGroups;
    }

}
