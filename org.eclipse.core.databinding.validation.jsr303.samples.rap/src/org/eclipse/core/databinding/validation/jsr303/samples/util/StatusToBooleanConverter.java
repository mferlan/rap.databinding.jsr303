package org.eclipse.core.databinding.validation.jsr303.samples.util;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IStatus;

public class StatusToBooleanConverter implements IConverter {

    public Object convert(Object oFromObject) {
        IStatus status = (IStatus) oFromObject;
        return status.isOK();
    }

    public Object getFromType() {
        return IStatus.class;
    }

    public Object getToType() {
        return Boolean.class;
    }

}
