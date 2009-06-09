/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.test.performance;

import static org.apache.commons.lang.Validate.notNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Mitko Kolev
 */
public class Timestamp implements Serializable {

    private static final long serialVersionUID = 5310347155712235170L;
    
    private static final TimeUnit NANO_UNIT = TimeUnit.NANOSECONDS;
    
    private final long nanoValue;

    public Timestamp(Long value, TimeUnit unit) {
        notNull(unit, "The unit must not be null!");
        notNull(value, "The value must not be null!");
        this.nanoValue = NANO_UNIT.convert(value, unit);
    }

    public boolean isAfterOrEqual(Timestamp timestamp) {
        return !isBefore(timestamp);
    }

    public boolean isBefore(Timestamp timestamp) {
        notNull(timestamp, "The timestamp must not be null!");
        long delta = nanoValue - timestamp.nanoValue;
        if (delta >= 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return the timestamp value with nanosecond precision
     */
    public long getNanoValue() {
        return nanoValue;
    }

    /**
     * Returns the timestamp value in the given unit
     * 
     * @param unit
     * @return
     */
    public long getValue(TimeUnit unit) {
        notNull(unit, "The unit must not be null!");
        return unit.convert(nanoValue, NANO_UNIT);
    }

    /**
     * @return the timestamp unit
     */
    public TimeUnit getUnit() {
        return NANO_UNIT;
    }

    public String format(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(getValue(TimeUnit.MILLISECONDS));
        return format.format(date);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }if (obj == null){
            return false;
        }if (getClass() != obj.getClass()){
            return false;
        }
        Timestamp other = (Timestamp) obj;
        if (nanoValue != other.nanoValue){
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (nanoValue ^ (nanoValue >>> 32));
        return result;
    }

}
