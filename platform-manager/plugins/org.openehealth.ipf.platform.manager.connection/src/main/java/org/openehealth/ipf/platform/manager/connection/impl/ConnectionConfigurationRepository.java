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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Repository for connections.
 * 
 * @author Mitko Kolev
 */
public class ConnectionConfigurationRepository {

    private final List<IConnectionConfiguration> connectionConfigurations;

    public ConnectionConfigurationRepository() {
        this.connectionConfigurations = new ArrayList<IConnectionConfiguration>();
    }

    /**
     * Initializes the repository with some connections.
     * 
     * @param connectionConfigurationsParams
     */
    public ConnectionConfigurationRepository(
            Set<IConnectionConfiguration> connectionConfigurationsParams) {
        this();
        if (connectionConfigurationsParams != null) {
            for (IConnectionConfiguration connectionConfiguration : connectionConfigurationsParams) {
                connectionConfigurations
                        .add((IConnectionConfiguration) connectionConfiguration
                                .clone());
            }
        }
    }

    /**
     * Returns all available connections in the connection repository.
     * 
     * @return
     */
    public List<IConnectionConfiguration> getConnectionConfigurations() {
        List<IConnectionConfiguration> connectionConfigurationClones = new ArrayList<IConnectionConfiguration>();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionConfigurationClones
                    .add((IConnectionConfiguration) connectionConfiguration
                            .clone());
        }
        return Collections.unmodifiableList(connectionConfigurationClones);
    }

    /**
     * Returns if a connection with name connectionName exists in the
     * repository.
     * 
     * @param connectionName
     * @return
     */
    public boolean isConnectionNameInUse(String connectionName) {
        // do not allow connections with empty names
        if (connectionName == null || connectionName.length() == 0)
            return false;
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            if (connectionConfiguration.getName().equals(connectionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the connection for given name. The relationship between
     * connection and names is 1:1 A connection name is considered unique in the
     * repository.
     * 
     * @param connectionName
     * @return the connection with connectionName
     */
    public IConnectionConfiguration getConnectionConfigurationForName(
            String connectionName) {
        if (connectionName == null || connectionName.length() == 0)
            return null;
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            if (connectionConfiguration.getName().equals(connectionName)) {
                return (IConnectionConfiguration) connectionConfiguration
                        .clone();
            }
        }
        return null;
    }

    /**
     * Adds a connection with a default IJMXConnectionAdapter implementation to
     * the repository.
     * 
     * @param observer
     */

    public void addConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null)
            return;
        if (connectionConfigurations.contains(connectionConfiguration)) {
            connectionConfigurations.remove(connectionConfiguration);
        }
        connectionConfigurations.add(connectionConfiguration);
    }

    public boolean isConnectionConfigurationExisting(
            IConnectionConfiguration connectionConfiguration) {
        return connectionConfigurations.contains(connectionConfiguration);
    }

    /**
     * Delegates the method to the repository. Notifies the listeners.
     * IOException is thrown, because the close method is called.
     * 
     * @param observer
     */
    public void removeConnectionConfiguration(
            IConnectionConfiguration connectionConfiguration) {

        if (connectionConfigurations.contains(connectionConfiguration)) {
            connectionConfigurations.remove(connectionConfiguration);
        }
    }

    /**
     * Adss the connection set to the repository.
     * 
     * @param connectionConfigurations
     */
    public void addAll(Set<IConnectionConfiguration> connectionConfigurations) {
        if (connectionConfigurations != null) {
            for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
                this.addConnectionConfiguration(connectionConfiguration);
            }
        }
    }

    /**
     * Adss the connection set to the repository.
     * 
     * @param connectionConfigurations
     */
    public void removeAll() {
        this.connectionConfigurations.clear();
    }
}
