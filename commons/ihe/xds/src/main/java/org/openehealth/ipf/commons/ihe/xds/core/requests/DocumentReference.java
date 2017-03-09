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
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Contains a reference to a single document in an XDS repository.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentReference", propOrder = {"homeCommunityId", "repositoryUniqueId", "documentUniqueId" })
@XmlRootElement(name = "retrieveDocument")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class DocumentReference implements Serializable {
    private static final long serialVersionUID = 7147966094676034661L;
    
    private String repositoryUniqueId;
    private String documentUniqueId;
    private String homeCommunityId;
   
    /**
     * Constructs the document reference.
     */
    public DocumentReference() {}
    
    /**
     * Constructs the document reference.
     * @param repositoryUniqueId
     *          the unique ID of the repository containing the document.
     * @param documentUniqueId
     *          the unique ID of the document.
     * @param homeCommunityId
     *          the home community ID.
     */
    public DocumentReference(String repositoryUniqueId, String documentUniqueId, String homeCommunityId) {
        this.repositoryUniqueId = repositoryUniqueId;
        this.documentUniqueId = documentUniqueId;
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * @return the unique ID of the repository containing the document.
     */
    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }
    
    /**
     * @param repositoryUniqueId
     *          the unique ID of the repository containing the document.
     */
    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }
    
    /**
     * @return the unique ID of the document.
     */
    public String getDocumentUniqueId() {
        return documentUniqueId;
    }
    
    /**
     * @param documentUniqueId
     *          the unique ID of the document.
     */
    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }
    
    /**
     * @return the home community ID.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * @param homeCommunityId
     *          the home community ID.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

}
