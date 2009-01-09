/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.process;

import java.util.Collection;

import org.apache.camel.Message;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;

/**
 * Provides support for handling extraction and integration of attachments for a 
 * specific type of message.
 * <p>
 * A handler is usually tied to an endpoint and defines a strategy for 
 * extracting attachments from an exchange produced by the endpoint as well as
 * integrating attachments into an exchange send to the endpoint.
 * @author Jens Riemschneider
 */
public interface AttachmentHandler {

    /**
     * Handle message extraction for the given message.
     * <p>
     * If the message is not compatible with the handler, this method will 
     * return without taking any further action. If the message is compatible, 
     * the handler will extract the attachment and return them.
     * <p>
     * The handler is also allowed to change the contents of the message directly.
     * 
     * @param unitOfWorkId
     *          the id of the current unit of work 
     * @param message
     *          the message that is to be handled
     * @return the attachments that were extracted 
     * @throws Exception 
     *          any kind of exception that occurred during the handling
     */
    Collection<AttachmentDataSource> extract(String unitOfWorkId, Message message) throws Exception;

    /**
     * Handle message integration for the given message.
     * <p>
     * In contrast to {@link #extract(Message)}, this method cannot test if it
     * is operating for a compatible endpoint. Therefore, the message is always
     * changed to meet the requirements of the endpoint that is compatible with
     * this handler. The actual endpoint might show unexpected behavior or throw
     * an exception if it is not compatible.
     * @param message
     *          the message that is to be handled 
     * @param attachments
     *          the attachments that need to be integrated into the message
     * @throws Exception 
     *          any kind of exception that occurred during the handling
     */
    void integrate(Message message, Collection<AttachmentDataSource> attachments) throws Exception;

    /**
     * Removes the attachments that were created for the given unit of work
     * <p>
     * This method might only mark the resource for removal after their next
     * usage. This is necessary if the resources are still needed for the 
     * final endpoint processing of the message. This method is called when 
     * the unit of work is completed but before the endpoint processed the
     * output message.  
     * @param unitOfWorkId
     *          the id of the current unit of work 
     * @param message
     *          the message that is going to be send out
     */
    void cleanUp(String unitOfWorkId, Message message);
}
