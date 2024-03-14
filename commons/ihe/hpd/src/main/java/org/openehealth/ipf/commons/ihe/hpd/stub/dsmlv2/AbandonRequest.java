/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AbandonRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbandonRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage">
 *       &lt;attribute name="abandonID" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}RequestID" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbandonRequest")
public class AbandonRequest
    extends DsmlMessage
{

    @XmlAttribute(name = "abandonID", required = true)
    protected String abandonID;

    /**
     * Gets the value of the abandonID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbandonID() {
        return abandonID;
    }

    /**
     * Sets the value of the abandonID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbandonID(String value) {
        this.abandonID = value;
    }

}
