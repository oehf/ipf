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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Mitko Kolev
 */
public class MeasurementHistory implements Serializable {

    private static final long serialVersionUID = -5551291616837750950L;

    private List<Measurement> measurements;

    private Date referenceDate;

    public MeasurementHistory(Date referenceDate) {
        notNull(referenceDate, "The referenceDate must not be null!");
        this.measurements = new ArrayList<Measurement>();
        this.referenceDate = referenceDate;

    }

    public MeasurementHistory(MeasurementHistory copy) {
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeLong(referenceDate.getTime());
        writeMeasurements(out);
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        this.referenceDate = new Date(in.readLong());
        this.measurements = readMeasurements(in);
    }

    private void writeMeasurements(ObjectOutputStream out) throws IOException {
        String[] names = new String[measurements.size()];
        long[] times = new long[measurements.size()];
        for (int t = 0; t < measurements.size(); t++) {
            names[t] = measurements.get(t).getName();
            times[t] = measurements.get(t).getTimestamp().getNanoValue();
        }
        out.writeObject(names);
        out.writeObject(times);
    }

    /**
     * The method exists to provide deserialization for the Measurement and
     * ArrayList classes. These classes can not be deserialized in Groovy. For
     * more detailed description of the problem check
     * http://jira.codehaus.org/browse/GROOVY-1627
     * 
     * @param in
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<Measurement> readMeasurements(ObjectInputStream in)
            throws IOException, ClassNotFoundException {

        String[] names = (String[]) in.readObject();
        long[] nanoTimes = (long[]) in.readObject();
        List<Measurement> measurements = new ArrayList<Measurement>();
        if (names == null || names.length == 0) {
            return measurements;
        }
        for (int t = 0; t < names.length; t++) {
            Measurement m = new Measurement(new Timestamp(nanoTimes[t],
                    TimeUnit.NANOSECONDS), names[t]);
            measurements.add(m);
        }
        return measurements;
    }

}
