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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.requests;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Contains a request for a single document.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public class RetrieveDocument {
    private String repositoryUniqueID;
    private String documentUniqueID;
    private String homeCommunityID;
   
    /**
     * Constructs the request.
     */
    public RetrieveDocument() {}
    
    /**
     * Constructs the request.
     * @param repositoryUniqueID
     *          the unique ID of the repository containing the document.
     * @param documentUniqueID
     *          the unique ID of the document.
     * @param homeCommunityID
     *          the home community ID.
     */
    public RetrieveDocument(String repositoryUniqueID, String documentUniqueID, String homeCommunityID) {
        this.repositoryUniqueID = repositoryUniqueID;
        this.documentUniqueID = documentUniqueID;
        this.homeCommunityID = homeCommunityID;
    }

    /**
     * @return the unique ID of the repository containing the document.
     */
    public String getRepositoryUniqueID() {
        return repositoryUniqueID;
    }
    
    /**
     * @param repositoryUniqueID
     *          the unique ID of the repository containing the document.
     */
    public void setRepositoryUniqueID(String repositoryUniqueID) {
        this.repositoryUniqueID = repositoryUniqueID;
    }
    
    /**
     * @return the unique ID of the document.
     */
    public String getDocumentUniqueID() {
        return documentUniqueID;
    }
    
    /**
     * @param documentUniqueID
     *          the unique ID of the document.
     */
    public void setDocumentUniqueID(String documentUniqueID) {
        this.documentUniqueID = documentUniqueID;
    }
    
    /**
     * @return the home community ID.
     */
    public String getHomeCommunityID() {
        return homeCommunityID;
    }

    /**
     * @param homeCommunityID
     *          the home community ID.
     */
    public void setHomeCommunityID(String homeCommunityID) {
        this.homeCommunityID = homeCommunityID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((documentUniqueID == null) ? 0 : documentUniqueID.hashCode());
        result = prime * result + ((homeCommunityID == null) ? 0 : homeCommunityID.hashCode());
        result = prime * result
                + ((repositoryUniqueID == null) ? 0 : repositoryUniqueID.hashCode());
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
        if (documentUniqueID == null) {
            if (other.documentUniqueID != null)
                return false;
        } else if (!documentUniqueID.equals(other.documentUniqueID))
            return false;
        if (homeCommunityID == null) {
            if (other.homeCommunityID != null)
                return false;
        } else if (!homeCommunityID.equals(other.homeCommunityID))
            return false;
        if (repositoryUniqueID == null) {
            if (other.repositoryUniqueID != null)
                return false;
        } else if (!repositoryUniqueID.equals(other.repositoryUniqueID))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
