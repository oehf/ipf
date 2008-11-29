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
package org.openehealth.ipf.platform.manager.connection.ui.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.SessionSafeWorkerJob;
import org.openehealth.ipf.platform.manager.connection.ui.utils.messages.Messages;

/**
 * Executes a close connection job in a UI thread.
 * <p>
 * 
 * @author Mitko Kolev
 */
public class CloseJMXConnectionJob extends SessionSafeWorkerJob {

    private final IJMXConnectionManager jMXConnectionManager;

    private final IConnectionConfiguration connectionConfiguration;

    private final static String closeConnectionResourceKey = "DisconnectHandler.closeConnection";

    private final static String closeConnectionFailedResourceKey = "DisconnectHandler.closeConnectionFailed";

    private final static Log log = LogFactory
            .getLog(CloseJMXConnectionJob.class);

    public CloseJMXConnectionJob(Display display,
            IJMXConnectionManager jMXConnectionManager,
            IConnectionConfiguration connectionConfiguration) {
        super(display, Messages.getLabelString(closeConnectionResourceKey)
                + connectionConfiguration.getHost() + ":"
                + connectionConfiguration.getPort());
        this.connectionConfiguration = connectionConfiguration;
        this.jMXConnectionManager = jMXConnectionManager;
        this.setUser(true);
    }

    @Override
    public synchronized IStatus run(IProgressMonitor monitor) {
        log.info("DisconnectJob started for connection "
                + connectionConfiguration);
        try {
            monitor.beginTask(this.getName(), 1);
            jMXConnectionManager
                    .closeConnectionConfiguration(connectionConfiguration);
            return Status.OK_STATUS;
        } catch (Exception e) {
            return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages
                    .getLabelString(closeConnectionFailedResourceKey)
                    + " " + connectionConfiguration.toString() + ".");
        } finally {
            monitor.done();
        }
    }
}