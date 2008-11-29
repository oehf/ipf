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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openehealth.ipf.platform.manager.jmxexplorer.ui";

    // The shared instance
    private static Activator plugin;

    private IJMXExplorerMediator jmxExplorerMediator;

    private ServiceTracker jmxExplorrerMediatorTracker;

    private final Log log = LogFactory.getLog(Activator.class);

    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext context) throws Exception {

        super.start(context);
        plugin = this;

        // create a tracker and track the service
        jmxExplorrerMediatorTracker = new ServiceTracker(context,
                IJMXExplorerMediator.class.getName(), null);
        jmxExplorrerMediatorTracker.open();
        jmxExplorerMediator = (IJMXExplorerMediator) jmxExplorrerMediatorTracker
                .getService();
        if (jmxExplorerMediator == null) {
            log.error("Cannot find a connection manager service!");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        jmxExplorrerMediatorTracker.close();
        jmxExplorrerMediatorTracker = null;
        jmxExplorerMediator = null;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public IJMXExplorerMediator getJMXExplorerMediator() {
        return jmxExplorerMediator;
    }
}
