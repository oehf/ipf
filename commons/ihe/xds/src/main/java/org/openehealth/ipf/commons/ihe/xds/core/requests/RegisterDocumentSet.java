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
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;

/**
 * The data required for the register document set request.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterDocumentSet", propOrder = {
        "submissionSet", "folders", "documentEntries", "associations"})
@XmlRootElement(name = "registerDocumentSet")
public class RegisterDocumentSet implements Serializable {
    private static final long serialVersionUID = 4029172072096691799L;
    
    @XmlElementRef
    private SubmissionSet submissionSet;
    @XmlElementRef
    private final List<Folder> folders = new ArrayList<Folder>();
    @XmlElementRef
    private final List<DocumentEntry> documentEntries = new ArrayList<DocumentEntry>();
    @XmlElementRef
    private final List<Association> associations = new ArrayList<Association>();

    /**
     * @return the submission set.
     */
    public SubmissionSet getSubmissionSet() {
        return submissionSet;
    }
    
    /**
     * @param submissionSet 
     *          the submission set.
     */
    public void setSubmissionSet(SubmissionSet submissionSet) {
        this.submissionSet = submissionSet;
    }
    
    /**
     * @return the list of folders.
     */
    public List<Folder> getFolders() {
        return folders;
    }

    /**
     * @return the list of document entries.
     */
    public List<DocumentEntry> getDocumentEntries() {
        return documentEntries;
    }

    /**
     * @return the list of associations.
     */
    public List<Association> getAssociations() {
        return associations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((associations == null) ? 0 : associations.hashCode());
        result = prime * result + ((documentEntries == null) ? 0 : documentEntries.hashCode());
        result = prime * result + ((folders == null) ? 0 : folders.hashCode());
        result = prime * result + ((submissionSet == null) ? 0 : submissionSet.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegisterDocumentSet other = (RegisterDocumentSet) obj;
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
        if (submissionSet == null) {
            if (other.submissionSet != null)
                return false;
        } else if (!submissionSet.equals(other.submissionSet))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
