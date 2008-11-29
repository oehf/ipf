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

import java.util.List;

import junit.framework.TestCase;

import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.platform.manager.connection.mock.ConnectionConfigurationRepositorylObjectStreamSerializerMock;

/**
 * Tests the connneciton repository functionality.
 * 
 * @author Mitko Kolev
 */
public class SerializableConnectionConfigurationRepositoryTest extends TestCase {

    ConnectionConfigurationRepository connectionConfigurationRepository;

    @Override
    public void setUp() throws Exception {
        connectionConfigurationRepository = new ConnectionConfigurationRepository();
    }

    public void testSerializeDeserializeRepository() {
        ConnectionConfigurationRepositorylObjectStreamSerializerMock serializer = new ConnectionConfigurationRepositorylObjectStreamSerializerMock();
        serializer.write(connectionConfigurationRepository, null);
        ConnectionConfigurationRepository serialized = serializer.read(null);
        List<IConnectionConfiguration> connectionConfigurationsSerialized = serialized
                .getConnectionConfigurations();
        List<IConnectionConfiguration> connectionConfigurationsOriginal = connectionConfigurationRepository
                .getConnectionConfigurations();

        for (int t = 0; t < connectionConfigurationsSerialized.size(); t++) {
            IConnectionConfiguration connectionConfigurationSerialized = connectionConfigurationsSerialized
                    .get(t);
            IConnectionConfiguration connectionConfigurationOriginal = connectionConfigurationsOriginal
                    .get(t);
            assertTrue(connectionConfigurationSerialized
                    .equals(connectionConfigurationOriginal));
        }
        for (int t = 0; t < connectionConfigurationsOriginal.size(); t++) {
            IConnectionConfiguration connectionConfigurationSerialized = connectionConfigurationsSerialized
                    .get(t);
            IConnectionConfiguration connectionConfigurationOriginal = connectionConfigurationsOriginal
                    .get(t);
            assertTrue(connectionConfigurationSerialized
                    .equals(connectionConfigurationOriginal));
        }
    }

}
