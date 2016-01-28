package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.lang.reflect.Field;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

public class Jsr303RequiredControlDecoratorSupport {

    private static String constructControlId(String parentBindingId, String name,
                                             boolean markLabel) {
        StringBuilder sb = new StringBuilder();
        if ( parentBindingId != null ) {
            sb.append( parentBindingId );
            sb.append( "." );
        }
        sb.append( name );
        if ( markLabel ) {
            sb.append( "Label" );
        }
        return sb.toString();
    }

    public static void create(UIControlContainer container, Class< ? > pojoClazz, boolean markLabel,
                              int location) {

        Jsr303RequiredControlDecoratorSupport.create( container, pojoClazz, null, markLabel,
                        location );

        Set< Field > embededObjectFields = ReflectionUtils.findFields( pojoClazz, Valid.class );
        for ( Field field : embededObjectFields ) {
            Jsr303RequiredControlDecoratorSupport.create( container, field.getType(),
                            field.getName(), markLabel, location );
        }

    }

    private static void create(UIControlContainer container, Class< ? > pojoClazz,
                               String parentBindingId, boolean markLabel, int location) {
        Set< Field > requiredFields = ReflectionUtils.findFields( pojoClazz, NotNull.class );
        for ( Field field : requiredFields ) {
            final Control control;
            control = container.getControl( Jsr303RequiredControlDecoratorSupport
                            .constructControlId( parentBindingId, field.getName(), markLabel ) );
            if ( control != null ) {
                Jsr303RequiredControlDecoratorSupport.markAsRequired( control, "Field is required",
                                location );
            }
        }
    }

    private static void markAsRequired(Control control, String sTooltip, int location) {

        // create the decoration for the text UI component
        final ControlDecoration deco = new ControlDecoration( control, location );

        // re-use an existing image
        Image image = FieldDecorationRegistry.getDefault()
                        .getFieldDecoration( FieldDecorationRegistry.DEC_REQUIRED ).getImage();
        // set description and image
        if ( sTooltip != null ) {
            deco.setDescriptionText( sTooltip );
        }
        deco.setImage( image );
        // hide deco if not in focus
        // deco.setShowOnlyOnFocus( false );
    }
}
