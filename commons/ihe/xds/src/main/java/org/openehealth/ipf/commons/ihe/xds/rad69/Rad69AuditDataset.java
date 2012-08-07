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
package org.openehealth.ipf.commons.ihe.xds.rad69;

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;


/**
 * RAD-69 specific Audit Dataset.
 * @author Clay Sebourn
 */
public class Rad69AuditDataset extends XdsAuditDataset {
    private static final long serialVersionUID = -8776033207572005899L;

    private String[] studyInstanceUuids;
    private String[] seriesInstanceUuids;
    private String[] documentUuids;
    private String[] repositoryUuids;
    private String[] homeCommunityUuids;

    public Rad69AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public String[] getDocumentUuids() {
        return documentUuids;
    }

    public void setDocumentUuids(String[] documentUuids) {
        this.documentUuids = documentUuids;
    }

    public String[] getHomeCommunityUuids() {
        return homeCommunityUuids;
    }

    public void setHomeCommunityUuids(String[] homeCommunityUuids) {
        this.homeCommunityUuids = homeCommunityUuids;
    }

    public String[] getRepositoryUuids() {
        return repositoryUuids;
    }

    public void setRepositoryUuids(String[] repositoryUuids) {
        this.repositoryUuids = repositoryUuids;
    }

    public String[] getSeriesInstanceUuids() {
        return seriesInstanceUuids;
    }

    public void setSeriesInstanceUuids(String[] seriesInstanceUuids) {
        this.seriesInstanceUuids = seriesInstanceUuids;
    }

    public String[] getStudyInstanceUuids() {
        return studyInstanceUuids;
    }

    public void setStudyInstanceUuids(String[] studyInstanceUuids) {
        this.studyInstanceUuids = studyInstanceUuids;
    }

}
