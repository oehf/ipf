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

/**
 * XDS enumeration with different values for ebXML 2.1 and ebXML 3.0.
 * @author Dmytro Rud
 */
@EqualsAndHashCode(callSuper = true)
abstract public class XdsVersionedEnum extends XdsEnum {
    private static final long serialVersionUID = 3692466404503839164L;

    /** ebXML 2.1 string representation of the code */
    @Getter private final String ebXML21;

    /**
     * @param type    type of the code.  Shall be {@link Type#OFFICIAL) for static instances
     *                and either {@link Type#USER_DEFINED_VALUE) or {@link Type#INVALID)
     *                for values generated dynamically in the factories.
     * @param ebXML21 ebXML 2.1 representation of the code.
     * @param ebXML30 ebXML 3.0 representation of the code.
     */
    protected XdsVersionedEnum(Type type, String ebXML21, String ebXML30) {
        super(type, ebXML30);
        this.ebXML21 = ebXML21;
    }

}
