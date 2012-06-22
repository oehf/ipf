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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.DateAdapter;

/**
 * Represents a date and time range used in queries.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeRange", propOrder = {"from", "to"})
public class TimeRange implements Serializable {
    private static final long serialVersionUID = -5468726370729209318L;
    
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(value = DateAdapter.class)
    private String from;
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(value = DateAdapter.class)
    private String to;
    
    /**
     * @return the start time of the range.
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * @param from
     *          the start time of the range.
     */
    public void setFrom(String from) {
        this.from = from;
    }
    
    /**
     * @return the end time of the range.
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to
     *          the end time of the range.
     */
    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeRange other = (TimeRange) obj;
        if (from == null) {
            if (other.from != null)
                return false;
        } else if (!from.equals(other.from))
            return false;
        if (to == null) {
            if (other.to != null)
                return false;
        } else if (!to.equals(other.to))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
