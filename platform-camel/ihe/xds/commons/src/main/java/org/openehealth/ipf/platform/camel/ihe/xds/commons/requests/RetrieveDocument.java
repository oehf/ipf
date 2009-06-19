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

/**
 * Contains a request for a single document.
 * @author Jens Riemschneider
 */
public class RetrieveDocument {
    private String repositoryUniqueID;
    private String documentUniqueID;
    private String homeCommunityID;
    
    public String getRepositoryUniqueID() {
        return repositoryUniqueID;
    }
    
    public void setRepositoryUniqueID(String repositoryUniqueID) {
        this.repositoryUniqueID = repositoryUniqueID;
    }
    
    public String getDocumentUniqueID() {
        return documentUniqueID;
    }
    
    public void setDocumentUniqueID(String documentUniqueID) {
        this.documentUniqueID = documentUniqueID;
    }
    
    public String getHomeCommunityID() {
        return homeCommunityID;
    }

    public void setHomeCommunityID(String homeCommunityID) {
        this.homeCommunityID = homeCommunityID;
    }
}
