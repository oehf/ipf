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

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepositoryAbstractSerializer;
import org.openehealth.ipf.platform.manager.connection.mock.ConnectionConfigurationRepositorylObjectStreamSerializerMock;

/**
 * @author Mitko Kolev
 */
public class ConnectionConfiguartionRepositoryAbstractSerializerTest extends
        TestCase {

    ConnectionConfigurationRepositoryAbstractSerializer serializer;

    @Override
    public void setUp() {
        serializer = new ConnectionConfigurationRepositorylObjectStreamSerializerMock();
    }

    @Test
    public void testConnectionRepositoryAbstractSerializerPathWithNoSeparatorAtTheEnd() {
        String folder = "usr" + File.separator + "home" + File.separator
                + "eclipse" + File.separator + "ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .indexOf(folder
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER) == 0);
    }

    public void testConnectionRepositoryAbstractSerializerPathWithSeparatorAtTheEnd() {
        String folder = "usr" + File.separator + "home" + File.separator
                + "eclipse" + File.separator + "ipm" + File.separator;

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .indexOf(folder
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER) == 0);
    }

    public void testConnectionRepositoryAbstractSerializerPathWithSeparatorAtTheEndWindows() {
        String folder = "file:/C:/eclipse/ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .indexOf("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER) == 0);
    }

    public void testConnectionRepositoryAbstractSerializerPathWithNoFile() {
        String folder = "C:/eclipse/ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .indexOf("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER) == 0);
    }

    public void testConnectionRepositoryAbstractSerializerPathWithFileSeparator() {
        String folder = "C:" + File.separator + "eclipse" + File.separator
                + "ipm";

        System.setProperty("osgi.install.area", folder);
        String globalPath = serializer.getSerializationGlobalPath();
        assertTrue(globalPath
                .indexOf("C:"
                        + File.separator
                        + "eclipse"
                        + File.separator
                        + "ipm"
                        + File.separator
                        + ConnectionConfigurationRepositoryAbstractSerializer.CONNECTIONS_LOCAL_FOLDER) == 0);
    }
}
