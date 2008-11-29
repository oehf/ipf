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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;

/**
 * This serializer of the reposirtory is intended to be used only when the
 * connection are serialized on disk.
 * 
 * @see IConnectionConfiguration
 * @see IJMXConnectionManager
 * 
 * @author Mitko Kolev
 */
public abstract class ConnectionConfigurationRepositoryAbstractSerializer {
    public static final String CONNECTIONS_LOCAL_FOLDER = "connections";

    public String getSerializationGlobalPath() {
        String installArea = System.getProperty("osgi.install.area");
        if (installArea != null) {
            if (installArea.startsWith("file:/")) {
                installArea = installArea.substring(6);
            }
            // convert the / character to a separator, to have the real path
            installArea = installArea.replace('/', File.separatorChar);

            if (!installArea.endsWith(File.separator))
                installArea = installArea + File.separator;

            installArea = installArea + "connections";
            File f = new File(installArea);
            if (!f.exists()) {
                f.mkdir();
            }
            return installArea + File.separator + "ConnectionRepository.obj";
        } else {
            return "";
        }
    }

    /**
     * Returns the stream where the repository file was serialized
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Returns the stream where the repository file should be serialized
     * 
     * @return
     */
    public abstract OutputStream getOutputStream() throws IOException;

    public abstract void write(ConnectionConfigurationRepository manager,
            Object params);

    /**
     * Initializes the connection repository.
     * 
     * @param params
     * @return
     */
    public abstract ConnectionConfigurationRepository read(Object params);
}
