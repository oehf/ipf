/*
 * Copyright 2016 the original author or authors.
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * XDS enumeration for cases when same values are used in both ebXML 2.1 and ebXML 3.0.
 *
 * @author Dmytro Rud
 */
@EqualsAndHashCode
abstract public class XdsEnum implements Serializable {
    private static final long serialVersionUID = 6970017039702190232L;

    @XmlType(name = "EnumValueType")
    @XmlEnum(String.class)
    public enum Type {
        /**
         * Official code defined by IHE or other authority.
         */
        OFFICIAL,
        /**
         * Non-standard, user-defined code (whenever allowed and possible).
         */
        USER_DEFINED,
        /**
         * Invalid code.
         */
        INVALID
    }


    /**
     * ebXML 3.0 string representation of the code
     */
    @Getter
    private final String ebXML30;

    @Getter
    private final Type type;

    /**
     * @return value to be used in XML of the simplified IPF XDS data model.
     */
    abstract public String getJaxbValue();


    /**
     * Constructor for cases when ebXML 2.1 and 3.0 use equal code representations.
     *
     * @param type  type of the code.  Shall be {@link Type#OFFICIAL) for static instances
     *              and either {@link Type#USER_DEFINED_VALUE) or {@link Type#INVALID)
     *              for values generated dynamically in the factories.
     * @param ebXML30 ebXML 3.0 representation of the code.
     */
    protected XdsEnum(Type type, String ebXML30) {
        this.type = type;
        this.ebXML30 = ebXML30;
    }

}
