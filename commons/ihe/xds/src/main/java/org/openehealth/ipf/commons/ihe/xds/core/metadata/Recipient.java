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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Represents a recipient containing a person and/or organization
 * and/or telecommunication address.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * <p>
 * If both, the person and the organization, are defined, the person is 
 * expected to be a member of the specified organization.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Recipient", propOrder = {"person", "organization", "telecom"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Recipient implements Serializable {
    private static final long serialVersionUID = -8192511869759795939L;

    @Getter @Setter private Person person;
    @Getter @Setter private Organization organization;
    @Getter @Setter private Telecom telecom;

    /**
     * Constructs a recipient.
     */
    public Recipient() {}

    /**
     * Constructs a recipient.
     * @param organization
     *          the organization.
     * @param person
     *          the person.
     * @param telecom
     *          telecommunication address.
     */
    public Recipient(Organization organization, Person person, Telecom telecom) {
        this.organization = organization;
        this.person = person;
        this.telecom = telecom;
    }

}
