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
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.jsr303.Jsr303PropertyValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303PropertyValidator;
import org.eclipse.core.databinding.validation.jsr303.Jsr303UpdateValueStrategyFactory;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
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

public class Jsr303DatabindingMetadataBuilder {

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

    private Map< String, DatabindingMetadata > m_mapMetadata;
    private UIControlContainer m_oUiContainer;
    private Class< ? > m_classPojo;
    private Realm m_oRealm;

    private WritableValue m_oWritableValue;

    public Jsr303DatabindingMetadataBuilder(Realm realm, UIControlContainer oUiContainer,
                                            Class< ? > oPojoClass) {
        super();
        this.m_oRealm = realm;
        this.m_oUiContainer = oUiContainer;
        this.m_classPojo = oPojoClass;
        Set< String > setPojoProperties = filter( oUiContainer.keySet(), oPojoClass );
        this.m_mapMetadata = new HashMap< String, DatabindingMetadata >( setPojoProperties.size() );
        for ( String property : setPojoProperties ) {
            this.m_mapMetadata.put( property, new DatabindingMetadata( realm ) );
        }
        this.m_oWritableValue = new WritableValue( this.m_oRealm, null, this.m_classPojo );
    }

    public void applyValidationGroup(Class< ? >... validationGroups) {
        applyValidationGroup( null, validationGroups );
    }

    public void applyValidationGroup(String propertyPath, Class< ? >... validationGroups) {
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
                    final DatabindingMetadata metadata = getMetadata( uiBinding );
                    final IValidator validator = new Jsr303PropertyValidator(
                                    beanDescriptor.getElementClass(),
                                    propertyDescriptor.getPropertyName(), true, validationGroups );
                    metadata.getUpdateValueStrategy().setAfterConvertValidator( validator );
                }

            }

        }
    }

    /**
     *
     * @param databindingContext
     * @param pojo
     */
    public void bind(DataBindingContext databindingContext, Object pojo) {
        Class< ? > modelType = (Class< ? >) this.m_oWritableValue.getValueType();
        if ( modelType.isAssignableFrom( pojo.getClass() ) ) {
            this.m_oWritableValue.setValue( pojo );
        }
        else {
            throw new IllegalArgumentException(
                            "Model " + pojo + " does not match declared model type" + modelType );
        }
        for ( Entry< String, DatabindingMetadata > entry : this.m_mapMetadata.entrySet() ) {
            DatabindingMetadata metadata = entry.getValue();
            Binding binding = databindingContext.bindValue( metadata.getTargetObservable(),
                            metadata.getModelObservable(), metadata.getUpdateValueStrategy(),
                            null );
            if ( metadata.getControlDecoratorLocation() != SWT.NONE ) {
                ControlDecorationSupport.create( binding, metadata.getControlDecoratorLocation() );
            }
        }

    }

    /**
     *
     */
    public void buildMetadata() {
        for ( Entry< String, DatabindingMetadata > entry : this.m_mapMetadata.entrySet() ) {
            String pojoProperty = entry.getKey();
            DatabindingMetadata metadata = entry.getValue();
            buildTarget( pojoProperty, metadata );
            buildModel( pojoProperty, metadata );
            buildStrategy( pojoProperty, metadata );
        }
    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildModel(String pojoProperty, DatabindingMetadata metadata) {
        IBeanValueProperty beanValueProperty = PojoProperties.value( pojoProperty );
        IObservableValue model = beanValueProperty.observeDetail( this.m_oWritableValue );
        metadata.setModelObservable( model );
    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildStrategy(String pojoProperty, DatabindingMetadata metadata) {
        IBeanObservable modelObservable = (IBeanObservable) metadata.getModelObservable();
        final Class< ? > beanType;
        if ( modelObservable.getObserved() == null ) {
            // connect them
            if ( pojoProperty.contains( "." ) ) {
                String pojoField = pojoProperty.substring( 0, pojoProperty.lastIndexOf( '.' ) );
                try {
                    Field field = this.m_classPojo.getDeclaredField( pojoField );
                    beanType = field.getType();
                }
                catch ( NoSuchFieldException e ) {
                    throw new IllegalArgumentException(
                                    pojoField + " cannot be found in " + this.m_classPojo, e );
                }
                catch ( SecurityException e ) {
                    throw new IllegalArgumentException(
                                    pojoField + " cannot be found in " + this.m_classPojo, e );
                }
            }
            else {
                beanType = this.m_classPojo;
            }
        }
        else {
            beanType = modelObservable.getObserved().getClass();
        }

        final String beanPropertyName;
        if ( modelObservable.getPropertyDescriptor() != null ) {
            beanPropertyName = modelObservable.getPropertyDescriptor().getName();
        }
        else {
            // connect them
            if ( pojoProperty.contains( "." ) ) {
                beanPropertyName = pojoProperty.substring( pojoProperty.lastIndexOf( '.' ) + 1,
                                pojoProperty.length() );
            }
            else {
                beanPropertyName = pojoProperty;
            }
        }

        metadata.setUpdateValueStrategy( Jsr303UpdateValueStrategyFactory.create( beanType,
                        beanPropertyName, false ) );
    }

    /**
     *
     * @param pojoProperty
     * @param metadata
     */
    protected void buildTarget(String pojoProperty, DatabindingMetadata metadata) {
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
            LoggerFactory.getLogger( Jsr303BindSupport.class ).debug(
                            control.getClass() + " not supported for property  " + pojoProperty );
            // do nothing
        }
        metadata.setTargetObservable( target );
    }

    /**
     *
     */
    protected void buildTargets() {
        for ( Entry< String, DatabindingMetadata > entry : this.m_mapMetadata.entrySet() ) {
            String pojoProperty = entry.getKey();
            DatabindingMetadata metadata = entry.getValue();
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
        for ( String bindingProperty : setBindingProperties ) {
            if ( bindingProperty.contains( "." ) ) {
                String[] splitt = bindingProperty.split( "\\." );
                if ( splitt.length == 2 ) {
                    String fieldName = splitt[ 0 ];
                    try {
                        Field field = pojoClass.getDeclaredField( fieldName );
                        Class< ? > fieldClass = field.getType();
                        Field innerField = fieldClass.getDeclaredField( splitt[ 1 ] );
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

    public DatabindingMetadata getMetadata(String pojoProperty) {
        return this.m_mapMetadata.get( pojoProperty );
    }

    public Class< ? > getPojoType() {
        return this.m_classPojo;
    }

    public Realm getRealm() {
        return this.m_oRealm;
    }

    public Set< String > keySet() {
        return this.m_mapMetadata.keySet();
    }

}
