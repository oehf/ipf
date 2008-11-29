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
package org.openehealth.ipf.platform.manager.connection.ui.osgi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Loads the appropriate OSGI services, needed by the plug-in.
 */
public class Activator extends AbstractUIPlugin {

    private final static Log log = LogFactory.getLog(Activator.class);

    // The plug-in ID
    public static final String PLUGIN_ID = "org.openehealth.ipf.platform.manager.connection.ui";

    // The shared instance
    static Activator plugin;

    private static ServiceReference connectionManagerReference;

    private static IJMXConnectionManager jMXConnectionManager;

    /**
     * The constructor
     */
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
        log.info("Starting Connection viewer bundle.");
        // cache the context

        plugin = this;
        connectionManagerReference = context
                .getServiceReference(IJMXConnectionManager.class.getName());
        jMXConnectionManager = (IJMXConnectionManager) context
                .getService(connectionManagerReference);
        if (jMXConnectionManager != null) {
            log.info("ConnectionManager instance cached.");
        } else {
            log.error("Cannot cache the ConnectionManager instance!");
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
        context.ungetService(connectionManagerReference);

        jMXConnectionManager = null;
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IJMXConnectionManager getJMXConnectionManager() {
        return jMXConnectionManager;
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

    /**
     * Returns the file where the memento of this module has to be saved
     * 
     * @return
     */
    public static File getStateFile() {
        IPath path = Activator.getDefault().getStateLocation();
        if (path == null) {
            return null;
        }
        path = path.append("Connection.xml");
        return path.toFile();
    }

    /**
     * Saves plug-in appropriate data of the XMLMemento.
     * 
     * @param memento
     */
    public static void saveMementoToFile(XMLMemento memento) {
        File stateFile = getStateFile();
        if (stateFile != null) {
            try {
                FileOutputStream stream = new FileOutputStream(stateFile);
                OutputStreamWriter writer = new OutputStreamWriter(stream,
                        "utf-8"); //$NON-NLS-1$
                memento.save(writer);
                writer.close();
            } catch (IOException ioe) {
                stateFile.delete();
            }
        }
    }
}
