/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.correlation;

/**
 * Interface for message correlators in asynchronous 
 * Web Service-based IHE transactions. 
 * @author Dmytro Rud
 */
public interface AsynchronyCorrelator {

    /**
     * Stores informations about an asynchronous request.
     * @param messageId
     *      WS-Addressing message ID of the request.
     * @param serviceEndpoint
     *      URL of the endpoint the request is being sent to.
     * @param correlationKey
     *      correlation key provided by the user (optional).
     * @param requestPayload
     *      request payload (optional).
     */
    void put(String messageId, 
             String serviceEndpoint, 
             String correlationKey, 
             String requestPayload);
    
    /**
     * Returns the URL of the endpoint to which the message with the given 
     * ID has been sent, or <code>null</code> if the message is unknown.
     */
    String getServiceEndpoint(String messageId);

    /**
     * Returns the user-defined correlation key for the message with the  
     * given ID, or <code>null</code> if the message is unknown or the 
     * user did not provided any correlation key.
     */
    String getCorrelationKey(String messageId);

    /**
     * Returns the payload of the request message with the given ID, 
     * or <code>null</code> if payload saving has been switched off.
     */
    String getRequestPayload(String messageId);

    /**
     * Deletes informations about the message with the given ID.
     * <p>
     * This method is supposed to be called internally after the correlation
     * of an asynchronous response has been completed; the user does not have 
     * to call it explicitly (but who knows...).
     * @return
     *      <code>true</code> when there actually was something to delete, 
     *      i.e. when the given ID was known; <code>false</code> otherwise.
     */
    boolean delete(String messageId);
    
}
