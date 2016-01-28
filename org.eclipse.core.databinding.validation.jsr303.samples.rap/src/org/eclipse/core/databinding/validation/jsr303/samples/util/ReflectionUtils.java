package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Constraint;

import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;

public class ReflectionUtils {
    /**
     * @return null safe set
     */
    public static Set< Field > findFields(Class< ? > classs, Class< ? extends Annotation > ann) {
        Set< Field > set = new HashSet< Field >();
        Class< ? > c = classs;
        while ( c != null ) {
            for ( Field field : c.getDeclaredFields() ) {
                if ( !java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
                    if ( field.isAnnotationPresent( ann ) ) {
                        set.add( field );
                    }
                }

            }
            c = c.getSuperclass();
        }
        return set;
    }

    public static Set< Field > findJsr303AnnotatedFields(Class< ? > classs) {
        Set< Field > set = new HashSet< Field >();
        Class< ? > c = classs;
        while ( c != null ) {
            for ( Field field : c.getDeclaredFields() ) {
                if ( !java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
                    Annotation[] annotations = field.getAnnotations();
                    for ( Annotation annotation : annotations ) {
                        Constraint[] constraint = annotation.getClass()
                                        .getAnnotationsByType( javax.validation.Constraint.class );
                        if ( constraint.length > 0 ) {
                            set.add( field );
                        }
                    }
                }

            }
            c = c.getSuperclass();
        }
        return set;
    }

    public static void main(String[] args) {
        ReflectionUtils.findFields( Person.class, java.lang.annotation.Annotation.class );
    }
}
