package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.observable.value.IObservableValue;

public interface IObservableValueFactory {

    public IObservableValue create(Object valueHolder, String valuePath);

}
