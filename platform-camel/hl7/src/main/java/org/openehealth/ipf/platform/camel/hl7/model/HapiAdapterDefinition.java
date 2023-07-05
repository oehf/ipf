/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.hl7.model;

import org.apache.camel.spi.Metadata;
import org.openehealth.ipf.platform.camel.core.model.ProcessorAdapterDefinition;
import org.openehealth.ipf.platform.camel.hl7.adapter.HapiAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 */
@Metadata(label = "ipf,hl7,eip,transformation")
@XmlRootElement(name = "ack")
@XmlAccessorType(XmlAccessType.FIELD)
public class HapiAdapterDefinition extends ProcessorAdapterDefinition {

    @XmlTransient
    private HapiAdapter adapter;

    public HapiAdapterDefinition() {
    }

    public HapiAdapterDefinition(HapiAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public String toString() {
        return "HapiAdapter[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "hapiAdapter";
    }

    public HapiAdapter getAdapter() {
        return adapter;
    }
}
