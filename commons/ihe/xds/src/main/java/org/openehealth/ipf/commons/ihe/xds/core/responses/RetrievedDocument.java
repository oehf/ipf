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
package org.openehealth.ipf.commons.ihe.xds.core.responses;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

import javax.activation.DataHandler;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;

/**
 * A single document retrieved from the repository.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrievedDocument", propOrder = {"requestData", "mimeType", "newRepositoryUniqueId",
        "newDocumentUniqueId"})
@XmlRootElement(name = "retrievedDocument")
@EqualsAndHashCode(doNotUseGetters = true)
public class RetrievedDocument implements Serializable {
    private static final long serialVersionUID = -3950026651885804263L;
    
    @Getter @Setter private transient DataHandler dataHandler;
    @XmlElementRef
    @Getter @Setter private RetrieveDocument requestData;
    @Getter @Setter private String mimeType;
    @Getter @Setter private String newRepositoryUniqueId;
    @Getter @Setter private String newDocumentUniqueId;

    /**
     * Constructs the retrieved document.
     */
    public RetrievedDocument() {}

    /**
     * Constructs the retrieved document.
     * @param dataHandler
     *          the data handler allowing access to the content of the document. 
     * @param requestData
     *          the data specified in the request.
     * @param newRepositoryUniqueId
     *          ID of the Document Repository that will support retrieval of the
     *          document created as a result of retrieval of the On-Demand Document
     *          (required when the On-Demand Document Source supports the Persistence
     *          of Retrieved Documents Option).
     * @param newDocumentUniqueId
     *          ID of the document created as a result of retrieval
     *          of the On-Demand Document.
     * @param mimeType
     *          MIME type of the document.
     */
    public RetrievedDocument(
            DataHandler dataHandler,
            RetrieveDocument requestData,
            String newRepositoryUniqueId,
            String newDocumentUniqueId,
            String mimeType)
    {
        this.dataHandler = dataHandler;
        this.requestData = requestData;
        this.newRepositoryUniqueId = newRepositoryUniqueId;
        this.newDocumentUniqueId = newDocumentUniqueId;
        this.mimeType = mimeType;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
