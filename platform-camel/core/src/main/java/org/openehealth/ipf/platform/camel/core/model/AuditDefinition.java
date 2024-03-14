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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spi.Metadata;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip")
@XmlRootElement(name = "audit")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuditDefinition extends DelegateDefinition {

    @XmlTransient
    private Processor auditProcessor;
    @XmlAttribute
    private String auditProcessorBeanName;
    
    public AuditDefinition() {
        auditProcessor = new Noop();
    }
    
    /**
     * @param auditProcessor the auditProcessor to set
     */
    public void setAuditProcessor(Processor auditProcessor) {
        this.auditProcessor = auditProcessor;
    }

    /**
     * @param auditProcessorBeanName the auditProcessorBeanName to set
     */
    public void setAuditProcessorBeanName(String auditProcessorBeanName) {
        this.auditProcessorBeanName = auditProcessorBeanName;
    }


    public Processor getAuditProcessor() {
        return auditProcessor;
    }

    public String getAuditProcessorBeanName() {
        return auditProcessorBeanName;
    }

    private static class Noop implements Processor {

        @Override
        public void process(Exchange exchange) {
        }
        
    }
    
}
