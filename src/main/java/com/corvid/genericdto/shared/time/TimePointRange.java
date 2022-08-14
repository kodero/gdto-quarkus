/**
 * Copyright (c) Mar 28, 2012 10:08:22 AM mokua,kodero
 */
package com.corvid.genericdto.shared.time;

import javax.persistence.*;

/**
 * range between two time points
 * note thats its a scalar range
 *
 * @author 10:08:22 AM mokua,kodero
 */
@Embeddable
public class TimePointRange {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "START_HOUR")),
            @AttributeOverride(name = "minute", column = @Column(name = "START_MINUTE"))
    })
    private TimeOfDay start;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "END_HOUR")),
            @AttributeOverride(name = "minute", column = @Column(name = "END_MINUTE"))
    })
    private TimeOfDay end;

    /**
     * @param start
     * @param end
     */
    public TimePointRange(TimeOfDay start, TimeOfDay end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     *
     */
    public TimePointRange() {
        super();
    }

    public TimeOfDay getStart() {
        return start;
    }

    public void setStart(TimeOfDay start) {
        this.start = start;
    }

    public TimeOfDay getEnd() {
        return end;
    }

    public void setEnd(TimeOfDay end) {
        this.end = end;
    }


}
