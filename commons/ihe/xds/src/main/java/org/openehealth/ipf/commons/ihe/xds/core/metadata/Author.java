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
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the human or machine that created an entry.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Author", propOrder = {"authorPerson", "authorInstitution",
        "authorSpecialty", "authorRole", "authorTelecom"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Author implements Serializable {
    private static final long serialVersionUID = 6731221295927724760L;
    
    private Person authorPerson;
    private final List<Organization> authorInstitution = new ArrayList<>();
    private final List<Identifiable> authorRole = new ArrayList<>();
    private final List<Identifiable> authorSpecialty = new ArrayList<>();
    private final List<Telecom> authorTelecom = new ArrayList<>();

    /**
     * @return basic information about the author.
     */
    public Person getAuthorPerson() {
        return authorPerson;
    }
    
    /**
     * @param authorPerson
     *          basic information about the author.
     */
    public void setAuthorPerson(Person authorPerson) {        
        this.authorPerson = authorPerson;
    }

    /**
     * @return the list of author institutions. Never <code>null</code>, but
     *          can be empty. 
     */
    public List<Organization> getAuthorInstitution() {
        return authorInstitution;
    }
    
    /**
     * @return the list of author roles. Never <code>null</code>, but
     *          can be empty. 
     */
    public List<Identifiable> getAuthorRole() {
        return authorRole;
    }
    
    /**
     * @return the list of author specialties. Never <code>null</code>, but
     *          can be empty. 
     */
    public List<Identifiable> getAuthorSpecialty() {
        return authorSpecialty;
    }

    /**
     * @return the list of author telecommunication addresses.
     *          Never <code>null</code>, but can be empty.
     */
    public List<Telecom> getAuthorTelecom() {
        return authorTelecom;
    }

}
