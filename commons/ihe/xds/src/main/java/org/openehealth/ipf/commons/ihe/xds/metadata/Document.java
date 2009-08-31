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
package org.openehealth.ipf.commons.ihe.xds.metadata;

import java.io.Serializable;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents the contents of a document and the describing entry.
 * <p> 
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public class Document implements Serializable {
    private static final long serialVersionUID = 5206884085835642756L;
    
    private DocumentEntry documentEntry;
    private transient DataHandler dataHandler;
    
    /**
     * Constructs a document.
     */
    public Document() {}

    /**
     * Constructs a document.
     * @param documentEntry
     *          the document entry describing the meta data of the document.
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    public Document(DocumentEntry documentEntry, DataHandler dataHandler) {
        this.documentEntry = documentEntry;
        this.dataHandler = dataHandler;
    }

    /**
     * @return the data handler allowing access to the contents of the document.
     */
    public DataHandler getDataHandler() {
        return dataHandler;
    }
    
    /**
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * @return the document entry describing the meta data of the document.
     */
    public DocumentEntry getDocumentEntry() {
        return documentEntry;
    }

    /**
     * @param documentEntry
     *          the document entry describing the meta data of the document.
     */
    public void setDocumentEntry(DocumentEntry documentEntry) {
        this.documentEntry = documentEntry;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataHandler == null) ? 0 : dataHandler.hashCode());
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
        if (dataHandler == null) {
            if (other.dataHandler != null)
                return false;
        } else if (!dataHandler.equals(other.dataHandler))
            return false;
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
