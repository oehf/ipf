/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.flow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Martin Krasser
 */
@Entity
@Table(name = "T_FLOW_NUMBER")
public class FlowNumber {
    
    public static final String DEFAULT_SEQUENCE = "default";
    
    @Id
    @Column(name="C_SEQUENCE")
    private String sequence;
    
    @Column(name="C_VALUE")
    private long value;

    public FlowNumber() {
        this(DEFAULT_SEQUENCE);
    }

    public FlowNumber(String sequence) {
        value = 0L;
        this.sequence = sequence;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
    
    public long incrementAndGet() {
        value++;
        return value; 
    }
    
}
