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
package org.openehealth.ipf.platform.manager.connection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Observer;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.naming.AuthenticationException;

/**
 * Manages the connection repository. Defines access method to the connection
 * store. Every connection in the repository has a repository-unique name.
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public interface IJMXConnectionManager {

    /**
     * Returns all available connections in the connection repository.
     * 
     * @return
     */
    public List<IConnectionConfiguration> getConnectionConfigurations();

    /**
     * Returns if the connection is connected.
     * 
     * @param connectionConfiguration
     * @return true if the connection is connected, false otherwise
     */
    public boolean isConnected(IConnectionConfiguration connectionConfiguration);

    /**
     * Every connection has its adapter. The method does not return null
     * 
     * @param connectionConfiguration
     *            the connection for which the adapter is requested
     * @return the adapter of the connection
     */
    public MBeanServerConnection getMBeanServerConnectionConfigurationAdapter(
            IConnectionConfiguration connectionConfiguration)
            throws IOException;

    /**
     * Returns if a connection with name connectionName exists in the
     * repository.
     * 
     * @param connectionName
     * @return
     */
    public boolean isConnectionNameInUse(String connectionName);

    /**
     * Returns the connection for given name. The relationship between
     * connection and names is 1:1 A connection name is considered unique in the
     * repository.
     * 
     * @param connectionName
     * @return the connection with connectionName
     */
    public IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName);

    /**
     * Loads a proxy for the
     * <code>proxyClass<code> and registers the notification listener if any.
     * 
     * @param <T>
     * @param <N>
     * @param connectionConfiguration
     * @param nameURI
     * @param proxyClass
     * @param notificationListener
     * @return
     * @throws IOException
     * @throws MalformedObjectNameException
     * @throws InstanceNotFoundException
     */
    public <T, N extends NotificationListener> T loadProxyForClass(
            IConnectionConfiguration connectionConfiguration, String nameURI,
            Class<T> proxyClass, N notificationListener) throws IOException,
            MalformedObjectNameException, InstanceNotFoundException;

    /**
     * Adds a connection with a default IJMXConnectionAdapter implementation to
     * the repository.
     * 
     * @param observer
     */

    public void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration);

    /**
     * Delegates the method to the repository. Notifies the listeners.
     * IOException is thrown, because the close method is called.
     * 
     * @param observer
     */
    public void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration);

    /**
     * Opens a connection synchronously
     * 
     * @param connectionConfiguration
     * @throws IOException
     *             if the open has failed
     */
    public void openConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration)
            throws MalformedURLException, IOException, AuthenticationException;

    /**
     * Closes a connection synchronously
     * 
     * @param connectionConfiguration
     * @throws IOException
     */
    public void closeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration);

    /**
     * The added Observer will be notified with ConnectionEvent
     * 
     * @param observer
     */
    public void addObserver(Observer observer);

    /**
     * Deletes the observer from the list of observers
     * 
     * @param observer
     */
    public void deleteObserver(Observer observer);
}
