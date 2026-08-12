/*
 * Copyright 2026 the original author or authors.
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

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.MAPAggregator;

/**
 * Since CXF 4.1.8, WS-Addressing decoupled destinations (i.e. non-anonymous
 * <tt>wsa:ReplyTo</tt> and <tt>wsa:FaultTo</tt> endpoint references) are rejected
 * by default in order to prevent SSRF attacks: without an explicit opt-in, CXF
 * answers such requests with a <tt>wsa:DestinationUnreachable</tt> SOAP fault
 * instead of dispatching the response to the given URI.
 * <p>
 * Asynchronous responses over WS-Addressing are a regular part of several IHE
 * transactions (XCPD, XCA, XDR, ...), so IPF pre-approves them per exchange for
 * those endpoints whose transaction configuration declares
 * {@link org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration#isAllowAsynchrony()}.
 * This spares the deployment from having to set the JVM-wide system property
 * <tt>org.apache.cxf.ws.addressing.decoupled.enabled</tt>, which would enable
 * decoupled destinations for <i>all</i> endpoints in the JVM.
 * <p>
 * The URI scheme allowlist (system property
 * <tt>org.apache.cxf.ws.addressing.decoupled.allowedSchemes</tt>, by default
 * <tt>http://</tt>, <tt>https://</tt> and a few others) remains in force &mdash;
 * this interceptor cannot be used to reach e.g. <tt>file://</tt> destinations.
 *
 * @author Christian Ohr
 */
public class DecoupledDestinationApprovalInterceptor extends AbstractPhaseInterceptor<Message> {

    public DecoupledDestinationApprovalInterceptor() {
        super(Phase.PRE_LOGICAL);
        addBefore(MAPAggregator.class.getName());
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        message.getExchange().put(ContextUtils.DECOUPLED_DESTINATION_APPROVED_PROPERTY, Boolean.TRUE);
    }
}
