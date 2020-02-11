/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.ext.unit.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TCPSyslogServer extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(TCPSyslogServer.class);

    private final int port;

    private final NetServerOptions nsOptions;

    private final Async async;

    public TCPSyslogServer(int port, Async async){
        this.port = port;
        this.async = async;
        nsOptions = new NetServerOptions()
                .setReuseAddress(true)
                .setHost("localhost")
                .setSsl(false);
    }

    public TCPSyslogServer(int port, String clientAuth,
                           String trustStorePath, String trustStorePassword,
                           String keyStorePath, String keyStorePassword,
                           Async async){
        this.port = port;
        this.async = async;
        nsOptions = new NetServerOptions()
                .setReuseAddress(true)
                .setHost("localhost")
                .setClientAuth(ClientAuth.valueOf(clientAuth))
                .setTrustStoreOptions(trustStorePath != null? new JksOptions().
                        setPath(trustStorePath).
                        setPassword(trustStorePassword): null)
                .setKeyStoreOptions(keyStorePath != null? new JksOptions().
                        setPath(keyStorePath).
                        setPassword(keyStorePassword): null)
                .setSsl(true);
    }

    @Override
    public void start() {
        LOG.info("Starting syslog server");
        NetServer netServer = vertx.createNetServer(nsOptions);
        netServer.connectHandler(netSocket -> netSocket.handler(buffer -> {
            LOG.debug("Received content on port {} ({} bytes)", port, buffer.length());
            async.countDown();
        })).listen(port);
    }
}
