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
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.naming.AuthenticationException;
import javax.naming.ServiceUnavailableException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;

/**
 * IConnection manager implementation.
 * 
 * @see IJMXConnectionManager
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionManagerImpl extends java.util.Observable implements
        IJMXConnectionManager {

    private final List<IConnectionConfiguration> connectedConnectionConfigurations;

    private final Log log = LogFactory.getLog(JMXConnectionManagerImpl.class);

    private final TreeMap<IConnectionConfiguration, IJMXConnectionAdapter> adapters;

    private ConnectionConfigurationRepository repository;

    public JMXConnectionManagerImpl(ConnectionConfigurationRepository repository) {
        // make not copy at this time
        this.repository = repository;
        this.connectedConnectionConfigurations = new Vector<IConnectionConfiguration>();
        this.adapters = new TreeMap<IConnectionConfiguration, IJMXConnectionAdapter>();
        this.addAdapters(repository.getConnectionConfigurations());
    }

    /**
     * Constructs a connection Manager with predefined list of connections.
     * 
     * @param connectionConfigurationsSorted
     */
    public JMXConnectionManagerImpl(
            Set<IConnectionConfiguration> connectionConfigurationsSorted) {
        this(new ConnectionConfigurationRepository());
        List<IConnectionConfiguration> connectionConfigurationsAsList = new ArrayList<IConnectionConfiguration>();
        connectionConfigurationsAsList.addAll(connectionConfigurationsSorted);
        addAdapters(connectionConfigurationsAsList);
        repository.addAll(connectionConfigurationsSorted);
    }

    /**
     * Adds adapters
     * 
     * @param connectionConfigurationsSorted
     */
    private void addAdapters(
            List<IConnectionConfiguration> connectionConfigurationsSorted) {
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurationsSorted) {
            adapters.put(connectionConfiguration,
                    new JMXRMIConnectionAdapterImpl(connectionConfiguration));
        }
    }

    @Override
    public synchronized List<IConnectionConfiguration> getConnectionConfigurations() {
        return repository.getConnectionConfigurations();
    }

    /**
     * Currently used only for mock purposes.
     * 
     * @param connectionConfiguration
     * @param adapter
     */
    public synchronized void addConnection(
            IJMXConnectionAdapter connectionAdapter) {
        IConnectionConfiguration connectionConfiguration = connectionAdapter
                .getConnectionConfigurationContext();
        if (connectionConfiguration == null)
            return;
        repository.addConnectionConfiguration(connectionConfiguration);

        if (!adapters.containsKey(connectionConfiguration)) {
            // do not save the given connection, save a copy instead
            adapters.put((IConnectionConfiguration) connectionConfiguration
                    .clone(), connectionAdapter);
            log.info("Connection " + connectionConfiguration
                    + " added to the connection repository.");

        }
        ConnectionEvent event = new ConnectionEvent(connectionConfiguration,
                ConnectionEvent.CONNECTION_ADDED);
        // marks that the repository has been changed, we will have to notify
        // the
        // listeners then
        this.setChanged();
        log.info("Firing Connection CONNECTION_ADDED event.");
        this.notifyObservers(event);
    }

    @Override
    /*
     * Adds a connection with a JMXRMIConnectionAdapterImpl adapter.
     */
    public synchronized void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            IJMXConnectionAdapter connectionAdapter = new JMXRMIConnectionAdapterImpl(
                    connectionConfiguration);
            addConnection(connectionAdapter);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.openehealth.ipf.platform.manager.connection.IConnectionManager#
     * removeConnection
     * (org.openehealth.ipf.platform.manager.connection.IConnection)
     */
    public synchronized void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration != null) {
            if (!repository
                    .isConnectionConfigurationExisting(connectionConfiguration)) {
                return;
            }
            this.repository
                    .removeConnectionConfiguration(connectionConfiguration);
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                connectedConnectionConfigurations
                        .remove(connectionConfiguration);
            }

            if (adapters.containsKey(connectionConfiguration)) {
                adapters.remove(connectionConfiguration);
            }

            IConnectionConfiguration connectionConfigurationClone = (IConnectionConfiguration) connectionConfiguration
                    .clone();

            if (isConnected(connectionConfiguration)) {
                // does not throw event
                closeConnectionConfiguration(connectionConfigurationClone);
            }
            ConnectionEvent event = new ConnectionEvent(
                    connectionConfigurationClone,
                    ConnectionEvent.CONNECTION_REMOVED);
            // marks that the repository has been changed, we will have to
            // notify
            // the
            // listeners then
            this.setChanged();
            log.info("Firing Connection CONNECTION_REMOVED event.");
            this.notifyObservers(event);
        }
    }

    private void connectionConfigurationStatusChanged(
            IConnectionConfiguration connectionConfiguration, boolean connected) {
        if (connectionConfiguration == null)
            return;

        ConnectionEvent event;
        // do not send this connection. send only a clone so that the repository
        // does
        // not expose its
        // real data.
        IConnectionConfiguration connectionConfigurationClone = (IConnectionConfiguration) connectionConfiguration
                .clone();
        if (connected) {
            event = new ConnectionEvent(connectionConfigurationClone,
                    ConnectionEvent.JMX_CONNECTION_OPEN);
            // cleanup
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                log
                        .error("Connected Connections already contains the connection with status connected!");
            } else {
                this.connectedConnectionConfigurations
                        .add((IConnectionConfiguration) connectionConfiguration
                                .clone());
            }
        } else {
            // cleanup
            if (connectedConnectionConfigurations
                    .contains(connectionConfiguration)) {
                connectedConnectionConfigurations
                        .remove(connectionConfiguration);
            }
            event = new ConnectionEvent(connectionConfigurationClone,
                    ConnectionEvent.JMX_CONNECTION_CLOSED);
        }

        this.setChanged();
        this.notifyObservers(event);
    }

    public synchronized boolean isConnected(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null)
            return false;
        return connectedConnectionConfigurations
                .contains(connectionConfiguration);
    }

    @Override
    public synchronized boolean isConnectionNameInUse(String connectionName) {
        if (connectionName == null || connectionName.length() == 0)
            return false;
        return repository.isConnectionNameInUse(connectionName);
    }

    public synchronized void openConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration)
            throws MalformedURLException, IOException, AuthenticationException {
        if (connectionConfiguration == null)
            return;
        try {
            IJMXConnectionAdapter adapter = adapters
                    .get(connectionConfiguration);
            if (adapter == null) {
                log.error("The connection has no registered adapter");
                return;
            }
            adapter.openConnection();
            this.connectionConfigurationStatusChanged(connectionConfiguration,
                    true);
        } catch (AuthenticationException t) {
            throw t;
        } catch (MalformedURLException mfe) {
            throw mfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable t) {
            handleExpcetion(connectionConfiguration, t);
        }
    }

    public synchronized void closeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        // make the check in the method
        if (closeConnectionConfigurationSilently(connectionConfiguration)) {
            // throw event
            this.connectionConfigurationStatusChanged(connectionConfiguration,
                    false);
        }

    }

    /**
     * Exposed only to the bundle.
     */
    public synchronized void closeAllConnectionsSilently() {

        // avoid concurent modification...
        for (int t = connectedConnectionConfigurations.size() - 1; t >= 0; t--) {
            this
                    .closeConnectionConfigurationSilently(connectedConnectionConfigurations
                            .get(t));
        }
        // WARNING!!!
        // if (the connecon configuration has already been deleted from
        // the JMX
        // notification forwarder thread, do not delete it.
        for (int t = connectedConnectionConfigurations.size() - 1; t >= 0; t--) {
            connectedConnectionConfigurations.remove(t);
        }

    }

    /**
     * Delegates the close to the connection adapter.
     * 
     * @param connectionConfiguration
     * @return
     */
    private synchronized boolean closeConnectionConfigurationSilently(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null)
            return false;
        IJMXConnectionAdapter adapter = adapters.get(connectionConfiguration);
        if (adapter == null) {
            log.error("The connection has no registered adapter");
            return false;
        }
        // makes the null checks
        return adapter.closeConnection();

    }

    @Override
    public synchronized <T, N extends NotificationListener> T loadProxyForClass(
            IConnectionConfiguration connectionConfiguration, String nameURI,
            Class<T> proxyClass, N notificationListener) throws IOException,
            MalformedObjectNameException, InstanceNotFoundException {
        if (connectionConfiguration == null)
            return null;
        IJMXConnectionAdapter adapter = adapters.get(connectionConfiguration);
        if (adapter == null)
            throw new IOException("Cannot load Proxy");
        return adapter.loadProxyForClass(nameURI, proxyClass,
                notificationListener);

    }

    @Override
    public synchronized MBeanServerConnection getMBeanServerConnectionConfigurationAdapter(
            IConnectionConfiguration connectionConfiguration)
            throws IOException {
        if (connectionConfiguration == null)
            throw new IllegalArgumentException(
                    "Cannot reach the MBean server of a null connection");
        IJMXConnectionAdapter connectionAdapter = adapters
                .get(connectionConfiguration);
        if (connectionAdapter == null)
            throw new IOException(
                    "The connection adapter do not exist. The connection is probably removed!");
        return connectionAdapter.getMBeanServerConnection();

    }

    @Override
    public synchronized IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName) {
        return repository.getConnectionConfigurationForName(connectionName);
    }

    private void handleExpcetion(
            IConnectionConfiguration connectionConfiguration, Throwable t) {
        Throwable clause = t.getCause();
        if (clause instanceof ConnectException) {
            closeConnectionConfiguration(connectionConfiguration);
        } else if (clause instanceof ServiceUnavailableException) {
            closeConnectionConfiguration(connectionConfiguration);
        } else {
            log.error("Cannot handle exception", t);
            closeConnectionConfiguration(connectionConfiguration);
        }
    }

    public ConnectionConfigurationRepository getRepository() {
        return repository;
    }

    public void setRepository(ConnectionConfigurationRepository repository) {
        this.repository = repository;
    }
}
