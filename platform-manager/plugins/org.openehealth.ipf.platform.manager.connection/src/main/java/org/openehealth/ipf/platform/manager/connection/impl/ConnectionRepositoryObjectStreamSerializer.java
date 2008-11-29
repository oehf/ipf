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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * 
 * @see ConnectionConfigurationRepositoryAbstractSerializer
 * 
 * @author Mitko Kolev
 */
public class ConnectionRepositoryObjectStreamSerializer extends
        ConnectionConfigurationRepositoryAbstractSerializer {

    Log log = LogFactory
            .getLog(ConnectionRepositoryObjectStreamSerializer.class);

    @SuppressWarnings("unchecked")
    @Override
    public ConnectionConfigurationRepository read(Object params) {
        try {
            ObjectInputStream stream = new ObjectInputStream(getInputStream());
            TreeSet<IConnectionConfiguration> connectionConfigurationsSorted = (TreeSet<IConnectionConfiguration>) stream
                    .readObject();
            ConnectionConfigurationRepository repository = new ConnectionConfigurationRepository(
                    connectionConfigurationsSorted);
            return repository;

        } catch (FileNotFoundException e) {
            log.debug("Connection Repository does not exits", e.getCause());
        } catch (Exception e) {
            log.error("Cannot read the repository serialization file", e);
        }
        return new ConnectionConfigurationRepository();
    }

    @Override
    public void write(ConnectionConfigurationRepository repository,
            Object params) {
        List<IConnectionConfiguration> connectionConfigurations = repository
                .getConnectionConfigurations();
        // use LinkedHashMap to preserve the order of elements as specified by

        TreeSet<IConnectionConfiguration> connectionConfigurationsSorted = new TreeSet<IConnectionConfiguration>();
        connectionConfigurationsSorted.addAll(connectionConfigurations);

        ObjectOutputStream stream;
        try {
            stream = new ObjectOutputStream(getOutputStream());
            stream.writeObject(connectionConfigurationsSorted);
            stream.writeObject(connectionConfigurations);
            stream.flush();
        } catch (FileNotFoundException e) {
            log.error("Cannot write repository file", e);
        } catch (IOException e) {
            log.error("Cannot write repository file", e);
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(
                getSerializationGlobalPath()));
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new BufferedOutputStream(new FileOutputStream(this
                .getSerializationGlobalPath()));
    }
}
