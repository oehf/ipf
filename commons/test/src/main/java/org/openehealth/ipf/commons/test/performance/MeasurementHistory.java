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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Mitko Kolev
 */
public class MeasurementHistory implements Serializable {

    private static final long serialVersionUID = -5551291616837750950L;

    private final List<Measurement> measurements;

    private final Date referenceDate;

    public MeasurementHistory(Date referenceDate) {
        notNull(referenceDate, "The referenceDate must not be null!");
        this.measurements = new ArrayList<Measurement>();
        this.referenceDate = referenceDate;

    }
    
    public MeasurementHistory(MeasurementHistory copy){
        notNull(copy, "The copy must not be null!");
        this.measurements = new ArrayList<Measurement>(copy.measurements);
        this.referenceDate = new Date(copy.referenceDate.getTime());
    }

    /**
     * @return the measurements
     */
    public List<Measurement> getMeasurements() {
        return Collections.unmodifiableList(measurements);
    }

    /**
     * @return the referenceDate
     */
    public Date getReferenceDate() {
        return referenceDate;
    }
    
    public void add(Measurement measurement){
        notNull(measurement, "The measurement must not be null!");
        measurements.add(measurement);
    }
    

}
