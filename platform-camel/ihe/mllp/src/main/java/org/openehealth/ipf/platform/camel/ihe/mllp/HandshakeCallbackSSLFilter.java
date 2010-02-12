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
package org.openehealth.ipf.platform.camel.ihe.mllp;

import org.apache.mina.common.IoFilter;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.SSLFilter;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

/**
 * {@link IoFilter} similar to an {@link SSLFilter} that provides a
 * callbacks to handle a handshake exception.
 */
public class HandshakeCallbackSSLFilter extends SSLFilter {
    /**
     * Callback interface for dealing with handshake failures.
     */
    public interface Callback {
        /**
         * Runs the callback.
         * @param session
         *          the session in which the handshake failure occurred.
         */
        void run(IoSession session);
    }

    private Callback handshakeExceptionCallback;

    /** Creates a new SSL filter using the specified {@link SSLContext}.
     * @param sslContext
     *          the context.
     */
    public HandshakeCallbackSSLFilter(SSLContext sslContext) {
        super(sslContext);
    }

    /**
     * Sets the callback used if a failure occurred while receiving a handshake.
     * @param handshakeExceptionCallback
     *          the callback to use.
     */
    public void setHandshakeExceptionCallback(Callback handshakeExceptionCallback) {
        this.handshakeExceptionCallback = handshakeExceptionCallback;
    }

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws SSLException {
        try {
            super.messageReceived(nextFilter, session, message);
        }
        catch (SSLHandshakeException e) {
            handshakeExceptionCallback.run(session);
            throw e;
        }
    }
}
