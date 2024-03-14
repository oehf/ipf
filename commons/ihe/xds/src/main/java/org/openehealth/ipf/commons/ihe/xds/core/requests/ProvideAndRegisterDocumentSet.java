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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.builder.ProvideAndRegisterDocumentSetBuilder;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The data required for the Provide and register document set request.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvideAndRegisterDocumentSet", propOrder = {
        "submissionSet", "folders", "documents", "associations"})
@XmlRootElement(name = "provideAndRegisterDocumentSet")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class ProvideAndRegisterDocumentSet implements Serializable {
    private static final long serialVersionUID = -746285879968609663L;
    
    @XmlElementRef
    @Getter @Setter private SubmissionSet submissionSet;
    @XmlElementRef
    @Getter private final List<Folder> folders = new ArrayList<>();
    @XmlElementRef
    @Getter private final List<Document> documents = new ArrayList<>();
    @XmlElementRef
    @Getter private final List<Association> associations = new ArrayList<>();
    @XmlAttribute
    @Getter @Setter private String targetHomeCommunityId;
    
    public static ProvideAndRegisterDocumentSetBuilder builderWith(SubmissionSet submissionSet) {
        return new ProvideAndRegisterDocumentSetBuilder(false, submissionSet);
    }

    /**
     * Provide a builder with additional support for generating attributes if not already present.
     * - SubmissionSet.submissionTime
     * - UniqueId's
     * - EntryUuid's
     * - HasMember associations
     * 
     * @return Builder that constructs a ProvideAndRegisterDocumentSet and generate certain attributes.
     */
    public static ProvideAndRegisterDocumentSetBuilder supportiveBuilderWith(SubmissionSet submissionSet) {
        return new ProvideAndRegisterDocumentSetBuilder(true, submissionSet);
    }


}
