package org.eclipse.core.databinding.validation.jsr303.samples.util;

import java.util.Calendar;
import java.util.Date;

/**
 * System preferences are loaded one an cannot be changed during runtime
 *
 * @author mferlan
 *
 */
public class SystemPreferences {

    private static volatile SystemPreferences instance;

    public static SystemPreferences getInstance() {
        if ( SystemPreferences.instance == null ) {
            synchronized ( SystemPreferences.class ) {
                if ( SystemPreferences.instance == null ) {
                    SystemPreferences.instance = new SystemPreferences();
                }
            }
        }
        return SystemPreferences.instance;
    }

    // FIELDS
    private boolean m_bCheckZipFiveDigits;
    private int m_nMinimumUserYears;
    private PasswordComplexity m_oPasswordComplexity;
    private int m_nPasswordMinLength;
    private Long m_nMandantNo;
    private String m_sProviderId;

    private SystemPreferences() {
        // todo: load all system preferences , decide to set default value or throw a failure;
        this.m_bCheckZipFiveDigits = false;
        this.m_nMinimumUserYears = 3;
        this.m_nPasswordMinLength = 6;
        this.m_oPasswordComplexity = PasswordComplexity.APLHABET_NUMBER;
        this.m_sProviderId = System.getProperty( "webtick.provider.id", "0" );
        this.m_nMandantNo = Long.valueOf( this.m_sProviderId );
    }

    public Long getMandantNo() {
        return this.m_nMandantNo;
    }

    public Date getMinimumAge() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.AM_PM, Calendar.AM );
        calendar.set( Calendar.HOUR, 0 );
        calendar.set( Calendar.HOUR_OF_DAY, 0 );
        calendar.set( Calendar.MINUTE, 0 );
        calendar.set( Calendar.SECOND, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );

        calendar.add( Calendar.YEAR, -this.m_nMinimumUserYears );
        return calendar.getTime();
    }

    public int getMinimumUserYears() {
        return this.m_nMinimumUserYears;
    }

    public PasswordComplexity getPasswordComplexity() {
        return this.m_oPasswordComplexity;
    }

    public int getPasswordMinLength() {
        return this.m_nPasswordMinLength;
    }

    public String getProviderId() {
        return this.m_sProviderId;
    }

    /**
     * @return the checkZipFiveDigits
     */
    public boolean isCheckZipFiveDigits() {
        return this.m_bCheckZipFiveDigits;
    }

}
