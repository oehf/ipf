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
package org.openehealth.ipf.platform.manager.connection.osgi;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepositoryAbstractSerializer;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionRepositoryObjectStreamSerializer;
import org.openehealth.ipf.platform.manager.connection.impl.JMXConnectionManagerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Registers the OSGI appropriate services relevant for connections.
 * 
 * @author Mitko Kolev
 */
public class Activator implements BundleActivator {

    private final Log log = LogFactory.getLog(Activator.class);

    private ConnectionConfigurationRepositoryAbstractSerializer serializer;

    private static JMXConnectionManagerImpl connectionManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        serializer = new ConnectionRepositoryObjectStreamSerializer();
        log.info("Starting Connection bundle:");
        ConnectionConfigurationRepository repository = serializer.read(null);
        connectionManager = new JMXConnectionManagerImpl(repository);

        context.registerService(IJMXConnectionManager.class.getName(),
                connectionManager, new Hashtable<String, String>());
        log.info("ConnectionManager service registered.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        connectionManager.deleteObservers();
        connectionManager.closeAllConnectionsSilently();

        serializer.write(connectionManager.getRepository(), null);
        log.info("ConnectionManager service unregistered.");

    }

    /**
     * Used internally only in this plugin!
     * 
     * @return
     */
    public static JMXConnectionManagerImpl getConnectionManager() {
        return connectionManager;
    }
}
