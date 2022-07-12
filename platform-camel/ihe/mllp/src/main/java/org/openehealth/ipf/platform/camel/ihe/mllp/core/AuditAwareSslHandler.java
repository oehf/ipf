/*
 * Copyright 2021 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.SslHandler;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditUtils;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.util.List;

class AuditAwareSslHandler extends SslHandler {

    private final MllpEndpointConfiguration config;

    public AuditAwareSslHandler(SSLEngine engine, MllpEndpointConfiguration config) {
        super(engine);
        this.config = config;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws SSLException {
        try {
            super.decode(ctx, in, out);
        } catch (SSLException e) {
            if (config.isAudit()) {
                var socketAddress = ctx.channel().remoteAddress();
                var hostname = socketAddress != null ? socketAddress.toString() : "unknown";
                var auditMessage = MllpAuditUtils.auditAuthenticationNodeFailure(
                        config.getAuditContext(), e.getMessage(), hostname);
                config.getAuditContext().audit(auditMessage);
            }
            throw e;
        }
    }

}
