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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in ebXML 3.0-based retrieval-related XDS-I transactions.
 *
 * @author Clay Sebourn
 */
abstract public class XdsIRetrieveAuditStrategy extends XdsAuditStrategy<XdsIRetrieveAuditDataset> {

    public XdsIRetrieveAuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit);
    }


    @Override
    public void enrichDatasetFromRequest(Object pojo, XdsIRetrieveAuditDataset auditDataset) {
        RetrieveImagingDocumentSetRequestType request = (RetrieveImagingDocumentSetRequestType) pojo;

        List<String> studyInstanceUniqueIds  = new ArrayList<String>();
        List<String> seriesInstanceUniqueIds = new ArrayList<String>();
        List<String> documentUniqueIds       = new ArrayList<String>();
        List<String> repositoryUniqueIds     = new ArrayList<String>();
        List<String> homeCommunityIds        = new ArrayList<String>();

        List<RetrieveImagingDocumentSetRequestType.StudyRequest> requestedStudies = request.getStudyRequest();
        if (requestedStudies != null) {
            for (RetrieveImagingDocumentSetRequestType.StudyRequest studyRequest : requestedStudies) {
                List<RetrieveImagingDocumentSetRequestType.SeriesRequest> requestedSeries = studyRequest.getSeriesRequest();
                if (requestedSeries != null) {
                    for (RetrieveImagingDocumentSetRequestType.SeriesRequest seriesRequest : requestedSeries) {
                        List<RetrieveDocumentSetRequestType.DocumentRequest> requestedDocuments = seriesRequest.getDocumentRequests();
                        if (requestedDocuments != null) {
                            for (RetrieveDocumentSetRequestType.DocumentRequest document : requestedDocuments) {
                                studyInstanceUniqueIds.add(studyRequest.getStudyInstanceUID());
                                seriesInstanceUniqueIds.add(seriesRequest.getSeriesInstanceUID());
                                documentUniqueIds.add(document.getDocumentUniqueId());
                                repositoryUniqueIds.add(document.getRepositoryUniqueId());
                                homeCommunityIds.add(document.getHomeCommunityId());
                            }
                        }
                    }
                }
            }
        }

        int size = documentUniqueIds.size();
        if (size > 0) {
            auditDataset.setStudyInstanceUniqueIds(studyInstanceUniqueIds.toArray(new String[size]));
            auditDataset.setSeriesInstanceUniqueIds(seriesInstanceUniqueIds.toArray(new String[size]));
            auditDataset.setDocumentUniqueIds(documentUniqueIds.toArray(new String[size]));
            auditDataset.setRepositoryUniqueIds(repositoryUniqueIds.toArray(new String[size]));
            auditDataset.setHomeCommunityIds(homeCommunityIds.toArray(new String[size]));
        }
    }


    @Override
    public XdsIRetrieveAuditDataset createAuditDataset() {
        return new XdsIRetrieveAuditDataset(isServerSide());
    }


    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Object pojo) {
        RetrieveDocumentSetResponseType response = (RetrieveDocumentSetResponseType) pojo;
        EbXMLRegistryResponse ebXML = new EbXMLRegistryResponse30(response.getRegistryResponse());
        return getEventOutcomeCodeFromRegistryResponse(ebXML);
    }

}
