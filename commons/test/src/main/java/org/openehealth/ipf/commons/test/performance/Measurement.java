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
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Mitko Kolev
 */
@XmlRootElement(name = "measurement", namespace = "http://www.openehealth.org/ipf/commons/test/performance/types/1.0")
@XmlAccessorType(XmlAccessType.FIELD)
public class Measurement implements Serializable {

    private static final long serialVersionUID = 5300482522175786091L;

    private final static String EMPTY_NAME = "";

    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlElement(name = "timestamp", required = true)
    private Timestamp timestamp;

    public Measurement() {
        this(new Timestamp(System.currentTimeMillis(), TimeUnit.MILLISECONDS),
                EMPTY_NAME);
    }

    public Measurement(Timestamp timestamp) {
        this(timestamp, EMPTY_NAME);

    }

    /**
     * @param timestamp
     * @param name
     */
    public Measurement(Timestamp timestamp, String name) {
        super();
        notNull(name, "The name must not be null!");
        notNull(timestamp, "The timestamp must not be null!");

        this.name = name;
        this.timestamp = timestamp;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return
     */
    public boolean isNameEmpty() {
        return name.equals(EMPTY_NAME);
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
        Measurement other = (Measurement) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!timestamp.equals(other.timestamp)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }
}
