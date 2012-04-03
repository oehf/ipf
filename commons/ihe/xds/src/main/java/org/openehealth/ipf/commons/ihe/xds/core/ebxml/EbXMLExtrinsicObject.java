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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;

import javax.activation.DataHandler;

/**
 * Encapsulation of the ebXML classes for {@code ExtrinsicObjectType}.
 * <p>
 * This class contains convenience methods and provides a version independent
 * abstraction of the ebXML data structure.
 * @author Jens Riemschneider
 */
public interface EbXMLExtrinsicObject extends EbXMLRegistryObject {
    /**
     * @return the mime type of this object.
     */
    String getMimeType();
    
    /**
     * @param mimeType 
     *          the mime type of this object.
     */
    void setMimeType(String mimeType);

    /**
     * @return the status of this entry.
     */
    AvailabilityStatus getStatus();
    
    /**
     * @param status
     *          the status of this entry.
     */
    void setStatus(AvailabilityStatus status);

    /**
     * @return document returned in an ITI-63 response.
     */
    DataHandler getDataHandler();

    /**
     * @param dataHandler
     *      document to be returned in an ITI-63 response.
     */
    void setDataHandler(DataHandler dataHandler);
}
