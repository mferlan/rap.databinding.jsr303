package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;

public class DatabindingMetadata {
    private Realm realm;
    private IObservableValue targetObservable;
    private IObservableValue modelObservable;
    private int controlDecoratorLocation = SWT.TOP | SWT.RIGHT;
    private UpdateValueStrategy updateValueStrategy;

    public DatabindingMetadata(Realm oRealm) {
        super();
        this.realm = oRealm;
    }

    public int getControlDecoratorLocation() {
        return this.controlDecoratorLocation;
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

    public void setControlDecoratorLocation(int oControlDecoratorLocation) {
        this.controlDecoratorLocation = oControlDecoratorLocation;
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
