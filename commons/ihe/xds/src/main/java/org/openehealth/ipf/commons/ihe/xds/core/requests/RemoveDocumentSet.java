/*
 * Copyright 2013 the original author or authors.
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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object for the Remove Document Set transaction.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Boris Stanojevic
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveDocumentSet", propOrder = {
        "query",
        "references"
})
@XmlRootElement(name = "removeDocumentSet")
@EqualsAndHashCode(callSuper = false, doNotUseGetters = false)
public class RemoveDocumentSet implements Serializable {
    private static final long serialVersionUID = -737326382128159189L;

    public static final String DEFAULT_DELETION_SCOPE = "urn:oasis:names:tc:ebxml-regrep:DeletionScopeType:DeleteAll";

    @XmlElementRefs({
            @XmlElementRef(type = FindDocumentsQuery.class),
            @XmlElementRef(type = FindDocumentsForMultiplePatientsQuery.class),
            @XmlElementRef(type = FetchQuery.class),
            @XmlElementRef(type = FindFoldersQuery.class),
            @XmlElementRef(type = FindFoldersForMultiplePatientsQuery.class),
            @XmlElementRef(type = FindSubmissionSetsQuery.class),
            @XmlElementRef(type = GetAllQuery.class),
            @XmlElementRef(type = GetAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsAndAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsQuery.class),
            @XmlElementRef(type = GetFolderAndContentsQuery.class),
            @XmlElementRef(type = GetFoldersForDocumentQuery.class),
            @XmlElementRef(type = GetFoldersQuery.class),
            @XmlElementRef(type = GetFromDocumentQuery.class),
            @XmlElementRef(type = GetRelatedDocumentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetAndContentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetsQuery.class)})
    @Getter @Setter
    private Query query;

    @XmlElementRef
    @Getter
    private final List<ObjectReference> references = new ArrayList<ObjectReference>();

    @XmlAttribute
    @Setter
    private String deletionScope;

    public String getDeletionScope(){
        if (deletionScope == null){
            deletionScope = DEFAULT_DELETION_SCOPE;
        }
        return deletionScope;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
