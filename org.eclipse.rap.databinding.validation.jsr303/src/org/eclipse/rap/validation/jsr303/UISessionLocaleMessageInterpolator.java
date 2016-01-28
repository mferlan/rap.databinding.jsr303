package org.eclipse.rap.validation.jsr303;

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.eclipse.rap.rwt.RWT;

public class UISessionLocaleMessageInterpolator implements MessageInterpolator {
    private MessageInterpolator delegate;

    public UISessionLocaleMessageInterpolator(MessageInterpolator oDelegate) {
        super();
        this.delegate = oDelegate;
    }

    public String interpolate(String oArg0, Context oArg1) {
        return this.delegate.interpolate( oArg0, oArg1, RWT.getLocale() );
    }

    public String interpolate(String oArg0, Context oArg1, Locale oArg2) {
        return this.delegate.interpolate( oArg0, oArg1, RWT.getLocale() );
    }

}
