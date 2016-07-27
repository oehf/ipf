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
import lombok.ToString;
import org.openehealth.ipf.commons.core.ContentMap;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Represents the contents of a document and the describing entry.
 * <p> 
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 * @author Stefan Ivanov
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document")
@XmlRootElement(name = "document")
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Document extends ContentMap implements Serializable {
    private static final long serialVersionUID = 5206884085835642756L;

    private DocumentEntry documentEntry;

    public Document() {
    }

    /**
     * Constructs a document.
     * @param documentEntry
     *          the document entry describing the meta data of the document.
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    public Document(DocumentEntry documentEntry, DataHandler dataHandler) {
        this.documentEntry = documentEntry;
        if (dataHandler != null) {
            setContent(DataHandler.class, dataHandler);
        }
    }

    /**
     * @return the document entry describing the meta data of the document.
     */
    public DocumentEntry getDocumentEntry() {
        return documentEntry;
    }
    
    /**
     * @param documentEntry
     *            the document entry describing the meta data of the document.
     */
    public void setDocumentEntry(DocumentEntry documentEntry) {
        this.documentEntry = documentEntry;
    }

    /**
     * Same as <code>getContent(DataHandler.class)</code>.
     *
     * @return the data handler allowing access to the contents of the document.
     */
    @XmlElement(name = "data")
    @XmlMimeType("application/octet-stream")
    public DataHandler getDataHandler() {
        return getContent(DataHandler.class);
    }
    
    /**
     * Same as <code>setContent(DataHandler.class, DataHandler dataHandler)</code>.
     *
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    public void setDataHandler(DataHandler dataHandler) {
        setContent(DataHandler.class, dataHandler);
    }

}
