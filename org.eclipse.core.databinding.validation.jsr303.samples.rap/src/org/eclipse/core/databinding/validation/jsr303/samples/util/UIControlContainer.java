package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UIControlContainer {
    private Map< String, Object > m_mapId2control = new HashMap< String, Object >();

    public < T > T getControl(String bindingProperty) {
        return (T) this.m_mapId2control.get( bindingProperty );
    }

    public < T > T getControl(String bindingProperty, Class< ? extends T > clazz) {
        return (T) this.m_mapId2control.get( bindingProperty );
    }

    public Set< String > keySet() {
        return this.m_mapId2control.keySet();
    }

    public void merge(String prefix, UIControlContainer uiContainer) {
        for ( String key : uiContainer.keySet() ) {
            put( prefix + "." + key, uiContainer.getControl( key ) );
        }
    }

    public Object put(String bindingProperty, Object control) {
        if ( this.m_mapId2control.containsKey( bindingProperty ) ) {
            final String format = "conflict: control with id '%s' already defined: %s, %s"; //$NON-NLS-1$
            final String msg = String.format( format, bindingProperty,
                            this.m_mapId2control.get( bindingProperty ), control );
            throw new RuntimeException( msg );
        }
        return this.m_mapId2control.put( bindingProperty, control );
    }

}
