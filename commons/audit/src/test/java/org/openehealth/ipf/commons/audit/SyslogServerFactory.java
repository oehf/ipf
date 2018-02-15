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

import io.vertx.core.Verticle;
import io.vertx.ext.unit.Async;

/**
 *
 */
public final class SyslogServerFactory {

    public static Verticle createUDPServer(String host, int port, Async async){
        return new UDPSyslogServer(host, port, async);
    }

    public static Verticle createTCPServer(int port, Async async){
        return new TCPSyslogServer(port, async);
    }

    public static Verticle createTCPServerOneWayTLS(int port, String keystorePath, String keystorePassword, Async async){
        return new TCPSyslogServer(port, "NONE", null, null, keystorePath, keystorePassword, async);
    }

    public static Verticle createTCPServerTwoWayTLS(int port,
                                                    String truststorePath, String truststorePassword,
                                                    String keystorePath, String keystorePassword, Async async){
        return new TCPSyslogServer(port, "REQUIRED", truststorePath, truststorePassword,
                                   keystorePath, keystorePassword, async);
    }

}
