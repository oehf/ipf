/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.apache.commons.lang3.StringUtils;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * XDS audit dataset specific for transactions related to document retrieval.
 * @author Dmytro Rud
 */
public class XdsRetrieveAuditDataset extends XdsAuditDataset {
    private static final long serialVersionUID = -8776033207572005899L;

    private final List<String[]> deliveredDocuments    = new ArrayList<String[]>();
    private final List<String[]> notDeliveredDocuments = new ArrayList<String[]>();


    public enum Status {DELIVERED, NOT_DELIVERED}


    public XdsRetrieveAuditDataset(boolean serverSide) {
        super(serverSide);
    }


    public boolean hasDocuments(Status status) {
        return ! getDocumentList(status).isEmpty();
    }


    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Status status) {
        return (status == Status.DELIVERED)
                ? RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS
                : RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE;
    }


    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode() {
        throw new RuntimeException("Please call #getEventOutcomeCode(Status status) instead");
    }


    public void registerRequestedDocument(String documentUniqueId, String repositoryUniqueId, String homeCommunityId, String studyInstanceUID, String seriesInstanceUID) {
        String[] metadata = new String[] {documentUniqueId, repositoryUniqueId, homeCommunityId, studyInstanceUID, seriesInstanceUID};
        notDeliveredDocuments.add(metadata);
    }

    
    public void registerDeliveredDocument(String documentUniqueId, String repositoryUniqueId, String homeCommunityId) {
        Iterator<String[]> iterator = notDeliveredDocuments.iterator();
        while (iterator.hasNext()) {
            String[] metadata = iterator.next();
            if (StringUtils.equals(metadata[0], documentUniqueId) &&
                StringUtils.equals(metadata[1], repositoryUniqueId) &&
                StringUtils.equals(metadata[2], homeCommunityId))
            {
                deliveredDocuments.add(metadata);
                iterator.remove();
            }
        }
    }


    private List<String[]> getDocumentList(Status status) {
        return (status == Status.DELIVERED) ? deliveredDocuments : notDeliveredDocuments;
    }


    private String[] array(Status status, int index) {
        List<String[]> list = getDocumentList(status);
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            result[i] = list.get(i)[index];
        }
        return result;
    }


    public String[] getDocumentIds      (Status status) { return array(status, 0); }
    public String[] getRepositoryIds    (Status status) { return array(status, 1); }
    public String[] getHomeCommunityIds (Status status) { return array(status, 2); }
    public String[] getStudyInstanceIds (Status status) { return array(status, 3); }
    public String[] getSeriesInstanceIds(Status status) { return array(status, 4); }
}
