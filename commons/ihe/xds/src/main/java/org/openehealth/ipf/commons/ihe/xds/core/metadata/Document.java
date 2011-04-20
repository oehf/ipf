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
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.util.DocumentContentHelper;

/**
 * Represents the contents of a document and the describing entry.
 * <p> 
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public class Document implements Serializable {
    private static final long serialVersionUID = 5206884085835642756L;
    
    private DocumentEntry documentEntry;
    private transient Map<Class<?>, Object> contents = new HashMap<Class<?>, Object>();

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
        if (dataHandler != null)
            addContents(DataHandler.class, dataHandler);
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
     * @return the data handler allowing access to the contents of the document.
     */
    @Deprecated
    public DataHandler getDataHandler() {
        return getContents(DataHandler.class);
    }
    
    /**
     * @param dataHandler
     *          the data handler allowing access to the contents of the document.
     */
    @Deprecated
    public void setDataHandler(DataHandler dataHandler) {
        addContents(DataHandler.class, dataHandler);
    }
    
    public Map<Class<?>, Object> getContents() {
        return contents;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getContents(Class<T> key) {
        if (contents.isEmpty())
            return null;
        if (!contents.containsKey(key)) {
            T result = DocumentContentHelper.convert(contents, key);
            if (result != null)
                addContents(key, result);
        }
        return (T) contents.get(key);
    }
    
    public Object addContents(Class<?> clazz, Object content) {
        return contents.put(clazz, content);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((getContents(DataHandler.class) == null) ?
                0 : getContents(DataHandler.class).hashCode());
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
