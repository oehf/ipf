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

import static org.apache.commons.lang3.Validate.notNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mitko Kolev
 */
@XmlRootElement(name = "measurementHistory", namespace = "http://www.openehealth.org/ipf/commons/test/performance/types/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeasurementHistory implements Serializable {

    private static final long serialVersionUID = -5551291616837750950L;

    @XmlElementRef(name = "measurements")
    private List<Measurement> measurements;

    @XmlElement(name = "referenceDate", type = Date.class)
    private Date referenceDate;

    public MeasurementHistory() {
        measurements = new ArrayList<Measurement>();
        referenceDate = new Date();
    }

    public MeasurementHistory(Date referenceDate) {
        notNull(referenceDate, "The referenceDate must not be null!");
        measurements = new ArrayList<Measurement>();
        this.referenceDate = referenceDate;
    }

    public MeasurementHistory(MeasurementHistory copy) {
        notNull(copy, "The copy must not be null!");

        referenceDate = new Date(copy.referenceDate.getTime());
        measurements = new ArrayList<Measurement>(copy.measurements);

    }

    /**
     * @return the measurements
     */
    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(measurements);
    }

    /**
     * @param measurements
     *            the measurements to set
     */
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    /**
     * @return the referenceDate
     */
    public Date getReferenceDate() {
        return referenceDate;
    }

    /**
     * @param referenceDate
     *            the referenceDate to set
     */
    public void setReferenceDate(Date referenceDate) {
        this.referenceDate = referenceDate;
    }

    public void add(Measurement measurement) {
        notNull(measurement, "The measurement must not be null!");
        measurements.add(measurement);
    }

    public Measurement getFirstMeasurement() {
        return measurements.get(0);
    }

    public Measurement getLastMeasurement() {
        return measurements.get(measurements.size() - 1);
    }

    public Timestamp getFirstMeasurementTimestamp() {
        return measurements.get(0).getTimestamp();
    }

    public Timestamp getLastMeasurementTimestamp() {
        return measurements.get(measurements.size() - 1).getTimestamp();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MeasurementHistory other = (MeasurementHistory) obj;
        if (measurements == null) {
            if (other.measurements != null) {
                return false;
            }
        } else if (!measurements.equals(other.measurements)) {
            return false;
        }
        if (referenceDate == null) {
            if (other.referenceDate != null) {
                return false;
            }
        } else if (!referenceDate.equals(other.referenceDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((measurements == null) ? 0 : measurements.hashCode());
        result = prime * result
                + ((referenceDate == null) ? 0 : referenceDate.hashCode());
        return result;
    }

}
