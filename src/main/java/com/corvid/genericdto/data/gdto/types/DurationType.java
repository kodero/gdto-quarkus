package com.corvid.genericdto.data.gdto.types;

import com.corvid.genericdto.shared.time.Duration;
import com.corvid.genericdto.util.LoggingUtil;

import org.jboss.logging.Logger.Level;

/**
 * Created with IntelliJ IDEA.
 * User: mokua,kodero
 * Date: 4/17/14
 * Time: 9:15 AM
 */
public class DurationType extends AbstractType<Duration> {

    public DurationType(String regExp) {
        super(regExp);
    }

    public DurationType(String regExp, String contentAsString) {
        super(regExp);
        this.instantiateFromString(contentAsString);
    }

    public DurationType() {
        this(null);
    }

    /**
     * 10 days
     *
     * @param content
     * @return
     */
    protected Duration construct(String content) {
        LoggingUtil.log(DurationType.class, Level.DEBUG, String.format("Construct duration instance from string [%s]", content));
        if (content == null) return null;
        String[] contentArray = content.split(" ");
        assert contentArray.length == 2;
        final String qtyString = contentArray[0];
        final String units = contentArray[1];
        final long value = Long.valueOf(qtyString);
        String ss = units.endsWith("s") ? units.substring(0, units.length() - 1) : units;
        return Duration.of(value, ss);
    }

    @Override
    public String toString() {
        return "{" +
                "DurationType ='" + getValue() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DurationType)) return false;
        DurationType that = (DurationType) o;
        if (this.t.compareTo(that.t) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return t.hashCode();
    }
}