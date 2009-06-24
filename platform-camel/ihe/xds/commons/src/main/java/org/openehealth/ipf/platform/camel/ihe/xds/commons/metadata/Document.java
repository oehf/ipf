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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

import javax.activation.DataHandler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents the contents of a document and the describing entry.
 * @author Jens Riemschneider
 */
public class Document {
    private DocumentEntry documentEntry;
    private DataHandler dataHandler;
     
    public DataHandler getDataHandler() {
        return dataHandler;
    }
    
    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public DocumentEntry getDocumentEntry() {
        return documentEntry;
    }

    public void setDocumentEntry(DocumentEntry documentEntry) {
        this.documentEntry = documentEntry;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
