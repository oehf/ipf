/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.manager.connection.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.naming.AuthenticationException;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * The interface is not exposed to the outside. Represents the JMX'view of a
 * connection.
 * 
 * @author Mitko Kolev
 */
public interface IJMXConnectionAdapter extends Serializable {
    /**
     * Implements a bridge to a the JMX implementation of creating a proxy.
     * 
     * 
     * @param <T>
     * @param <N>
     * @param nameURI
     *            The name of the
     * @param proxyClass
     * @param notificationListener
     * @return
     * @throws IOException
     * @throws MalformedObjectNameException
     * @throws InstanceNotFoundException
     * 
     * @See {@link javax.management.JMX#newMBeanProxy(MBeanServerConnection, javax.management.ObjectName, Class)}
     */
    public <T, N extends NotificationListener> T loadProxyForClass(
            String nameURI, Class<T> proxyClass, N notificationListener)
            throws IOException, MalformedObjectNameException,
            InstanceNotFoundException;

    /**
     * Allow real {@link}
     * 
     * @return
     * @throws IOException
     */
    public MBeanServerConnection getMBeanServerConnection() throws IOException;

    /**
     * The connection context for this adapter (the adaptee).
     * 
     * @return the connection instance this adapter adapts.
     */
    public IConnectionConfiguration getConnectionConfigurationContext();

    /**
     * Physically opens a remote connection to a JMX server.
     * 
     * @throws MalformedURLException
     *             if the host is wrong
     * @throws IOException
     *             if the connection can not be established.
     * @throws AuthenticationException
     *             if the JMX server requires authentication.
     */
    public void openConnection() throws MalformedURLException, IOException,
            AuthenticationException;

    /**
     * Closes the physical JMX connection silently.
     */
    public boolean closeConnection();

    /**
     * Returns the URL to which the JMXConnectorFactory will connect.
     * 
     * @return
     */
    public String createConnectionURL();
}