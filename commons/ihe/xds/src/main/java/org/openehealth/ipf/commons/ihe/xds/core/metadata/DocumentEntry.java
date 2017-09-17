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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.PatientInfoAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.PatientInfoXml;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.StringMap;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.StringMapAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XDS document entry according to the IHE XDS specification.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentEntry", propOrder = {
        "sourcePatientId", "sourcePatientInfo", "creationTime", "authors", "legalAuthenticator", "serviceStartTime",
        "serviceStopTime", "classCode", "confidentialityCodes", "eventCodeList", "formatCode",
        "healthcareFacilityTypeCode", "languageCode", "practiceSettingCode", "typeCode", "repositoryUniqueId",
        "mimeType", "size", "hash", "uri", "type", "referenceIdList", "documentAvailability"})
@XmlRootElement(name = "documentEntry")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class DocumentEntry extends XDSMetaClass implements Serializable {
    private static final long serialVersionUID = -4779500440504776909L;
    
    @XmlElement(name = "author")
    @Getter private final List<Author> authors = new ArrayList<>();
    @Getter @Setter private Code classCode;
    @XmlElement(name = "confidentialityCode")
    @Getter private final List<Code> confidentialityCodes = new ArrayList<>();
    @Getter private Timestamp creationTime;
    @XmlElement(name = "eventCode")
    @Getter private final List<Code> eventCodeList = new ArrayList<>();
    @Getter @Setter private Code formatCode;
    @Getter @Setter private String hash;
    @Getter @Setter private Code healthcareFacilityTypeCode;
    @Getter @Setter private String languageCode;
    @Getter @Setter private Person legalAuthenticator;
    @Getter @Setter private String mimeType;
    @Getter @Setter private Code practiceSettingCode;
    @Getter private Timestamp serviceStartTime;
    @Getter private Timestamp serviceStopTime;
    @Getter @Setter private Long size;
    @Getter @Setter private Identifiable sourcePatientId;
    @XmlJavaTypeAdapter(PatientInfoAdapter.class)
    @XmlElement(name = "sourcePatientInfo", type = PatientInfoXml.class)
    @Getter @Setter private PatientInfo sourcePatientInfo;
    @Getter @Setter private Code typeCode;
    @Getter @Setter private String uri;
    @Getter @Setter private String repositoryUniqueId;
    @Getter @Setter private DocumentEntryType type = DocumentEntryType.STABLE;
    @Getter private List<ReferenceId> referenceIdList = new ArrayList<>();
    @Getter @Setter private DocumentAvailability documentAvailability;

    /**
     * @param author
     *          the author of the document.
     * @deprecated please add the author to the list returned by {@link #getAuthors()}
     */
    @Deprecated
    public void setAuthor(Author author) {
        authors.clear();
        authors.add(author);
    }

    /**
     * @return the author of the document. If the document has multiple authors
     *          this method returns only the first in the list. If the document
     *          has no authors, this method returns {@code null}.
     * @deprecated please iterate over the list returned by {@link #getAuthors()}
     */
    @Deprecated
    public Author getAuthor() {
        return authors.isEmpty() ? null : authors.get(0);
    }


    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = Timestamp.fromHL7(creationTime);
    }

    public void setServiceStartTime(Timestamp serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public void setServiceStartTime(String serviceStartTime) {
        this.serviceStartTime = Timestamp.fromHL7(serviceStartTime);
    }

    public void setServiceStopTime(Timestamp serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    public void setServiceStopTime(String serviceStopTime) {
        this.serviceStopTime = Timestamp.fromHL7(serviceStopTime);
    }

}
