/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.primitive.CommonTS;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.DateTimeAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * HL7 timestamps (data type DTM) with particular precision, normalized to UTC.
 *
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Timestamp", propOrder = {"dateTime", "precision"})
public class Timestamp implements Serializable {
    private static final long serialVersionUID = 4324651691599629794L;

    public enum Precision {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND
    }

    private static final Map<Precision, DateTimeFormatter> FORMATTERS = new EnumMap<>(Precision.class);
    static {
        FORMATTERS.put(Precision.YEAR,   DateTimeFormat.forPattern("yyyy"));
        FORMATTERS.put(Precision.MONTH,  DateTimeFormat.forPattern("yyyyMM"));
        FORMATTERS.put(Precision.DAY,    DateTimeFormat.forPattern("yyyyMMdd"));
        FORMATTERS.put(Precision.HOUR,   DateTimeFormat.forPattern("yyyyMMddHH"));
        FORMATTERS.put(Precision.MINUTE, DateTimeFormat.forPattern("yyyyMMddHHmm"));
        FORMATTERS.put(Precision.SECOND, DateTimeFormat.forPattern("yyyyMMddHHmmss"));
    }

    /**
     * Timestamp.
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(value = DateTimeAdapter.class)
    @Getter private DateTime dateTime;

    /**
     * Precision of the timestamp (smallest present element, e.g. YEAR for "1980").
     */
    @XmlAttribute
    @Setter private Precision precision;

    public Timestamp() {
        // only for JAXB
    }

    /**
     * Initializes a {@link Timestamp} object with the given datetime and precision.
     */
    public Timestamp(DateTime dateTime, Precision precision) {
        setDateTime(dateTime);
        setPrecision(precision);
    }

    /**
     * Creates a {@link Timestamp} object from the given string.
     * @param s
     *      String of the pattern <code>YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]</code>.
     *      Milliseconds will be ignored.
     * @return
     *      a {@link Timestamp} object, or <code>null</code> if the parameter is <code>null</code> or empty.
     */
    public static Timestamp fromHL7(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        int pos = Math.max(s.indexOf('-'), s.indexOf('+'));
        int len = (pos >= 0) ? pos : s.length();

        // determine precision
        Precision precision;
        if (len >= 14) {
            precision = Precision.SECOND;
        } else if (len >= 12) {
            precision = Precision.MINUTE;
        } else if (len >= 10) {
            precision = Precision.HOUR;
        } else if (len >= 8) {
            precision = Precision.DAY;
        } else if (len >= 6) {
            precision = Precision.MONTH;
        } else {
            precision = Precision.YEAR;
        }

        // default time zone is UTC
        if (pos < 0) {
            s += "+0000";
        }

        // parse timestamp
        try {
            CommonTS ts = new CommonTS(s);
            DateTime dateTime = new DateTime(
                    ts.getYear(),
                    (ts.getMonth() == 0) ? 1 : ts.getMonth(),
                    (ts.getDay() == 0) ? 1 : ts.getDay(),
                    ts.getHour(),
                    ts.getMinute(),
                    ts.getSecond(),
                    DateTimeZone.forOffsetHoursMinutes(
                            ts.getGMTOffset() / 100,
                            ts.getGMTOffset() % 100));
            return new Timestamp(dateTime.toDateTime(DateTimeZone.UTC), precision);
        } catch (DataTypeException e) {
            throw new XDSMetaDataException(ValidationMessage.INVALID_TIME, s);
        }
    }

    /**
     * Returns a HL7 representation of the given timestamp, considering the precision.
     * @param timestamp
     *      {@link Timestamp} object, can be <code>null</code>.
     * @return
     *      HL7 representation of the timestamp, or <code>null</code> if the parameter is <code>null</code>.
     */
    public static String toHL7(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toHL7() : null;
    }

    private String toHL7() {
        return FORMATTERS.get(getPrecision()).print(getDateTime().toDateTime(DateTimeZone.UTC));
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = (dateTime != null) ? dateTime.toDateTime(DateTimeZone.UTC) : null;
    }

    public Precision getPrecision() {
        return (precision != null) ? precision : Precision.SECOND;
    }


    /**
     * Two HL7 timestamps are equal when they have the same values in the relevant fields
     * (i.e. in the ones covered by the precision).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timestamp timestamp = (Timestamp) o;
        return StringUtils.equals(toHL7(this), toHL7(timestamp));
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, precision);
    }

    @Override
    public String toString() {
        return "Timestamp(" +
                "dateTime=" + dateTime +
                ", precision=" + precision +
                ')';
    }
}
