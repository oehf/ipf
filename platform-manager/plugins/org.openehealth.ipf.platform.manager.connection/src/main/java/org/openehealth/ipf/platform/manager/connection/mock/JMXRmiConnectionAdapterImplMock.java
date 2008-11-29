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
package org.openehealth.ipf.platform.manager.connection.mock;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.impl.JMXConnectionManagerImpl;
import org.openehealth.ipf.platform.manager.connection.impl.ConnectionConfigurationRepository;
import org.openehealth.ipf.platform.manager.connection.impl.JMXRMIConnectionAdapterImpl;

/**
 * @see JMXRmiConnectionAdapterImpl
 * 
 * @author Mitko Kolev
 */
public class JMXRmiConnectionAdapterImplMock extends
        JMXRMIConnectionAdapterImpl {

    private static final long serialVersionUID = 4971934396142861172L;

    public JMXRmiConnectionAdapterImplMock(
            IConnectionConfiguration connectionConfiguration) {
        super(connectionConfiguration);
    }

    @Override
    public String createConnectionURL() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://"); //$NON-NLS-1$
            JMXConnectorServer cs = JMXConnectorServerFactory
                    .newJMXConnectorServer(url, null, mbs);
            cs.start();
            return cs.getAddress().toString();
        } catch (Exception ioe) {
            return "URL not available";
        }
    }

    @Override
    public IJMXConnectionManager getJMXConnectionManager() {
        IJMXConnectionManager manager = new JMXConnectionManagerImpl(
                new ConnectionConfigurationRepository()) {
            @Override
            public void closeConnectionConfiguration(
                    IConnectionConfiguration connectionConfiguration) {
                JMXRmiConnectionAdapterImplMock.this.closeConnection();
            }
        };
        manager.addConnectionConfiguration(this
                .getConnectionConfigurationContext());
        return manager;
    }
}
