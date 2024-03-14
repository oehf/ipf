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
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic response information.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response", propOrder = {"status", "errors"})
@XmlRootElement(name = "response")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Response implements Serializable {
    private static final long serialVersionUID = -6370795461214680771L;
    
    private Status status;
    @XmlElement(name = "error")
    private List<ErrorInfo> errors = new ArrayList<>();
    
    /**
     * Constructs the response.
     */
    public Response() {}
    
    /**
     * Constructs the response.
     * @param status
     *          the status of the request execution.
     */
    public Response(Status status) {        
        this.status = status;
    }
    
    /**
     * Constructs an error response object with the data from an exception.
     * @param throwable
     *          the exception that occurred.
     * @param defaultMetaDataError
     *          the default error code for
     *          {@link org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException}.
     * @param defaultError
     *          the default error code for any other exception.
     * @param location
     *          error location.
     */
    public Response(
            Throwable throwable,
            ErrorCode defaultMetaDataError,
            ErrorCode defaultError,
            String location)
    {
        this.status = Status.FAILURE;
        this.errors.add(new ErrorInfo(throwable, defaultMetaDataError, defaultError, location));
    }

    /**
     * @return the status of the request execution.
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * @param status
     *          the status of the request execution.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the list of errors that occurred.
     */
    public List<ErrorInfo> getErrors() {
        return errors;
    }

    /**
     * @param errors
     *          the list of errors that occurred.
     */
    public void setErrors(List<ErrorInfo> errors) {
        this.errors = errors;
    }


}
