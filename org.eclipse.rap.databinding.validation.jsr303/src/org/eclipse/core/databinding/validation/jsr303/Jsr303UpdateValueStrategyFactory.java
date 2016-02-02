package org.eclipse.core.databinding.validation.jsr303;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.runtime.MultiStatus;

/**
 * {@link UpdateValueStrategy} factory to create instance of {@link UpdateValueStrategy} configured
 * with {@link Jsr303PropertyValidator} to validate a property of a class beanType.
 *
 */
public class Jsr303UpdateValueStrategyFactory {

    /**
     * Create {@link UpdateValueStrategy} configured with {@link Jsr303PropertyValidator} to
     * validate the given propertyName of the given class beanType.
     *
     * @param beanType
     *            the class type of the Pojo to validate (ex: Person.class).
     * @param propertyName
     *            the property name to validate (ex: "email" if there is Person#getEmail()).
     * @param multiStatus
     *            true if validator must return a {@link MultiStatus} with the whole errors and
     *            false otherwise.
     * @param provideDefaults
     *            if <code>true</code>, default validators and a default converter will be provided
     *            based on the observable value's type.
     * @param updatePolicy
     *            one of {@link #POLICY_NEVER}, {@link #POLICY_ON_REQUEST}, {@link #POLICY_CONVERT},
     *            or {@link #POLICY_UPDATE}
     * @return
     */
    public static UpdateValueStrategy create(final Class beanType, final String propertyName,
                                             boolean multiStatus, boolean provideDefaults,
                                             int updatePolicy, Class< ? >... validationGroups) {
        UpdateValueStrategy strategy = new UpdateValueStrategy( provideDefaults, updatePolicy );
        strategy.setAfterConvertValidator( new Jsr303PropertyValidator( beanType, propertyName,
                        multiStatus, validationGroups ) );
        return strategy;
    }

    /**
     * Create {@link UpdateValueStrategy} configured with {@link Jsr303PropertyValidator} to
     * validate the given propertyName of the given class beanType.
     *
     * @param beanType
     *            the class type of the Pojo to validate (ex: Person.class).
     * @param propertyName
     *            the property name to validate (ex: "email" if there is Person#getEmail()).
     * @param multiStatus
     *            true if validator must return a {@link MultiStatus} with the whole errors and
     *            false otherwise.
     * @return
     */
    public static UpdateValueStrategy create(final Class beanType, final String propertyName,
                                             boolean multiStatus, Class< ? >... validationGroups) {
        UpdateValueStrategy strategy = new UpdateValueStrategy();
        strategy.setAfterConvertValidator( new Jsr303PropertyValidator( beanType, propertyName,
                        multiStatus, validationGroups ) );
        return strategy;
    }

    /**
     * Create {@link UpdateValueStrategy} configured with {@link Jsr303PropertyValidator} to
     * validate the given propertyName of the given class beanType.
     *
     * @param beanType
     *            the class type of the Pojo to validate (ex: Person.class).
     * @param propertyName
     *            the property name to validate (ex: "email" if there is Person#getEmail()).
     * @param provideDefaults
     *            if <code>true</code>, default validators and a default converter will be provided
     *            based on the observable value's type.
     * @param updatePolicy
     *            one of {@link #POLICY_NEVER}, {@link #POLICY_ON_REQUEST}, {@link #POLICY_CONVERT},
     *            or {@link #POLICY_UPDATE}
     * @return
     */
    public static UpdateValueStrategy create(final Class beanType, final String propertyName,
                                             boolean provideDefaults, int updatePolicy,
                                             Class< ? >... validationGroups) {
        return Jsr303UpdateValueStrategyFactory.create( beanType, propertyName, false,
                        provideDefaults, updatePolicy, validationGroups );
    }

    /**
     * Create {@link UpdateValueStrategy} configured with {@link Jsr303PropertyValidator} to
     * validate the given propertyName of the given class beanType.
     *
     * @param beanType
     *            the class type of the Pojo to validate (ex: Person.class).
     * @param propertyName
     *            the property name to validate (ex: "email" if there is Person#getEmail()).
     * @return
     */
    public static UpdateValueStrategy create(final Class beanType, final String propertyName,
                                             Class< ? >... validationGroups) {
        return Jsr303UpdateValueStrategyFactory.create( beanType, propertyName, false,
                        validationGroups );
    }

}
