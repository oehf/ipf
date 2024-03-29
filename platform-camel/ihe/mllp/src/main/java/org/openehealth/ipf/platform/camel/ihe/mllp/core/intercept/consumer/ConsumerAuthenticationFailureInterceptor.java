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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import org.apache.camel.Exchange;
import org.apache.camel.component.netty.NettyConstants;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuthenticationFailure;

import java.net.InetSocketAddress;

import static java.util.Objects.requireNonNull;

/**
 * Interceptor that handles any {@link MllpAuthenticationFailure} that occurred while
 * processing an exchange.
 */
public class ConsumerAuthenticationFailureInterceptor extends InterceptorSupport {

    private final AuditContext auditContext;

    public ConsumerAuthenticationFailureInterceptor(AuditContext auditContext) {
        this.auditContext = requireNonNull(auditContext);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            getWrappedProcessor().process(exchange);
        } catch (MllpAuthenticationFailure e) {
            var auditMessage =
                    MllpAuditUtils.auditAuthenticationNodeFailure(
                            auditContext, e.getMessage(), getRemoteAddress(exchange));
            auditContext.audit(auditMessage);
            throw e;
        }
    }

    private String getRemoteAddress(Exchange exchange) {
        var address = (InetSocketAddress) exchange.getIn().getHeader(NettyConstants.NETTY_REMOTE_ADDRESS);
        return address != null ? address.getAddress().getHostAddress() : "unknown";
    }

}
