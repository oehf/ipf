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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;

import java.util.List;
import java.util.function.Function;

/**
 * Builder for {@link RegisterDocumentSet} objects.
 */
public class RegisterDocumentSetBuilder extends AbstractSubmissionRequestBuilder<RegisterDocumentSetBuilder, RegisterDocumentSet, DocumentEntry> {
    public RegisterDocumentSetBuilder(boolean autoGenerate, SubmissionSet submissionSet) {
        super(autoGenerate, submissionSet, Function.identity());
    }

    @Override
    public RegisterDocumentSet doBuild(SubmissionSet submissionSet, List<Folder> folders, List<DocumentEntry> documents, List<Association> associations) {
        RegisterDocumentSet set = new RegisterDocumentSet();
        set.setSubmissionSet(submissionSet);
        set.getFolders().addAll(folders);
        set.getDocumentEntries().addAll(documents);
        set.getAssociations().addAll(associations);
        return set;
    }
}