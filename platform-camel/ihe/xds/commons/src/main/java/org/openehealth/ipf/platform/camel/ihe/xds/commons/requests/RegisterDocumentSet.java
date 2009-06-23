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

import java.util.ArrayList;
import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;

/**
 * The data required for the register document set request.
 * @author Jens Riemschneider
 */
public class RegisterDocumentSet {
    private SubmissionSet submissionSet;
    private final List<Folder> folders = new ArrayList<Folder>();
    private final List<DocumentEntry> documentEntries = new ArrayList<DocumentEntry>();
    private final List<Association> associations = new ArrayList<Association>();

    public SubmissionSet getSubmissionSet() {
        return submissionSet;
    }
    
    public void setSubmissionSet(SubmissionSet submissionSet) {
        this.submissionSet = submissionSet;
    }
    
    public List<Folder> getFolders() {
        return folders;
    }

    public List<DocumentEntry> getDocumentEntries() {
        return documentEntries;
    }

    public List<Association> getAssociations() {
        return associations;
    }
}
