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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.message.Exchange;

/**
 * Rejection handling strategy for WS transactions.
 * @author Dmytro Rud
 */
public interface WsRejectionHandlingStrategy {

    /**
     * This method will be called when the request has been rejected
     * by CXF (in this case, the CXF exchange will contain a SOAP fault)
     * or by IPF (transaction-specific logic).
     *
     * @param cxfExchange
     *      CXF exchange containing multiple representations of the request
     *      message and the whole context information.
     */
    void handleRejectedExchange(Exchange cxfExchange);


    /**
     * This method should return <code>true</code> when the given CXF
     * exchange can be considered rejected (failed).
     * <p>
     * @see org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils#extractOutgoingException(org.apache.cxf.message.Exchange)
     *
     * @param cxfExchange
     *      CXF exchange under consideration.
     * @return
     *      <code>true</code> when the given CXF exchange has been
     *      rejected; <code>false</code> otherwise.
     */
    boolean isRejected(Exchange cxfExchange);
}
