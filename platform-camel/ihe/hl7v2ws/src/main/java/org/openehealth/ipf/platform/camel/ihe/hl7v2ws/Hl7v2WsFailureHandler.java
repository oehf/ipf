/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.cxf.message.Exchange;

/**
 * Interface for pluggable fault handlers in HL7v2 WS transactions.
 * @author Dmytro Rud
 */
public interface Hl7v2WsFailureHandler {

    /**
     * This method will be called when the request has been rejected either
     * by CXF (in this case, the CXF exchange will contain a SOAP fault)
     * or by IPF (the CXF exchange will contain a HL7v2 NAK with code 'AR').
     *
     * @param cxfExchange
     *      CXF exchange containing multiple representations of the request
     *      message and the whole context information.
     */
    void handleFailedExchange(Exchange cxfExchange);
}
