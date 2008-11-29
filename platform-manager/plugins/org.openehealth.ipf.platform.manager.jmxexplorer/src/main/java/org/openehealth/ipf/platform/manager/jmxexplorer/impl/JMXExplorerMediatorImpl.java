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
package org.openehealth.ipf.platform.manager.jmxexplorer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.management.MBeanServerConnection;

import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;

/**
 * Mediates the communication between the connection module and the JMX module.
 * 
 * <li>Receives events from the connection module and notifies its observers
 * with those events connection changes</li> <li>Receives events from the JMX
 * connection facades, and notifies its listeners with those events</li>
 * 
 * 
 * Notifies it Observers with ConnectionEvent or with JMXEvent. Adapts the
 * IConnection to a new MBeanServerConnectionFacadeImpl. Does not use cache for
 * the facades.
 * 
 * @see IMBeanServerConnectionFacade
 * @author Mitko Kolev
 */
public class JMXExplorerMediatorImpl extends Observable implements
        IJMXExplorerMediator {

    private final IJMXConnectionManager jMXConnectionManager;

    public JMXExplorerMediatorImpl(IJMXConnectionManager jMXConnectionManager) {
        this.jMXConnectionManager = jMXConnectionManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator
     * #getConnections()
     */
    @Override
    public List<IConnectionConfiguration> getOpenConnectionConfigurations() {
        List<IConnectionConfiguration> connectedConnectionConfigurations = new ArrayList<IConnectionConfiguration>();
        for (IConnectionConfiguration connectionConfiguration : jMXConnectionManager
                .getConnectionConfigurations()) {
            if (jMXConnectionManager.isConnected(connectionConfiguration)) {
                connectedConnectionConfigurations.add(connectionConfiguration);
            }
        }
        return connectedConnectionConfigurations;
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ConnectionEvent) {
            ConnectionEvent event = (ConnectionEvent) arg;
            if (event.getType() == ConnectionEvent.JMX_CONNECTION_OPEN) {
                // udpate
                notifyObserversWithConnectionEvent(event);
            } else if (event.getType() == ConnectionEvent.JMX_CONNECTION_CLOSED) {
                notifyObserversWithConnectionEvent(event);

            } else if (event.getType() == ConnectionEvent.CONNECTION_REMOVED) {
                notifyObserversWithConnectionEvent(event);
            }
        }
    }

    /**
     * 
     * Does not casche the adapted instances.
     * 
     * @param connectionConfiguration
     *            the Connection for which a facade is requested
     * @return the newly created facade.
     * @throws IOException
     *             when the MBeanServerConnection cannot be used
     */
    @Override
    public IMBeanServerConnectionFacade getMBeanServerConnectionConfigurationFacade(
            IConnectionConfiguration connectionConfiguration)
            throws IOException {

        MBeanServerConnection mBeanServerConnection = jMXConnectionManager
                .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
        if (mBeanServerConnection == null) {
            throw new IOException("Connection is offline");
        }
        // pool of MBeanServerConnectionFacadeImpl instances is not required
        // here
        return new MBeanServerConnectionFacadeImpl(mBeanServerConnection, this);

    }

    private void notifyObserversWithConnectionEvent(ConnectionEvent event) {
        this.setChanged();
        this.notifyObservers(event);
    }

    protected void notifyObserversWithJMXEvent(JMXExplorerEvent event) {
        this.setChanged();
        this.notifyObservers(event);
    }

    /* (non-Javadoc)
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator#getConnectionConfigurations()
     */
    @Override
    public List<IConnectionConfiguration> getConnectionConfigurations() {
        return jMXConnectionManager.getConnectionConfigurations();
    }
}
