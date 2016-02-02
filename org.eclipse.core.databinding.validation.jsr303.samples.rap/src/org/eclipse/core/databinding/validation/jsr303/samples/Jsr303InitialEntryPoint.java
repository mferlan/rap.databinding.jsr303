package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.LoggerFactory;

public class Jsr303InitialEntryPoint extends AbstractEntryPoint {

    /**
     *
     */
    private static final long serialVersionUID = -618048721577106082L;

    @Override
    protected void createContents(Composite shell) {

        Composite parent = new Composite( shell, SWT.NONE );
        parent.setLayout( new GridLayout( 2, false ) );
        parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        Button button = new Button( parent, SWT.NONE );
        button.setText( "Registration" );
        button.addSelectionListener( new SelectionAdapter() {

            /**
             *
             */
            private static final long serialVersionUID = 8190874327996321794L;

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                redirect( "registration" );
            };
        } );

        Button buttonEdit = new Button( parent, SWT.NONE );
        buttonEdit.setText( "Edit user" );
        buttonEdit.addSelectionListener( new SelectionAdapter() {

            /**
             *
             */
            private static final long serialVersionUID = 8190874327996321794L;

            @Override
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                redirect( "person" );
            };
        } );

    }

    protected void redirect(String oString) {
        StringBuffer url = RWT.getRequest().getRequestURL();
        url.append( oString );
        try {
            RWT.getClient().getService( JavaScriptExecutor.class )
                            .execute( "parent.window.location.href=\"" + url + "\";" );
        }
        catch ( Exception e ) {
            LoggerFactory.getLogger( getClass() ).error( "Error redirecting  to " + url, e );
        }

    }

}
