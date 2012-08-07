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

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import java.util.List;

/**
 * Audit strategy for RAD-69.
 * @author Clay Sebourn
 */
abstract public class Rad69AuditStrategy extends XdsAuditStrategy<Rad69AuditDataset> {

    public Rad69AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }

    @Override
    public void enrichDatasetFromRequest(Object pojo, Rad69AuditDataset auditDataset) {
        RetrieveImagingDocumentSetRequestType request = (RetrieveImagingDocumentSetRequestType) pojo;

        // Determine the number of document requests
        int SIZE = 0;
        List<RetrieveImagingDocumentSetRequestType.StudyRequest> requestedStudies = request.getStudyRequest();
        if (requestedStudies != null)
        {
            for (RetrieveImagingDocumentSetRequestType.StudyRequest studyRequest : requestedStudies)
            {
                List<RetrieveImagingDocumentSetRequestType.SeriesRequest> requestedSeries = studyRequest.getSeriesRequest();
                if (requestedSeries != null)
                {
                    for (RetrieveImagingDocumentSetRequestType.SeriesRequest seriesRequest : requestedSeries)
                    {
                        List<RetrieveDocumentSetRequestType.DocumentRequest> requestedDocuments = seriesRequest.getDocumentRequests();
                        if (requestedDocuments != null)
                        {
                            SIZE = SIZE + requestedDocuments.size();
                        }
                    }
                }
            }

            if (SIZE > 0)
            {
                // Allocate arrays
                String[] studyInstanceUuids = new String[SIZE];
                String[] seriesInstanceUuids = new String[SIZE];
                String[] documentUuids = new String[SIZE];
                String[] repositoryUuids = new String[SIZE];
                String[] homeCommunityUuids = new String[SIZE];

                // Get the audit data
                int auditDatasetIndex = 0;

                for (RetrieveImagingDocumentSetRequestType.StudyRequest studyRequest : requestedStudies)
                {
                    List<RetrieveImagingDocumentSetRequestType.SeriesRequest> requestedSeries = studyRequest.getSeriesRequest();
                    for (RetrieveImagingDocumentSetRequestType.SeriesRequest seriesRequest : requestedSeries)
                    {
                        List<RetrieveDocumentSetRequestType.DocumentRequest> requestedDocuments = seriesRequest.getDocumentRequests();
                        for (RetrieveDocumentSetRequestType.DocumentRequest document : requestedDocuments)
                        {
                            studyInstanceUuids[auditDatasetIndex] = studyRequest.getStudyInstanceUID();
                            seriesInstanceUuids[auditDatasetIndex] = seriesRequest.getSeriesInstanceUID();
                            documentUuids[auditDatasetIndex] = document.getDocumentUniqueId();
                            repositoryUuids[auditDatasetIndex] = document.getRepositoryUniqueId();
                            homeCommunityUuids[auditDatasetIndex] = document.getHomeCommunityId();
                            auditDatasetIndex++;
                        }
                    }
                }

                // Update the audit dataset
                auditDataset.setStudyInstanceUuids(studyInstanceUuids);
                auditDataset.setSeriesInstanceUuids(seriesInstanceUuids);
                auditDataset.setDocumentUuids(documentUuids);
                auditDataset.setRepositoryUuids(repositoryUuids);
                auditDataset.setHomeCommunityUuids(homeCommunityUuids);
            }
        }
    }

    @Override
    public Rad69AuditDataset createAuditDataset() {
        return new Rad69AuditDataset(isServerSide());
    }

    @Override
    public RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response.getRegistryResponse()); 
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }
}
