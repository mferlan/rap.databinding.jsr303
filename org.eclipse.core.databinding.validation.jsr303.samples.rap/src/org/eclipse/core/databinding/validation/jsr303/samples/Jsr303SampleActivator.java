package org.eclipse.core.databinding.validation.jsr303.samples;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.validation.MessageInterpolator;
import javax.validation.ValidatorFactory;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.Application.OperationMode;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.application.ExceptionHandler;
import org.eclipse.rap.rwt.client.WebClient;
import org.eclipse.rap.validation.jsr303.ValidatorFactoryBuilder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Jsr303SampleActivator implements BundleActivator {

    class Jsr303ExampleApplication implements ApplicationConfiguration {

        public void configure(Application application) {
            Map< String, String > properties = new HashMap< String, String >();

            properties.put( WebClient.PAGE_TITLE, "Jsr303 Sample" );

            application.setOperationMode( OperationMode.SWT_COMPATIBILITY );

            application.addEntryPoint( "/", Jsr303InitialEntryPoint.class, properties );
            application.addEntryPoint( "/person", Jsr303PersonEntryPoint.class, properties );
            application.addEntryPoint( "/registration", Jsr303RegistrationEntryPoint2.class,
                            properties );
            application.setExceptionHandler( new Jsr303SampleExceptionHandler() );

            MessageInterpolator messageInterpolator;
            messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(
                            "MyValidationMessages", Jsr303SampleActivator.class );
            ValidatorFactoryBuilder builder = new ValidatorFactoryBuilder();
            builder.setMessageInterpolator( messageInterpolator );

            ValidatorFactory validatorFactory = builder.build();

            System.out.println( validatorFactory );
            application.setAttribute( ValidatorFactory.class.getName(), validatorFactory );
        }

    }

    class Jsr303SampleExceptionHandler implements ExceptionHandler {

        public void handleException(Throwable oThrowable) {
            oThrowable.printStackTrace();
        }

    }

    private ServiceRegistration< ? > applicationConfigurationRegistration;

    public void start(BundleContext context) throws Exception {

        Dictionary< String, String > properties = new Hashtable< String, String >();
        properties.put( "contextPath", "webtick" );
        this.applicationConfigurationRegistration = context.registerService(
                        ApplicationConfiguration.class.getName(), new Jsr303ExampleApplication(),
                        properties );

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
     */
    public void stop(BundleContext context) throws Exception {

        if ( this.applicationConfigurationRegistration != null ) {
            this.applicationConfigurationRegistration.unregister();
            this.applicationConfigurationRegistration = null;
        }

    }

}
