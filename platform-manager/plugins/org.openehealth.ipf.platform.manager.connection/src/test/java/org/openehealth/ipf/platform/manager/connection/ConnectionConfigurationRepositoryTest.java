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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.junit.Test;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepository;

/**
 * @author Mitko Kolev
 */
public class ConnectionConfigurationRepositoryTest extends TestCase {

    private ConnectionConfigurationRepository connectionConfigurationRepository;
    private final static int JMX_PORT_FOR_TESTING = 12222;

    @Override
    public void setUp() throws IOException {

        connectionConfigurationRepository = new ConnectionConfigurationRepository();

    }

    @Override
    public void tearDown() throws IOException {

    }

    public void testConstructor() {
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurations() != null);
        Set<IConnectionConfiguration> set = new TreeSet<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            set.add(connectionConfiguration);
        }

        connectionConfigurationRepository = new ConnectionConfigurationRepository(
                set);
        for (IConnectionConfiguration connectionConfiguration : set) {
            assertTrue(connectionConfigurationRepository
                    .getConnectionConfigurations().contains(
                            connectionConfiguration));
        }

        connectionConfigurationRepository = new ConnectionConfigurationRepository(
                null);
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurations() != null);
    }

    @Test
    public void testAddConnection() {
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            assertTrue(connectionConfigurationRepository
                    .isConnectionNameInUse("connection" + t));
        }

        // add once again the same
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurations().size() == 10);

        connectionConfigurationRepository.addConnectionConfiguration(null);
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurations().size() == 10);
    }

    @Test
    public void testAddRemoveConnection() {
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            connectionConfigurationRepository
                    .removeConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            assertFalse(connectionConfigurationRepository
                    .isConnectionNameInUse("connection" + t));
        }

        IConnectionConfiguration connectionConfigurationNonExisting = new ConnectionConfigurationImpl(
                "connectionNonExisitng", "localhost", JMX_PORT_FOR_TESTING);
        connectionConfigurationRepository
                .removeConnectionConfiguration(connectionConfigurationNonExisting);

    }

    @Test
    public void testGetConnections() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        List<IConnectionConfiguration> connectionConfigurations = connectionConfigurationRepository
                .getConnectionConfigurations();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = connectionConfigurations
                    .get(t);
            assertTrue(connectionConfiguration.getName().equals(
                    "connection" + t));
            // assert that we do not become a repository object.
            assertFalse(added == connectionConfiguration);
        }
    }

    public void testNullAware() {
        assertFalse(connectionConfigurationRepository
                .isConnectionConfigurationExisting(null));
        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(null));
        assertFalse(connectionConfigurationRepository.isConnectionNameInUse(""));

        IConnectionConfiguration connectionConfigurationNonExisting = new ConnectionConfigurationImpl(
                "connectionNonExisitng", "localhost", JMX_PORT_FOR_TESTING);
        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(connectionConfigurationNonExisting
                        .getName()));

        assertFalse(connectionConfigurationRepository
                .isConnectionNameInUse(null));
        assertFalse(connectionConfigurationRepository.isConnectionNameInUse(""));

        assertFalse(connectionConfigurationRepository
                .isConnectionConfigurationExisting(connectionConfigurationNonExisting));
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurationForName("non-existing-connection") == null);
        // test remove null
        connectionConfigurationRepository.removeConnectionConfiguration(null);

        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurationForName(null) == null);
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurationForName("") == null);

        connectionConfigurationRepository.addAll(null);
    }

    public void testGetConnectionForName() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = added.get(t);
            IConnectionConfiguration repoConnectionConfiguration = connectionConfigurationRepository
                    .getConnectionConfigurationForName(connectionConfiguration
                            .getName());
            assertTrue(repoConnectionConfiguration
                    .equals(connectionConfiguration));
            assertFalse(repoConnectionConfiguration == connectionConfiguration);
        }

    }

    public void testRemoveAll() {
        List<IConnectionConfiguration> added = new ArrayList<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            added.add(connectionConfiguration);
            connectionConfigurationRepository
                    .addConnectionConfiguration(connectionConfiguration);
        }
        connectionConfigurationRepository.removeAll();
        assertTrue(connectionConfigurationRepository
                .getConnectionConfigurations().size() == 0);
    }

    public void testAddSet() {
        Set<IConnectionConfiguration> added = new TreeSet<IConnectionConfiguration>();
        for (int t = 0; t < 10; t++) {
            IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                    "connection" + t, "localhost", JMX_PORT_FOR_TESTING);
            added.add(connectionConfiguration);
        }
        connectionConfigurationRepository.addAll(added);
        List<IConnectionConfiguration> connectionConfigurations = connectionConfigurationRepository
                .getConnectionConfigurations();
        assertTrue(connectionConfigurations.size() == added.size());
        for (int t = 0; t < connectionConfigurations.size(); t++) {
            assertTrue(added.contains(connectionConfigurations.get(t)));
        }
    }
}
