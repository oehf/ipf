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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.XdsRuntimeException;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.XdsEnumAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Contains information about an error.
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorInfo", propOrder = {"errorCode", "codeContext", "severity", "location"})
@EqualsAndHashCode(doNotUseGetters = true)
public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 7615868122051414551L;

    @XmlJavaTypeAdapter(XdsEnumAdapter.ErrorCodeAdapter.class)
    @Getter @Setter private ErrorCode errorCode;

    @Getter @Setter private String codeContext;

    @XmlJavaTypeAdapter(XdsEnumAdapter.SeverityAdapter.class)
    @Getter @Setter private Severity severity;

    @Getter @Setter private String location;


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
     */
    public ErrorInfo(ErrorCode errorCode, String codeContext, Severity severity, String location) {
        this.errorCode = errorCode;
        this.codeContext = codeContext;
        this.severity = severity;
        this.location = location;
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
        this(defaultError, throwable.getMessage(), Severity.ERROR, defaultLocation);
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
