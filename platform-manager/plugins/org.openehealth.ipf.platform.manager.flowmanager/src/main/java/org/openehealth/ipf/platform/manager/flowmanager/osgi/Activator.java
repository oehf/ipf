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
package org.openehealth.ipf.platform.manager.flowmanager.osgi;

import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Plugin;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerApplicationControllerImpl;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerRepositorylImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Loads the appropriate OSGI services.
 * 
 * @author Mitko Kolev
 */
public class Activator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openehealth.ipf.platform.manager.flowmanager";

    // The shared instance
    private static Activator plugin;

    private static ServiceReference connectionManagerReference;

    private static IJMXConnectionManager jMXConnectionManager;

    private static IFlowManagerApplicationController flowManagerApplicationController;

    private final Log log = LogFactory.getLog(Activator.class);

    /**
     * The constructor
     */
    public Activator() {
    }

    static ServiceRegistration registration;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        log.info("Starting FlowManager Bundle");
        super.start(context);
        plugin = this;
        connectionManagerReference = context
                .getServiceReference(IJMXConnectionManager.class.getName());
        jMXConnectionManager = (IJMXConnectionManager) context
                .getService(connectionManagerReference);

        FlowManagerRepositorylImpl repository = new FlowManagerRepositorylImpl(
                jMXConnectionManager);
        flowManagerApplicationController = new FlowManagerApplicationControllerImpl(
                jMXConnectionManager, repository);

        if (jMXConnectionManager != null) {
            log
                    .info("Adding FlowManager Repository as a ConnectionController listener.");
            jMXConnectionManager.addObserver(flowManagerApplicationController);
        }
        registration = context.registerService(
                IFlowManagerApplicationController.class.getName(),
                flowManagerApplicationController, null);
        context.ungetService(connectionManagerReference);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        log
                .info("Removing FlowManager Repository from ConnectionController's list of listener.");
        jMXConnectionManager.deleteObserver(flowManagerApplicationController);
        if (flowManagerApplicationController.getFlowManagerRepository() instanceof Observable) {
            Observable om = (Observable) flowManagerApplicationController
                    .getFlowManagerRepository();
            if (om.countObservers() > 0) {
                log
                        .error("FlowManagerRepository: not all observers are unregistered!");
            }
        }

        plugin = null;
        super.stop(context);
        registration.unregister();

    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

}
