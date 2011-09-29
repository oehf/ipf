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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
public class Response implements Serializable {
    private static final long serialVersionUID = -6370795461214680771L;
    
    private Status status;
    @XmlElement(name = "error")
    private List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
    
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
     *          the default error code for {@link XDSMetaDataException}.
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errors == null) ? 0 : errors.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        Response other = (Response) obj;
        if (errors == null) {
            if (other.errors != null)
                return false;
        } else if (!errors.equals(other.errors))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }    

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
