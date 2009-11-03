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
package org.openehealth.ipf.platform.camel.ihe.xds.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.openehealth.ipf.platform.camel.ws.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ws.DefaultItiWebService;

/**
 * Base class for XDS web services that are aware of a {@link DefaultItiConsumer}.
 *
 * @author Jens Riemschneider
 */
public class DefaultXdsWebService extends DefaultItiWebService {
    private static final Log log = LogFactory.getLog(DefaultXdsWebService.class);
    
    /**
     * Configures an error response object with the data from an exception.
     * @param errorResponse
     *          the response object to configure.
     * @param throwable
     *          the exception that occurred.
     * @param defaultMetaDataError
     *          the default error code for {@link XDSMetaDataException}.
     * @param defaultError
     *          the default error code for any other exception.
     */
    protected void configureError(Response errorResponse, Throwable throwable, ErrorCode defaultMetaDataError, ErrorCode defaultError) {
        errorResponse.setStatus(Status.FAILURE);
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCodeContext(throwable.getMessage());
        errorInfo.setSeverity(Severity.ERROR);
        errorResponse.getErrors().add(errorInfo);

        if (throwable instanceof XDSMetaDataException)  {
            XDSMetaDataException exception = (XDSMetaDataException) throwable;
            if (exception.getValidationMessage().getErrorCode() == null) {
                errorInfo.setErrorCode(defaultMetaDataError);
            }
            else {
                errorInfo.setErrorCode(exception.getValidationMessage().getErrorCode());
            }
        }
        else {
            errorInfo.setErrorCode(defaultError);
        }
        
        log.info("Configured error: " + errorResponse);
    }
}
