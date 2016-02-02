package org.eclipse.core.databinding.decoration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.DisposeEvent;
import org.eclipse.core.databinding.observable.IDecoratingObservable;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffVisitor;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.viewers.IViewerObservable;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Updates the underlying controls of the target observables of a {@link ValidationStatusProvider}
 * with {@link ControlUpdater}s mirroring the current validation status. Only those target
 * observables which implement {@link ISWTObservable} or {@link IViewerObservable} are decorated.
 *
 */
public class UpdateControlSupport {
    private static class TargetTupple {
        public final IObservable target;
        public final Control control;

        TargetTupple(IObservable target, Control control) {
            this.target = target;
            this.control = control;
        }
    }

    /**
     * Creates a {@link UpdateControlSupport} which observes the validation status of the specified
     * {@link ValidationStatusProvider}, and updates underlying SWT control of all target
     * observables that implement {@link ISWTObservable} or {@link IViewerObservable}.
     *
     * @param validationStatusProvider
     *            the {@link ValidationStatusProvider} to monitor.
     * @param composite
     *            the composite to use when constructing {@link ControlDecoration} instances.
     * @param updater
     *            custom strategy for updating the {@link ControlDecoration}(s) whenever the
     *            validation status changes.
     * @return a UpdateControlSupport which observes the validation status of the specified
     *         {@link ValidationStatusProvider}, and updates a underlying SWT control of all target
     *         observables that implement {@link ISWTObservable} or {@link IViewerObservable}.
     */
    public static UpdateControlSupport create(ValidationStatusProvider validationStatusProvider,
                                              ControlUpdater updater) {
        return new UpdateControlSupport( validationStatusProvider, updater );
    }

    /**
     * Creates a {@link UpdateControlSupport} which observes the validation status of the specified
     * {@link ValidationStatusProvider}, and updates underlying SWT control of all target
     * observables that implement {@link ISWTObservable} or {@link IViewerObservable}.
     *
     * @param validationStatusProvider
     *            the {@link ValidationStatusProvider} to monitor.
     * @param position
     * @return a {@link UpdateControlSupport} which observes the validation status of the specified
     *         {@link ValidationStatusProvider}, and displays a {@link ControlDecoration} over the
     *         underlying SWT control of all target observables that implement
     *         {@link ISWTObservable} or {@link IViewerObservable}.
     */
    public static UpdateControlSupport create(ValidationStatusProvider validationStatusProvider,
                                              int position) {
        return UpdateControlSupport.create( validationStatusProvider,
                        new DecorationControlUpdater( position ) );
    }

    private final ControlUpdater updater;
    private IObservableValue validationStatus;

    private IObservableList targets;

    private IDisposeListener disposeListener = new IDisposeListener() {
        public void handleDispose(DisposeEvent staleEvent) {
            dispose();
        }
    };

    private IValueChangeListener statusChangeListener = new IValueChangeListener() {
        public void handleValueChange(ValueChangeEvent event) {
            statusChanged( (IStatus) UpdateControlSupport.this.validationStatus.getValue() );
        }
    };

    private IListChangeListener targetsChangeListener = new IListChangeListener() {
        public void handleListChange(ListChangeEvent event) {
            event.diff.accept( new ListDiffVisitor() {
                @Override
                public void handleAdd(int index, Object element) {
                    targetAdded( (IObservable) element );
                }

                @Override
                public void handleRemove(int index, Object element) {
                    targetRemoved( (IObservable) element );
                }
            } );
            statusChanged( (IStatus) UpdateControlSupport.this.validationStatus.getValue() );
        }
    };

    private List targetTupples;

    private UpdateControlSupport(ValidationStatusProvider validationStatusProvider,
                                 ControlUpdater updater) {
        this.updater = updater;

        this.validationStatus = validationStatusProvider.getValidationStatus();
        Assert.isTrue( !this.validationStatus.isDisposed() );

        this.targets = validationStatusProvider.getTargets();
        Assert.isTrue( !this.targets.isDisposed() );

        this.targetTupples = new ArrayList();

        this.validationStatus.addDisposeListener( this.disposeListener );
        this.validationStatus.addValueChangeListener( this.statusChangeListener );

        this.targets.addDisposeListener( this.disposeListener );
        this.targets.addListChangeListener( this.targetsChangeListener );

        for ( Iterator it = this.targets.iterator(); it.hasNext(); ) {
            targetAdded( (IObservable) it.next() );
        }

        statusChanged( (IStatus) this.validationStatus.getValue() );
    }

    /**
     * Disposes this ControlDecorationSupport, including all control decorations managed by it. A
     * {@link UpdateControlSupport} is automatically disposed when its target
     * ValidationStatusProvider is disposed.
     */
    public void dispose() {
        if ( this.validationStatus != null ) {
            this.validationStatus.removeDisposeListener( this.disposeListener );
            this.validationStatus.removeValueChangeListener( this.statusChangeListener );
            this.validationStatus = null;
        }

        if ( this.targets != null ) {
            this.targets.removeDisposeListener( this.disposeListener );
            this.targets.removeListChangeListener( this.targetsChangeListener );
            this.targets = null;
        }

        this.disposeListener = null;
        this.statusChangeListener = null;
        this.targetsChangeListener = null;

        if ( this.targetTupples != null ) {
            this.targetTupples.clear();
            this.targetTupples = null;
        }
    }

    private Control findControl(IObservable target) {
        if ( target instanceof ISWTObservable ) {
            Widget widget = ( (ISWTObservable) target ).getWidget();
            if ( widget instanceof Control ) {
                return (Control) widget;
            }
        }

        if ( target instanceof IViewerObservable ) {
            Viewer viewer = ( (IViewerObservable) target ).getViewer();
            return viewer.getControl();
        }

        if ( target instanceof IDecoratingObservable ) {
            IObservable decorated = ( (IDecoratingObservable) target ).getDecorated();
            Control control = findControl( decorated );
            if ( control != null ) {
                return control;
            }
        }

        if ( target instanceof IObserving ) {
            Object observed = ( (IObserving) target ).getObserved();
            if ( observed instanceof IObservable ) {
                return findControl( (IObservable) observed );
            }
        }

        return null;
    }

    private void statusChanged(IStatus status) {
        for ( Iterator it = this.targetTupples.iterator(); it.hasNext(); ) {
            TargetTupple targetDecoration = (TargetTupple) it.next();
            this.updater.update( targetDecoration.control, status );
        }
    }

    private void targetAdded(IObservable target) {
        Control control = findControl( target );
        if ( control != null ) {
            this.targetTupples.add( new TargetTupple( target, control ) );
        }
    }

    private void targetRemoved(IObservable target) {
        for ( Iterator it = this.targetTupples.iterator(); it.hasNext(); ) {
            TargetTupple targetDecoration = (TargetTupple) it.next();
            if ( targetDecoration.target == target ) {
                it.remove();
            }
        }
    }
}
