package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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

/**
 * use {@link Jsr303DatabindingMetadataBuilder}
 * 
 * @author mferlan
 *
 */
@Deprecated
public class Jsr303BindSupport {

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

    public static DataBindingContext bindPojo(DataBindingContext dataBindingContext,
                                              UIControlContainer container, Object pojo,
                                              int controlDecorationLocation) {
        Jsr303BindSupport support = new Jsr303BindSupport( dataBindingContext, container,
                        controlDecorationLocation );
        support.bind( pojo.getClass(), pojo );

        return support.dataBindingContext;
    }

    /**
     *
     * @param dataBindingContext
     * @param container
     * @param pojo
     * @param controlDecorationLocation
     * @return
     */
    public static DataBindingContext bindWriteableValue(DataBindingContext dataBindingContext,
                                                        UIControlContainer container,
                                                        WritableValue writeableValue,
                                                        int controlDecorationLocation) {
        Jsr303BindSupport support = new Jsr303BindSupport( dataBindingContext, container,
                        controlDecorationLocation );
        support.bind( (Class< ? >) writeableValue.getValueType(), writeableValue );

        return support.dataBindingContext;
    }

    private DataBindingContext dataBindingContext;
    private UIControlContainer uiControlContainer;
    private int controlDecorationLocation;

    private Jsr303BindSupport(DataBindingContext dataBindingContext,
                              UIControlContainer uiControlContainer,
                              int controlDecorationLocation) {
        super();
        this.uiControlContainer = uiControlContainer;
        this.controlDecorationLocation = controlDecorationLocation;
        this.dataBindingContext = dataBindingContext;
    }

    /**
     *
     * @param container
     * @param pojo
     */
    protected void bind(Class< ? > pojoClass, Object pojo) {
        for ( String key : filter( this.uiControlContainer.keySet(), pojoClass ) ) {
            Object control = this.uiControlContainer.getControl( key );
            if ( control instanceof Control ) {
                if ( ( control instanceof Text ) || ( control instanceof Label )
                                || ( control instanceof Button ) ) {
                    bindText( control, pojo, key );
                }
                else {
                    if ( ( control instanceof DateTime ) ) {
                        bindSelection( control, pojo, key );
                    }

                    if ( ( control instanceof CCombo ) || ( control instanceof Combo ) ) {
                        bindSelection( control, pojo, key );
                        // TODO: bind items, question what items
                    }
                }

            }
            else {
                LoggerFactory.getLogger( getClass() )
                                .debug( control.getClass() + " not supported for binding  "
                                                + pojo.getClass().getName() + "#" + key );
                // do nothing
            }
        }

    }

    protected void bindSelection(Object control, Object pojo, String pojoProperty) {
        IObservableValue target = WidgetProperties.selection().observe( this.getValidationRealm(),
                        control );
        bindValue( target, pojo, pojoProperty );
    }

    protected void bindText(Object control, Object pojo, String pojoProperty) {
        IObservableValue target = WidgetProperties.text( SWT.Modify )
                        .observe( this.getValidationRealm(), control );
        bindValue( target, pojo, pojoProperty );
    }

    private void bindValue(IObservableValue target, Object pojo, String pojoProperty) {
        IBeanValueProperty beanValueProperty = PojoProperties.value( pojoProperty );
        IObservableValue model;
        if ( pojo instanceof IObservableValue ) {
            model = beanValueProperty.observeDetail( (IObservableValue) pojo );
        }
        else {
            model = beanValueProperty.observe( this.getValidationRealm(), pojo );
        }

        Class beanType = ( (IBeanObservable) model ).getObserved().getClass();
        // connect them
        String beanPropertyName;
        if ( pojoProperty.contains( "." ) ) {
            beanPropertyName = pojoProperty.substring( pojoProperty.lastIndexOf( '.' ) + 1,
                            pojoProperty.length() );
        }
        else {
            beanPropertyName = pojoProperty;
        }
        Binding bindValue = this.dataBindingContext.bindValue( target, model,
                        Jsr303UpdateValueStrategyFactory.create( beanType, beanPropertyName,
                                        false ),
                        null );

        // add some decorations to the control
        ControlDecorationSupport.create( bindValue, this.controlDecorationLocation, null );
    }

    private Set< String > filter(Set< String > setBindingProperties,
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

    private < T > T getControl(String oBindingProperty) {
        return this.uiControlContainer.getControl( oBindingProperty );
    }

    private < T > T getControl(String oBindingProperty, Class< ? extends T > oClazz) {
        return this.uiControlContainer.getControl( oBindingProperty, oClazz );
    }

    private Realm getValidationRealm() {
        return this.dataBindingContext.getValidationRealm();
    }

}
