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
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.AuthenticationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.osgi.Activator;

/**
 * Facade for openning and closing a connection. Creates RMI connection to the
 * given connection.
 * 
 * @see IJMXConnectionAdapter
 * 
 * @author Mitko Kolev
 */
public class JMXRMIConnectionAdapterImpl implements IJMXConnectionAdapter,
        NotificationListener {

    private final static long serialVersionUID = 5936363367842548992L;

    private final static Log log = LogFactory
            .getLog(JMXRMIConnectionAdapterImpl.class);

    private transient JMXConnector connector;

    /**
     * The connection context associated with this Adapter.
     */
    private final IConnectionConfiguration connectionConfigurationContext;

    /**
     * SOMETIMES THE Notification listener notifies infinitely! This flag is
     * updated when the connection is opened or closed.
     */
    private volatile boolean connectionAlreadyClosed;

    public JMXRMIConnectionAdapterImpl(
            IConnectionConfiguration connectionConfigurationContext) {
        if (connectionConfigurationContext == null)
            throw new IllegalArgumentException("Connection can not be null!");
        this.connectionConfigurationContext = connectionConfigurationContext;
        this.connectionAlreadyClosed = false;

    }

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
    public synchronized void openConnection() throws MalformedURLException,
            IOException, AuthenticationException {

        this.createJMXConnector();

        try {
            log.info("Connecting to " + connectionConfigurationContext);
            connector.connect();
            addConnectionNotificationListener();
            connectionAlreadyClosed = false;
        } catch (IllegalArgumentException ile) {
            log.debug("Authentication failed", ile);
            throw new AuthenticationException();
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            log.error(e);
            throw new IOException(e.getCause());
        }
    }

    /**
     * Closes the physical JMX connection silently.
     */
    public synchronized boolean closeConnection() {
        if (connector == null) {
            log.error("Cannot close a connection wiht a null connector");
            return false;
        }
        try {
            connector.close();
            connector = null;
        } catch (Throwable ioe) {
            log.error("Cannot close the connection", ioe);
        }
        // everything necessary has been done.
        return true;
    }

    protected void createJMXConnector() throws MalformedURLException,
            IOException {
        String urlString = createConnectionURL();
        JMXServiceURL url = new JMXServiceURL(urlString);
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        boolean isUsiingAuthentication = connectionConfigurationContext
                .getAuthenticationCredentials().isValid();
        if (isUsiingAuthentication) {
            // put the username and password credentials
            env.put(JMXConnector.CREDENTIALS, connectionConfigurationContext
                    .getAuthenticationCredentials().toStringArray());
            log.info("Creating JMX connection with credentials.");
        }
        if (isUsiingAuthentication) {
            connector = JMXConnectorFactory.newJMXConnector(url, env);
        } else {
            log.info("Creating JMX connection without credentials.");
            connector = JMXConnectorFactory.newJMXConnector(url, null);
        }
    }

    /**
     * Returns the URL to which the JMXConnectorFactory will connect.
     * 
     * @return
     */
    @Override
    public String createConnectionURL() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("service:jmx:rmi:///jndi/rmi://");
        buffer.append(connectionConfigurationContext.getHost());
        buffer.append(":");
        buffer.append(connectionConfigurationContext.getPort());
        buffer.append("/jmxrmi");
        return buffer.toString();
    }

    /**
     * Gets all the beans from the connection
     * 
     * @param connectionConfiguration
     * @return
     */
    @Override
    public synchronized <T, N extends NotificationListener> T loadProxyForClass(
            String nameURI, Class<T> proxyClass, N notificationListener)
            throws IOException, MalformedObjectNameException,
            InstanceNotFoundException {
        if (connector == null)
            return null;

        log.info("Requesting MBean for connection "
                + connectionConfigurationContext);
        if (nameURI == null || nameURI.length() == 0) {
            log.error("Requesting MBean, but the nameURI is null!");
            return null;
        }
        T proxyInstance = null;
        // cache the listener and use it for later use, will be used when the
        // connection is closed
        ObjectName objectName = new ObjectName(nameURI);

        MBeanServerConnection mbeanConnection = connector
                .getMBeanServerConnection();
        if (notificationListener != null) {
            mbeanConnection.addNotificationListener(objectName,
                    notificationListener, null, null);
            connector.addConnectionNotificationListener(notificationListener,
                    null, null);
        }
        proxyInstance = JMX.newMBeanProxy(mbeanConnection, objectName,
                proxyClass, true);
        return proxyInstance;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.connection.impl.IJMXConnectionAdapter#getMBeanServerConnection()
     */
    @Override
    public MBeanServerConnection getMBeanServerConnection() throws IOException {
        if (connector == null)
            throw new IOException("The connection is not connected");
        else
            return connector.getMBeanServerConnection();
    }

    /**
     * @see javax.management.NotificationListener#handleNotification(javax.management.Notification,
     *      java.lang.Object)
     */
    @Override
    public void handleNotification(Notification notification, Object handback) {
        // the connection has gone offline. remove it from the repository
        if (notification instanceof JMXConnectionNotification) {
            if (notification.getType().equals(JMXConnectionNotification.FAILED)) {
                log
                        .debug("Receive JMX notification JMXConnectionNotification.FAILED!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.CLOSED)) {
                log
                        .debug("Receive JMX notification JMXConnectionNotification.CLOSED!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.NOTIFS_LOST)) {
                log
                        .debug("Receive JMX notification JMXConnectionNotification.NOTIFS_LOST!");
            } else if (notification.getType().equals(
                    JMXConnectionNotification.OPENED)) {
                log
                        .debug("Receive JMX notification JMXConnectionNotification.OPENED!");
                return;
            }
            if (connectionAlreadyClosed == false) {
                connectionAlreadyClosed = true;
                // provide the connection manager with notification
                getJMXConnectionManager().closeConnectionConfiguration(
                        connectionConfigurationContext);

            }

        }

    }

    private void addConnectionNotificationListener() throws IOException,
            RuntimeException, MalformedObjectNameException,
            InstanceNotFoundException {
        ObjectName runtimeObjectName = new ObjectName("java.lang:type=Memory");
        MBeanServerConnection mbeanConnection = connector
                .getMBeanServerConnection();
        mbeanConnection.addNotificationListener(runtimeObjectName, this, null,
                null);
        connector.addConnectionNotificationListener(this, null, null);
    }

    /**
     * @see org.openehealth.ipf.platform.manager.connection.impl.IJMXConnectionAdapter#getConnectionConfigurationContext()
     */
    @Override
    public IConnectionConfiguration getConnectionConfigurationContext() {
        return connectionConfigurationContext;
    }

    public JMXConnector getConnector() {
        return connector;
    }

    public IJMXConnectionManager getJMXConnectionManager() {
        return Activator.getConnectionManager();
    }
}
