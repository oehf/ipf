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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a recipient containing a person and/or organization.
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
     * @deprecated please use {@link #Recipient(Organization, Person, Telecom)} instead.
     */
    @Deprecated
    public Recipient(Organization organization, Person person) {
        this(organization, person, null);
    }

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }    
}
