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
package org.openehealth.ipf.platform.manager.jmxexplorer;

import java.io.IOException;
import java.util.List;
import java.util.Observer;

import javax.management.MBeanServerConnection;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Mediates functionality for JMX explorer. Observes the changes in the
 * connection state. Notifies it Observers with ConnectionEvent or with JMXEvent
 * 
 * @author Mitko Kolev
 */
public interface IJMXExplorerMediator extends Observer {
    /**
     * Returns facade for accessing the MBeanServerConnection. The facade
     * implements utility methods.s
     * 
     * @param connectionConfiguration
     *            the Connection for which the facade is requested.
     * @return the MBeanServerConnection which is associated with the connection
     * 
     * @throws IOException
     *             when the MBeanInfoConnection is not accessible
     * @see MBeanServerConnection
     */
    public IMBeanServerConnectionFacade getMBeanServerConnectionConfigurationFacade(
            IConnectionConfiguration connectionConfiguration)
            throws IOException;

    /**
     * Returns all available open connections.
     * 
     * @return unmodifiable list
     */
    public List<IConnectionConfiguration> getOpenConnectionConfigurations();
    
    /**
     * Returns all available connections.
     * 
     * @return unmodifiable list
     */
    public List<IConnectionConfiguration> getConnectionConfigurations();

    /**
     * The observer will be notified with JMXExplorer events and
     * ConnectionEvents
     * 
     * @param observer
     */
    public void addObserver(Observer observer);

    /**
     * The unregisters the given observer from the list of observers
     * 
     * @param observer
     */
    public void deleteObserver(Observer observer);

}
