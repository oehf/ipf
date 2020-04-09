/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.commons.audit.utils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility functions for obtaining local system context such as
 * local hostname or IP address
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class AuditUtils {

    private static final Map<String, String> systemData = new ConcurrentHashMap<>();

    private static final String PID = "PID";
    private static final String IP = "IP";
    private static final String HOST = "HOST";
    private static final String USER = "USER";

    /**
     * @return the process ID of the running process
     */
    public static String getProcessId() {
        return systemData.computeIfAbsent(PID, s -> Long.toString(ProcessHandle.current().pid()));
    }

    /**
     * @return the IP Address of the local host or "unknown"
     */
    public static String getLocalIPAddress() {
        return systemData.computeIfAbsent(IP, s ->
                localInetAddress()
                        .map(InetAddress::getHostAddress)
                        .orElse("unknown")).trim();
    }

    /**
     * @return the name of the host or "unknown"
     */
    public static String getLocalHostName() {
        return systemData.computeIfAbsent(HOST, s ->
                localInetAddress()
                        .map(InetAddress::getCanonicalHostName)
                        .orElse("unknown")).trim();
    }

    /**
     * @return the name of the user running the process
     */
    public static String getUserName() {
        return systemData.computeIfAbsent(USER, s -> System.getProperty("user.name"));
    }

    /**
     * @param url a (remote) url
     * @return the host name extracted from the provided URL
     */
    public static String getHostFromUrl(String url) {
        if (url == null) return null;

        // drop schema
        int pos = url.indexOf("://");
        if (pos > 0) {
            url = url.substring(pos + 3);
        }

        // drop user authentication information
        pos = url.indexOf('@');
        if (pos > 0) {
            url = url.substring(pos + 1);
        }

        // drop trailing parts: port number, query parameters, path, fragment
        for (int i = 0; i < url.length(); ++i) {
            char c = url.charAt(i);
            if ((c == ':') || (c == '?') || (c == '/') || (c == '#')) {
                return url.substring(0, i);
            }
        }
        return url;
    }

    public static Optional<InetAddress> localInetAddress() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return Optional.of(socket.getLocalAddress());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
