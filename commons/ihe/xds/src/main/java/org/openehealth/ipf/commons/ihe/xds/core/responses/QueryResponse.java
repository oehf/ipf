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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the response data for a query.
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryResponse", propOrder = {
        "references", "submissionSets", "folders", "documentEntries", "associations", "documents"})
@XmlRootElement(name = "queryResponse")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class QueryResponse extends Response implements Serializable {
    @Serial
    private static final long serialVersionUID = -435462523350768903L;
    
    @XmlElement(name = "reference")
    @Getter @Setter private List<ObjectReference> references = new ArrayList<>();
    @XmlElementRef
    @Getter @Setter private List<DocumentEntry> documentEntries = new ArrayList<>();
    @XmlElementRef
    @Getter @Setter private List<Folder> folders = new ArrayList<>();
    @XmlElementRef
    @Getter @Setter private List<SubmissionSet> submissionSets = new ArrayList<>();
    @XmlElementRef
    @Getter @Setter private List<Association> associations = new ArrayList<>();
    @XmlElementRef
    @Getter @Setter private List<Document> documents = new ArrayList<>();
    
    /**
     * Constructs the response.
     */
    public QueryResponse() {}
    
    /**
     * Constructs the response.
     * @param status
     *          the status of the request execution.
     */
    public QueryResponse(Status status) {        
        super(status);
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
    public QueryResponse(
            Throwable throwable,
            ErrorCode defaultMetaDataError,
            ErrorCode defaultError,
            String location)
    {
        super(throwable, defaultMetaDataError, defaultError, location);
    }

}
