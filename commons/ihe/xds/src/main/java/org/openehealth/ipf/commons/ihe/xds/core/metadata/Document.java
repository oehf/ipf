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

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.core.ContentMap;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.*;

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
     * This method is deprecated.
     * Use <code>getContent(DataHandler.class)</code> instead.
     *
     * @return the data handler allowing access to the contents of the document.
     */
    @Deprecated
    public DataHandler getDataHandler() {
        return getContent(DataHandler.class);
    }
    
    /**
     * This method is deprecated.
     * Use <code>setContent(DataHandler.class, dataHandler)</code> instead.
     *
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    @Deprecated
    public void setDataHandler(DataHandler dataHandler) {
        setContent(DataHandler.class, dataHandler);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((getContent(DataHandler.class) == null) ?
                0 : getContent(DataHandler.class).hashCode());
        result = prime * result + ((documentEntry == null) ? 0 : documentEntry.hashCode());
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
        Document other = (Document) obj;
        if (documentEntry == null) {
            if (other.documentEntry != null)
                return false;
        } else if (!documentEntry.equals(other.documentEntry))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
