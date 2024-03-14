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

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.*;


/**
 * <p>Java class for DsmlModification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DsmlModification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="value" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlValue" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}AttributeDescriptionValue" />
 *       &lt;attribute name="operation" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="add"/>
 *             &lt;enumeration value="delete"/>
 *             &lt;enumeration value="replace"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DsmlModification", propOrder = {
    "value"
})
public class DsmlModification {

    @XmlElement(type = String.class)
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> value;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "operation", required = true)
    protected ModificationOperationType operation;

    /**
     * Gets the value of the value property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the value property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<Object> getValue() {
        if (value == null) {
            value = new ArrayList<>();
        }
        return this.value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link ModificationOperationType }
     *     
     */
    public ModificationOperationType getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModificationOperationType }
     *     
     */
    public void setOperation(ModificationOperationType value) {
        this.operation = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="add"/>
     *     &lt;enumeration value="delete"/>
     *     &lt;enumeration value="replace"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum ModificationOperationType {

        @XmlEnumValue("add")
        ADD("add"),
        @XmlEnumValue("delete")
        DELETE("delete"),
        @XmlEnumValue("replace")
        REPLACE("replace");
        private final String value;

        ModificationOperationType(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ModificationOperationType fromValue(String v) {
            for (var c: ModificationOperationType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
