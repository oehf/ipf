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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;

import junit.framework.TestCase;

import org.openehealth.ipf.platform.manager.connection.impl.JMXConnectionManagerImpl;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.platform.manager.connection.impl.JMXRMIConnectionAdapterImpl;
import org.openehealth.ipf.platform.manager.connection.mock.JMXConnectionManagerImplMock;

/**
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionManagerImplTest extends TestCase {

    private JMXConnectionManagerImpl connectionManager;

    @Override
    public void setUp() {

        connectionManager = new JMXConnectionManagerImplMock();
    }

    public void testAddConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "new", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager
                .addConnectionConfiguration((IConnectionConfiguration) null);

    }

    public void testAddConnectionRealAdapter() {
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "new", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnection(new JMXRMIConnectionAdapterImpl(
                connectionConfiguration));
        assertConnectionConfigurationAdded(connectionConfiguration);
    }

    private void assertConnectionConfigurationAdded(
            IConnectionConfiguration connectionConfiguration) {
        assertTrue(connectionManager.getConnectionConfigurations().contains(
                connectionConfiguration));
        // call open connection
        assertTrue(connectionManager
                .isConnectionNameInUse(connectionConfiguration.getName()));

        assertFalse(connectionManager.isConnected(connectionConfiguration));
        // should throw IOException here
        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    public void testRemoveConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "new", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager
                .removeConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationRemoved(connectionConfiguration);

        // must not yield exception
        connectionManager.removeConnectionConfiguration(null);

    }

    private void assertConnectionConfigurationRemoved(
            IConnectionConfiguration connectionConfiguration) {
        assertFalse(connectionManager.getConnectionConfigurations().contains(
                connectionConfiguration));
        // call open connection
        assertFalse(connectionManager
                .isConnectionNameInUse(connectionConfiguration.getName()));

        assertFalse(connectionManager.isConnected(connectionConfiguration));

        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException e) {
            assertTrue(true);
            return;
        }
        fail();
    }

    public void testOpenAllConnections() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);

        }
    }

    public void testOpenConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "testOpenConnection", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);

        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);

    }

    public void testOpenConnectionRealAdapter() throws Exception {
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "new", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnection(new JMXRMIConnectionAdapterImpl(
                connectionConfiguration));
        assertConnectionConfigurationAdded(connectionConfiguration);
        try {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            fail();
        } catch (IOException ioe) {
            assertTrue(true);
        } catch (Throwable t) {
            fail();
        }
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    private void assertConnectionConfigurationOpened(
            IConnectionConfiguration connectionConfiguration)
            throws IOException {
        assertTrue(connectionManager.isConnected(connectionConfiguration));
        MBeanServerConnection mbsc = connectionManager
                .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
        assertTrue(mbsc != null);
    }

    public void testCloseConnection() throws Exception {

        // the port is in the ConnectionRepositoryMock
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "testOpenConnection", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        connectionManager.addConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationAdded(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

        // one open and close
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);
        connectionManager.closeConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

        // 2 times close
        connectionManager.openConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationOpened(connectionConfiguration);
        connectionManager.closeConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);
        connectionManager.closeConnectionConfiguration(connectionConfiguration);
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    public void testCloseConnection2() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
        IConnectionConfiguration connectionConfigurationNonExisting = new ConnectionConfigurationImpl(
                "testOpenConnectionNotAdded", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        // nothing should be closed
        connectionManager
                .closeConnectionConfiguration(connectionConfigurationNonExisting);

        for (IConnectionConfiguration connectionConfiguration : connectionManager
                .getConnectionConfigurations()) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }

        connectionManager.closeConnectionConfiguration(null);

        for (IConnectionConfiguration connectionConfiguration : connectionManager
                .getConnectionConfigurations()) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
    }

    private void assertConnectionConfigurationClosed(
            IConnectionConfiguration connectionConfiguration) {
        assertFalse(connectionManager.isConnected(connectionConfiguration));
        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
            fail();
        } catch (IOException ioe) {
            assert (true);
            return;
        }
        fail();
    }

    public void testCloseAllConnectionsSilently() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            assertConnectionConfigurationOpened(connectionConfiguration);
        }
        connectionManager.closeAllConnectionsSilently();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertConnectionConfigurationClosed(connectionConfiguration);
        }
        // do it once again
        connectionManager.closeAllConnectionsSilently();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertConnectionConfigurationClosed(connectionConfiguration);
        }
    }

    public void testConstructor1() {
        ConnectionConfigurationRepository repo = new ConnectionConfigurationRepository();
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "testOpenConnection", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        repo.addConnectionConfiguration(connectionConfiguration);
        connectionManager = new JMXConnectionManagerImpl(repo);
        assertTrue(connectionManager.getConnectionConfigurations().size() == 1);
        assertTrue(connectionManager.getConnectionConfigurationForName(
                connectionConfiguration.getName()).equals(
                connectionConfiguration));
        assertConnectionConfigurationClosed(connectionConfiguration);

    }

    public void testConstructor2() {
        IConnectionConfiguration connectionConfiguration = new ConnectionConfigurationImpl(
                "testOpenConnection", "localhost",
                JMXConnectionManagerImplMock.JMX_MANAGEMENT_PORT);
        Set<IConnectionConfiguration> set = new TreeSet<IConnectionConfiguration>();
        set.add(connectionConfiguration);

        connectionManager = new JMXConnectionManagerImpl(set);
        assertTrue(connectionManager.getConnectionConfigurations().size() == 1);
        assertTrue(connectionManager.getConnectionConfigurationForName(
                connectionConfiguration.getName()).equals(
                connectionConfiguration));
        assertConnectionConfigurationClosed(connectionConfiguration);
    }

    public void testIsConnectionNameInUse() {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertTrue(connectionManager
                    .isConnectionNameInUse(connectionConfiguration.getName()));
        }
        assertFalse(connectionManager.isConnectionNameInUse("not-in-use"));
        assertFalse(connectionManager.isConnectionNameInUse(""));
        assertFalse(connectionManager.isConnectionNameInUse(null));
    }

    public void testGetMBeanServerConnection() throws Exception {
        List<IConnectionConfiguration> connectionConfigurations = connectionManager
                .getConnectionConfigurations();
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            connectionManager
                    .openConnectionConfiguration(connectionConfiguration);
        }
        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            assertTrue(connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration) != null);
        }
        connectionManager.closeAllConnectionsSilently();

        for (IConnectionConfiguration connectionConfiguration : connectionConfigurations) {
            try {
                connectionManager
                        .getMBeanServerConnectionConfigurationAdapter(connectionConfiguration);
                fail();
            } catch (IOException e) {
                assertTrue(true);
            } catch (Exception e) {
                fail();
            }
        }

        try {
            connectionManager
                    .getMBeanServerConnectionConfigurationAdapter(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

    }
}
