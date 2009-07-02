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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.responses;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Association;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.DocumentEntry;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Folder;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.ObjectReference;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.SubmissionSet;

/**
 * Contains the response data for a query.
 * @author Jens Riemschneider
 */
public class QueryResponse extends Response {
    private final List<ObjectReference> references = new ArrayList<ObjectReference>();
    private final List<DocumentEntry> documentEntries = new ArrayList<DocumentEntry>();
    private final List<Folder> folders = new ArrayList<Folder>();
    private final List<SubmissionSet> submissionSets = new ArrayList<SubmissionSet>();
    private final List<Association> associations = new ArrayList<Association>();
    
    public List<ObjectReference> getReferences() {
        return references;
    }

    public List<DocumentEntry> getDocumentEntries() {
        return documentEntries;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public List<SubmissionSet> getSubmissionSets() {
        return submissionSets;
    }

    public List<Association> getAssociations() {
        return associations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((associations == null) ? 0 : associations.hashCode());
        result = prime * result + ((documentEntries == null) ? 0 : documentEntries.hashCode());
        result = prime * result + ((folders == null) ? 0 : folders.hashCode());
        result = prime * result + ((references == null) ? 0 : references.hashCode());
        result = prime * result + ((submissionSets == null) ? 0 : submissionSets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryResponse other = (QueryResponse) obj;
        if (associations == null) {
            if (other.associations != null)
                return false;
        } else if (!associations.equals(other.associations))
            return false;
        if (documentEntries == null) {
            if (other.documentEntries != null)
                return false;
        } else if (!documentEntries.equals(other.documentEntries))
            return false;
        if (folders == null) {
            if (other.folders != null)
                return false;
        } else if (!folders.equals(other.folders))
            return false;
        if (references == null) {
            if (other.references != null)
                return false;
        } else if (!references.equals(other.references))
            return false;
        if (submissionSets == null) {
            if (other.submissionSets != null)
                return false;
        } else if (!submissionSets.equals(other.submissionSets))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
