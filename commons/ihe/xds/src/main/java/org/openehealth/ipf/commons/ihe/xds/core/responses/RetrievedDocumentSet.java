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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Documents returned by the Retrieve Document Set transaction.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrievedDocumentSet")
@XmlRootElement(name = "retrievedDocumentSet")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class RetrievedDocumentSet extends Response implements Serializable {    
    @Serial
    private static final long serialVersionUID = 4389321453383292730L;
    
    @XmlElementRef
    private final List<RetrievedDocument> documents = new ArrayList<>();

    /**
     * Constructs the response.
     */
    public RetrievedDocumentSet() {}
    
    /**
     * Constructs the response.
     * @param status
     *          the status of the request execution.
     */
    public RetrievedDocumentSet(Status status) {        
        super(status);
    }
    
    /**
     * Constructs the response.
     * @param status
     *          the status of the request execution.
     * @param documents
     *          the documents to add to this set.
     */
    public RetrievedDocumentSet(Status status, List<RetrievedDocument> documents) {        
        super(status);
        this.documents.addAll(documents);
    }
    
    /**
     * Constructs an error response object with the data from an exception.
     * @param throwable
     *          the exception that occurred.
     * @param defaultMetaDataError
     *          the default error code for {@link org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException}.
     * @param defaultError
     *          the default error code for any other exception.
     * @param location
     *          error location.
     */
    public RetrievedDocumentSet(
            Throwable throwable,
            ErrorCode defaultMetaDataError,
            ErrorCode defaultError,
            String location) {
        super(throwable, defaultMetaDataError, defaultError, location);
    }

    /**
     * @return the documents.
     */
    public List<RetrievedDocument> getDocuments() {
        return documents;
    }

}
