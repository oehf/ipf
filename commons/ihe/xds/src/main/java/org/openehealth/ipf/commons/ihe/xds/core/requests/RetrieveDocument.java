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

import javax.xml.bind.annotation.*;
import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Contains a request for a single document.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveDocument", propOrder = {"homeCommunityId", "repositoryUniqueId", "documentUniqueId" })
@XmlRootElement(name = "retrieveDocument")
public class RetrieveDocument implements Serializable {
    private static final long serialVersionUID = 7147966094676034661L;
    
    private String repositoryUniqueId;
    private String documentUniqueId;
    private String homeCommunityId;
   
    /**
     * Constructs the request.
     */
    public RetrieveDocument() {}
    
    /**
     * Constructs the request.
     * @param repositoryUniqueId
     *          the unique ID of the repository containing the document.
     * @param documentUniqueId
     *          the unique ID of the document.
     * @param homeCommunityId
     *          the home community ID.
     */
    public RetrieveDocument(String repositoryUniqueId, String documentUniqueId, String homeCommunityId) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentUniqueId == null) ? 0 : documentUniqueId.hashCode());
        result = prime * result + ((homeCommunityId == null) ? 0 : homeCommunityId.hashCode());
        result = prime * result
                + ((repositoryUniqueId == null) ? 0 : repositoryUniqueId.hashCode());
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
        RetrieveDocument other = (RetrieveDocument) obj;
        if (documentUniqueId == null) {
            if (other.documentUniqueId != null)
                return false;
        } else if (!documentUniqueId.equals(other.documentUniqueId))
            return false;
        if (homeCommunityId == null) {
            if (other.homeCommunityId != null)
                return false;
        } else if (!homeCommunityId.equals(other.homeCommunityId))
            return false;
        if (repositoryUniqueId == null) {
            if (other.repositoryUniqueId != null)
                return false;
        } else if (!repositoryUniqueId.equals(other.repositoryUniqueId))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
