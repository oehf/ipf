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
package org.openehealth.ipf.commons.ihe.xds.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a recipient containing a person and/or organization.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * <p>
 * If both, the person and the organization, are defined, the person is 
 * expected to be a member of the specified organization.
 * @author Jens Riemschneider
 */
public class Recipient implements Serializable {
    private static final long serialVersionUID = -8192511869759795939L;
    
    private Person person;
    private Organization organization;

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
     */
    public Recipient(Organization organization, Person person) {
        this.organization = organization;
        this.person = person;
    }

    /**
     * @return the person.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person
     *          the person.
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @return the organization.
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @param organization
     *          the organization.
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((organization == null) ? 0 : organization.hashCode());
        result = prime * result + ((person == null) ? 0 : person.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recipient other = (Recipient) obj;
        if (organization == null) {
            if (other.organization != null)
                return false;
        } else if (!organization.equals(other.organization))
            return false;
        if (person == null) {
            if (other.person != null)
                return false;
        } else if (!person.equals(other.person))
            return false;
        return true;
    }    

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }    
}
