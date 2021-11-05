/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import org.openehealth.ipf.commons.audit.server.SyslogServer;
import org.openehealth.ipf.commons.audit.server.TlsSyslogServer;
import org.openehealth.ipf.commons.audit.server.UdpSyslogServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.openehealth.ipf.commons.audit.server.support.SyslogEventDICOMPrinter.newErrorConsumer;
import static org.openehealth.ipf.commons.audit.server.support.SyslogEventDICOMPrinter.newEventConsumer;

/**
 * Primitive Server that just logs events received via UDP or TLS
 */
public class AuditRecordServer {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRecordServer.class);

    private static Optional<SyslogServer<?>> getSyslogServer(
            String channel,
            BiFunction<Consumer<? super Map<String, Object>>, Consumer<Throwable>, SyslogServer<?>> f,
            int port) {
        if (port >= 0) {
            var syslogServer = f.apply(newEventConsumer(channel), newErrorConsumer());
            syslogServer.start("0.0.0.0", port);
            return Optional.of(syslogServer);
        }
        return Optional.empty();
    }

    public static void main(String... args) throws Exception {

        if (args.length < 2) {
            LOG.error("Usage: AuditRecordServer <TLS-Port> <UDP-Port> (-1 for not launching channel, 0 for using a random port)");
        } else {
            final var tlsSyslogServer = getSyslogServer("TLS", TlsSyslogServer::new, Integer.parseInt(args[0]));
            final var udpSyslogServer = getSyslogServer("UDP", UdpSyslogServer::new, Integer.parseInt(args[1]));
            LOG.info("Waiting for requests...");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                tlsSyslogServer.ifPresent(SyslogServer::stop);
                udpSyslogServer.ifPresent(SyslogServer::stop);
            }));
            Thread.sleep(Long.MAX_VALUE);
        }
    }

}
