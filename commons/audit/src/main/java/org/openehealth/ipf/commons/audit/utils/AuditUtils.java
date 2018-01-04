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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Christian Ohr
 */
public class AuditUtils {

    private static Map<String, String> systemData = new ConcurrentHashMap<>();

    private static final String PID = "PID";
    private static final String IP = "IP";
    private static final String HOST = "IP";
    private static final String USER = "USER";

    /**
     * @return the (optional) process ID of the running process or
     */
    public static String getProcessId() {
        return systemData.computeIfAbsent(PID, s -> {
            RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
            String name = mx.getName();
            int pointer;
            if ((pointer = name.indexOf('@')) != -1) {
                return name.substring(0, pointer);
            }
            return "unknown";
        });
    }

    /**
     * @return the IP Address of the host
     */
    public static String getLocalIPAddress() {
        return systemData.computeIfAbsent(IP, s ->
                inetAddress()
                        .map(InetAddress::getHostAddress)
                        .orElse("unknown")).trim();
    }

    /**
     * @return the name of the host
     */
    public static String getLocalHostName() {
        return systemData.computeIfAbsent(HOST, s ->
                inetAddress()
                        .map(InetAddress::getCanonicalHostName)
                        .orElse("unknown")).trim();
    }

    /**
     * @return the name of the user running the process
     */
    public static String getUserName() {
        return systemData.computeIfAbsent(HOST, s -> System.getProperty("user.name"));
    }

    public static String getHostFromUrl(String address) {
        if (address == null) return null;

        // drop schema
        int pos = address.indexOf("://");
        if (pos > 0) {
            address = address.substring(pos + 3);
        }

        // drop user authentication information
        pos = address.indexOf('@');
        if (pos > 0) {
            address = address.substring(pos + 1);
        }

        // drop trailing parts: port number, query parameters, path, fragment
        for (int i = 0; i < address.length(); ++i) {
            char c = address.charAt(i);
            if ((c == ':') || (c == '?') || (c == '/') || (c == '#')) {
                return address.substring(0, i);
            }
        }
        return address;
    }

    private static Optional<InetAddress> inetAddress() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return Optional.of(socket.getLocalAddress());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
