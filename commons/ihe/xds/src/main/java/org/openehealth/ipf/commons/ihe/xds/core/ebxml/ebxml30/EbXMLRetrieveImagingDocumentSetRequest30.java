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
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType.SeriesRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.xdsi.RetrieveImagingDocumentSetRequestType.StudyRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveSeries;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveStudy;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * The ebXML 3.0 version of the {@link EbXMLRetrieveImagingDocumentSetRequest}.
 *
 * @author Clay Sebourn
 */
public class EbXMLRetrieveImagingDocumentSetRequest30 implements EbXMLRetrieveImagingDocumentSetRequest<RetrieveImagingDocumentSetRequestType> {
    private final RetrieveImagingDocumentSetRequestType request;

    /**
     * Constructs a request by wrapping the given ebXML 3.0 object.
     * @param request   the object to wrap.
     */
    public EbXMLRetrieveImagingDocumentSetRequest30(RetrieveImagingDocumentSetRequestType request) {
        this.request = requireNonNull(request, "request cannot be null");
    }

    @Override
    public RetrieveImagingDocumentSetRequestType getInternal() {
        return request;
    }

    @Override
    public List<RetrieveStudy> getRetrieveStudies() {
        var retrieveStudies = new ArrayList<RetrieveStudy>();
        for (var studyRequest : request.getStudyRequest())
        {
            var retrieveStudy = new RetrieveStudy();
            retrieveStudy.setStudyInstanceUID(studyRequest.getStudyInstanceUID());
            for (var seriesRequest : studyRequest.getSeriesRequest())
            {
                var retrieveSeries = new RetrieveSeries();
                retrieveSeries.setSeriesInstanceUID(seriesRequest.getSeriesInstanceUID());
                for (var documentRequest : seriesRequest.getDocumentRequests())
                {
                    var documentReference = new DocumentReference();
                    documentReference.setDocumentUniqueId(documentRequest.getDocumentUniqueId());
                    documentReference.setHomeCommunityId(documentRequest.getHomeCommunityId());
                    documentReference.setRepositoryUniqueId(documentRequest.getRepositoryUniqueId());
                    retrieveSeries.getDocuments().add(documentReference);
                }
                retrieveStudy.getRetrieveSerieses().add(retrieveSeries);
            }
            retrieveStudies.add(retrieveStudy);
        }
        return retrieveStudies;
    }

    @Override
    public List<String> getTransferSyntaxUIDList() {
        return new ArrayList<>(request.getTransferSyntaxUIDList().getTransferSyntaxUID());
    }

    @Override
    public void setRetrieveStudies(List<RetrieveStudy> retrieveStudies) {
        request.getStudyRequest().clear();
        if (retrieveStudies != null) {
            for (var retrieveStudy : retrieveStudies)
            {
                var studyRequest = new StudyRequest();
                studyRequest.setStudyInstanceUID(retrieveStudy.getStudyInstanceUID());
                for (var retrieveSeries : retrieveStudy.getRetrieveSerieses())
                {
                    var seriesRequest = new SeriesRequest();
                    seriesRequest.setSeriesInstanceUID(retrieveSeries.getSeriesInstanceUID());
                    for (var documentReference : retrieveSeries.getDocuments())
                    {
                        var documentRequest = new DocumentRequest();
                        documentRequest.setDocumentUniqueId(documentReference.getDocumentUniqueId());
                        documentRequest.setHomeCommunityId(documentReference.getHomeCommunityId());
                        documentRequest.setRepositoryUniqueId(documentReference.getRepositoryUniqueId());
                        seriesRequest.getDocumentRequests().add(documentRequest);
                    }
                    studyRequest.getSeriesRequest().add(seriesRequest);
                }
                request.getStudyRequest().add(studyRequest);
            }
        }
    }

    @Override
    public void setTransferSyntaxUIDList(List<String> transferSyntaxUIDList) {
        request.getTransferSyntaxUIDList().getTransferSyntaxUID().clear();
        if (transferSyntaxUIDList != null) {
            request.getTransferSyntaxUIDList().getTransferSyntaxUID().addAll(transferSyntaxUIDList);
        }
    }
}
