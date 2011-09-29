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
@XmlType(name = "RetrievedDocument")
@XmlRootElement(name = "retrievedDocument")
public class RetrievedDocument implements Serializable {
    private static final long serialVersionUID = -3950026651885804263L;
    
    private transient DataHandler dataHandler;
    @XmlElementRef
    private RetrieveDocument requestData;
    
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
     */
    public RetrievedDocument(DataHandler dataHandler, RetrieveDocument requestData) {
        this.dataHandler = dataHandler;
        this.requestData = requestData;
    }

    /**
     * @return the data handler allowing access to the content of the document.
     */
    public DataHandler getDataHandler() {
        return dataHandler;
    }
    
    /**
     * @param dataHandler 
     *          the data handler allowing access to the content of the document.
     */
    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }
    
    /**
     * @return the data specified in the request.
     */
    public RetrieveDocument getRequestData() {
        return requestData;
    }

    /**
     * @param requestData
     *          the data specified in the request.
     */
    public void setRequestData(RetrieveDocument requestData) {
        this.requestData = requestData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataHandler == null) ? 0 : dataHandler.hashCode());
        result = prime * result + ((requestData == null) ? 0 : requestData.hashCode());
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
        RetrievedDocument other = (RetrievedDocument) obj;
        if (dataHandler == null) {
            if (other.dataHandler != null)
                return false;
        } else if (!dataHandler.equals(other.dataHandler))
            return false;
        if (requestData == null) {
            if (other.requestData != null)
                return false;
        } else if (!requestData.equals(other.requestData))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
