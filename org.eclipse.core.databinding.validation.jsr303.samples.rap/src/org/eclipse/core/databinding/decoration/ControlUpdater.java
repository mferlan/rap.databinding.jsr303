package org.eclipse.core.databinding.decoration;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Control;

public interface ControlUpdater {

    public void update(Control control, IStatus status);

}
