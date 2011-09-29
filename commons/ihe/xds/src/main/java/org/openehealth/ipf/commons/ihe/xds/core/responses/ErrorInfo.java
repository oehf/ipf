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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.XdsRuntimeException;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Contains information about an error.
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorInfo", propOrder = {"errorCode", "codeContext", "severity", "location", "customErrorCode"})
public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 7615868122051414551L;
    
    private ErrorCode errorCode;
    private String codeContext;
    private Severity severity;
    private String location;

    // is used when errorCode is equal to ErrorCode._USER_DEFINED
    private String customErrorCode;


    /**
     * Constructs an error info.
     */
    public ErrorInfo() {}
    
    /**
     * Constructs an error info.
     * @param errorCode
     *          the error that occurred.
     * @param codeContext
     *          the context in which the error occurred.
     * @param severity
     *          the severity of the error.
     * @param location
     *          the location in which the error occurred.
     * @param customErrorCode
     *          user-defined error code for the cases when the <code>errorCode</code> parameter
     *          equals to {@link ErrorCode#_USER_DEFINED}, otherwise will be ignored.
     */
    public ErrorInfo(ErrorCode errorCode, String codeContext, Severity severity, String location, String customErrorCode) {
        this.errorCode = errorCode;
        this.codeContext = codeContext;
        this.severity = severity;
        this.location = location;
        this.customErrorCode = customErrorCode;
    }

    /**
     * Constructs an error info from the given exception.
     * @param throwable
     *          the exception that occurred.
     * @param defaultMetaDataError
     *          the default error code for {@link XDSMetaDataException}.
     * @param defaultError
     *          the default error code for any other exception.
     * @param defaultLocation
     *          default error location.
     */
    public ErrorInfo(
            Throwable throwable,
            ErrorCode defaultMetaDataError,
            ErrorCode defaultError,
            String defaultLocation)
    {
        this(defaultError, throwable.getMessage(), Severity.ERROR, defaultLocation, null);
        while (throwable != null) {
            if (throwable instanceof XDSMetaDataException) {
                XDSMetaDataException metaDataException = (XDSMetaDataException) throwable;
                this.errorCode = metaDataException.getValidationMessage().getErrorCode();
                if (this.errorCode == null) {
                    this.errorCode = defaultMetaDataError;
                }
                this.codeContext = metaDataException.getMessage();
                return;
            }
            if (throwable instanceof XdsRuntimeException) {
                XdsRuntimeException exception = (XdsRuntimeException) throwable;
                this.errorCode = exception.getErrorCode();
                this.codeContext = exception.getCodeContext();
                this.severity = exception.getSeverity();
                this.location = exception.getLocation();
                return;
            }
            throwable = throwable.getCause();
        }
    }

    /**
     * @return the error that occurred.
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    /**
     * @param errorCode
     *          the error that occurred.
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * @return the context in which the error occurred.
     */
    public String getCodeContext() {
        return codeContext;
    }
    
    /**
     * @param codeContext
     *          the context in which the error occurred.
     */
    public void setCodeContext(String codeContext) {
        this.codeContext = codeContext;
    }
    
    /**
     * @return the severity of the error.
     */
    public Severity getSeverity() {
        return severity;
    }
    
    /**
     * @param severity
     *          the severity of the error.
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    
    /**
     * @return the location in which the error occurred.
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * @param location
     *          the location in which the error occurred.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return
     *      custom error code, if any set.
     */
    public String getCustomErrorCode() {
        return customErrorCode;
    }

    public void setCustomErrorCode(String customErrorCode) {
        this.customErrorCode = customErrorCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codeContext == null) ? 0 : codeContext.hashCode());
        result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((severity == null) ? 0 : severity.hashCode());
        result = prime * result + ((customErrorCode == null) ? 0 : customErrorCode.hashCode());
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
        ErrorInfo other = (ErrorInfo) obj;
        if (codeContext == null) {
            if (other.codeContext != null)
                return false;
        } else if (!codeContext.equals(other.codeContext))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        } else if (!errorCode.equals(other.errorCode))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (severity == null) {
            if (other.severity != null)
                return false;
        } else if (!severity.equals(other.severity))
            return false;
        if (customErrorCode == null) {
            if (other.customErrorCode != null)
                return false;
        } else if (!customErrorCode.equals(other.customErrorCode))
            return false;
        return true;
    }    

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
