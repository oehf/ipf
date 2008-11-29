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
package org.openehealth.ipf.platform.manager.jmxexplorer.osgi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.impl.JMXExplorerMediatorImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Registers and consumes OSGI services.
 * 
 * @author Mitko Kolev
 */
public class Activator implements BundleActivator {

    private IJMXConnectionManager jMXConnectionManager;

    private ServiceTracker connectionManagerServiceTracker;

    private JMXExplorerMediatorImpl jmxExplorerMediator;

    private ServiceRegistration jmxExplorerMediatorRegistration;

    private final Log log = LogFactory.getLog(Activator.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext context) throws Exception {

        connectionManagerServiceTracker = new ServiceTracker(context,
                IJMXConnectionManager.class.getName(), null);
        connectionManagerServiceTracker.open();
        jMXConnectionManager = (IJMXConnectionManager) connectionManagerServiceTracker
                .getService();
        if (jMXConnectionManager == null) {
            log.error("Cannot find a connection manager service!");
        }
        jmxExplorerMediator = new JMXExplorerMediatorImpl(jMXConnectionManager);
        jMXConnectionManager.addObserver(jmxExplorerMediator);

        jmxExplorerMediatorRegistration = context
                .registerService(IJMXExplorerMediator.class.getName(),
                        jmxExplorerMediator, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        // close the service tracker

        jMXConnectionManager.deleteObserver(jmxExplorerMediator);
        jmxExplorerMediator.deleteObservers();

        connectionManagerServiceTracker.close();
        connectionManagerServiceTracker = null;
        jMXConnectionManager = null;
        jmxExplorerMediatorRegistration.unregister();
    }

}
