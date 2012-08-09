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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType.DocumentRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType.StudyRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType.SeriesRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveSeries;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveStudy;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * The ebXML 3.0 version of the {@link EbXMLRetrieveImagingDocumentSetRequest}.
 *
 * @author Clay Sebourn
 */
public class EbXMLRetrieveImagingDocumentSetRequest30 implements EbXMLRetrieveImagingDocumentSetRequest {
    private final RetrieveImagingDocumentSetRequestType request;

    /**
     * Constructs a request by wrapping the given ebXML 3.0 object.
     * @param request   the object to wrap.
     */
    public EbXMLRetrieveImagingDocumentSetRequest30(RetrieveImagingDocumentSetRequestType request) {
        notNull(request, "request cannot be null");
        this.request = request;
    }

    @Override
    public RetrieveImagingDocumentSetRequestType getInternal() {
        return request;
    }

    @Override
    public List<RetrieveStudy> getRetrieveStudies() {
        List<RetrieveStudy> retrieveStudies = new ArrayList<RetrieveStudy>();
        for (StudyRequest studyRequest : request.getStudyRequest())
        {
            RetrieveStudy retrieveStudy = new RetrieveStudy();
            retrieveStudy.setStudyInstanceUID(studyRequest.getStudyInstanceUID());
            for (SeriesRequest seriesRequest : studyRequest.getSeriesRequest())
            {
                RetrieveSeries retrieveSeries = new RetrieveSeries();
                retrieveSeries.setSeriesInstanceUID(seriesRequest.getSeriesInstanceUID());
                for (DocumentRequest documentRequest : seriesRequest.getDocumentRequests())
                {
                    RetrieveDocument retrieveDocument = new RetrieveDocument();
                    retrieveDocument.setDocumentUniqueId(documentRequest.getDocumentUniqueId());
                    retrieveDocument.setHomeCommunityId(documentRequest.getHomeCommunityId());
                    retrieveDocument.setRepositoryUniqueId(documentRequest.getRepositoryUniqueId());
                    retrieveSeries.getDocuments().add(retrieveDocument);
                }
                retrieveStudy.getRetrieveSerieses().add(retrieveSeries);
            }
            retrieveStudies.add(retrieveStudy);
        }
        return retrieveStudies;
    }

    @Override
    public void setRetrieveStudies(List<RetrieveStudy> retrieveStudies) {
        request.getStudyRequest().clear();
        if (retrieveStudies != null) {
            for (RetrieveStudy retrieveStudy : retrieveStudies)
            {
                StudyRequest studyRequest = new StudyRequest();
                studyRequest.setStudyInstanceUID(retrieveStudy.getStudyInstanceUID());
                for ( RetrieveSeries retrieveSeries : retrieveStudy.getRetrieveSerieses())
                {
                    SeriesRequest seriesRequest = new SeriesRequest();
                    seriesRequest.setSeriesInstanceUID(retrieveSeries.getSeriesInstanceUID());
                    for (RetrieveDocument retrieveDocument : retrieveSeries.getDocuments())
                    {
                        DocumentRequest documentRequest = new DocumentRequest();
                        documentRequest.setDocumentUniqueId(retrieveDocument.getDocumentUniqueId());
                        documentRequest.setHomeCommunityId(retrieveDocument.getHomeCommunityId());
                        documentRequest.setRepositoryUniqueId(retrieveDocument.getRepositoryUniqueId());
                        seriesRequest.getDocumentRequests().add(documentRequest);
                    }
                    studyRequest.getSeriesRequest().add(seriesRequest);
                }
                request.getStudyRequest().add(studyRequest);
            }
        }
    }
}
