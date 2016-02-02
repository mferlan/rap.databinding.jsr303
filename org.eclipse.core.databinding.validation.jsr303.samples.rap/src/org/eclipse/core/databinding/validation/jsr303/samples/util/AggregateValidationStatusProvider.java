package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.IObservableCollection;
import org.eclipse.core.databinding.observable.Observables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 *
 *
 */
public class AggregateValidationStatusProvider extends ValidationStatusProvider {
    /**
     *
     * @param binding
     * @param extraProviders
     * @return
     */
    private static IObservableCollection constructObservableCollection(Binding binding,
                                                                       ValidationStatusProvider... extraProviders) {
        final List< ValidationStatusProvider > list = new ArrayList< ValidationStatusProvider >();
        list.add( binding );
        for ( final ValidationStatusProvider provider : extraProviders ) {
            list.add( provider );
        }
        final Realm realm = binding.getModel().getRealm();
        return Observables.staticObservableList( realm, list );
    }

    private IObservableValue validationStatus;
    private IObservableList targets;

    private IObservableList models;

    /**
     *
     * @param binding
     * @param extraProviders
     */
    public AggregateValidationStatusProvider(Binding binding,
                                             ValidationStatusProvider... extraProviders) {
        this( AggregateValidationStatusProvider.constructObservableCollection( binding,
                        extraProviders ) );
    }

    public AggregateValidationStatusProvider(IObservableCollection observableCollection) {
        super();
        Realm realm = observableCollection.getRealm();
        this.validationStatus = new AggregateValidationStatus( realm, observableCollection,
                        AggregateValidationStatus.MAX_SEVERITY );
        ValidationStatusProvider first = (ValidationStatusProvider) observableCollection.iterator()
                        .next();
        this.targets = first.getTargets();
        this.models = first.getModels();
    }

    @Override
    public void dispose() {
        if ( this.validationStatus != null ) {
            this.validationStatus.dispose();
            this.validationStatus = null;
        }
        super.dispose();
    }

    @Override
    public IObservableList getModels() {
        return this.models;
    }

    @Override
    public IObservableList getTargets() {
        return this.targets;
    }

    @Override
    public IObservableValue getValidationStatus() {
        return this.validationStatus;
    }

}
