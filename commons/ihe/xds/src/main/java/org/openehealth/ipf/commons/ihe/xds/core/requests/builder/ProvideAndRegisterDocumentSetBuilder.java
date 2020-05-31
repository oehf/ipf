/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests.builder;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;

import java.util.List;

/**
 * Builder for {@link ProvideAndRegisterDocumentSet} objects.
 */
public class ProvideAndRegisterDocumentSetBuilder
        extends AbstractSubmissionRequestBuilder<ProvideAndRegisterDocumentSetBuilder, ProvideAndRegisterDocumentSet, Document> {
    private String targetHomeCommunityId;
    
    public ProvideAndRegisterDocumentSetBuilder(boolean autoGenerate, SubmissionSet submissionSet) {
        super(autoGenerate, submissionSet, Document::getDocumentEntry);
    }
    
    public ProvideAndRegisterDocumentSetBuilder withTargetHomeCommunityId(String targetHomeCommunityId) {
        this.targetHomeCommunityId = targetHomeCommunityId;
        return this;
    }

    @Override
    public ProvideAndRegisterDocumentSet doBuild(SubmissionSet submissionSet, List<Folder> folders, List<Document> documents, List<Association> associations) {
        ProvideAndRegisterDocumentSet pnrSet = new ProvideAndRegisterDocumentSet();
        pnrSet.setSubmissionSet(submissionSet);
        pnrSet.getFolders().addAll(folders);
        pnrSet.getDocuments().addAll(documents);
        pnrSet.getAssociations().addAll(associations);
        pnrSet.setTargetHomeCommunityId(targetHomeCommunityId);
        return pnrSet;
    }
}