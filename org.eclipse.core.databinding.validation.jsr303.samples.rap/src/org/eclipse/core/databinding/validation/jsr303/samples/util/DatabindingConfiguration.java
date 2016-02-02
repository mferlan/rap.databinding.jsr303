package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;

public class DatabindingConfiguration {
    private Realm realm;
    private IObservableValue targetObservable;
    private IObservableValue modelObservable;
    private UpdateValueStrategy updateValueStrategy;

    public DatabindingConfiguration(Realm oRealm) {
        super();
        this.realm = oRealm;
    }

    public IObservableValue getModelObservable() {
        return this.modelObservable;
    }

    public Realm getRealm() {
        return this.realm;
    }

    public IObservableValue getTargetObservable() {
        return this.targetObservable;
    }

    public UpdateValueStrategy getUpdateValueStrategy() {
        return this.updateValueStrategy;
    }

    public void setModelObservable(IObservableValue oModelObservable) {
        this.modelObservable = oModelObservable;
    }

    public void setTargetObservable(IObservableValue oTargetObservable) {
        this.targetObservable = oTargetObservable;
    }

    public void setUpdateValueStrategy(UpdateValueStrategy oUpdateValueStrategy) {
        this.updateValueStrategy = oUpdateValueStrategy;
    }

}
