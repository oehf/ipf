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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;

/**
 * Represents a ebXML {@code RegistryResponseType}.
 * @author Jens Riemschneider
 */
public interface EbXMLRegistryResponse {
    /**
     * @param status
     *          the status result.
     */
    void setStatus(Status status);
    
    /**
     * @return the status result.
     */
    Status getStatus();

    /**
     * @param errors
     *          the error list.
     */
    void setErrors(List<EbXMLRegistryError> errors);
    
    /**
     * @return the error list.
     */
    List<EbXMLRegistryError> getErrors();

    /**
     * @return the ebXML object being wrapped by this class.
     */
    Object getInternal();
}
