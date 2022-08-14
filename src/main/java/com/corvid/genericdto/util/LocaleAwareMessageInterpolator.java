package com.corvid.genericdto.util;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;

import java.util.Locale;

public class LocaleAwareMessageInterpolator extends
        ResourceBundleMessageInterpolator {

    private Locale defaultLocale = Locale.getDefault();

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public String interpolate(final String messageTemplate,
                              final Context context) {
        return interpolate(messageTemplate, context, defaultLocale);
    }

    @Override
    public String interpolate(final String messageTemplate,
                              final Context context, final Locale locale) {
        return super.interpolate(messageTemplate, context, locale);
    }
}