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

import javax.naming.AuthenticationException;

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
 * Executes an open connection job in a UI thread.
 * 
 * @author Mitko Kolev
 */
public class OpenJMXConnectionJob extends SessionSafeWorkerJob {

    private final static String openConnectionResourceKey = "ConnectHandler.openConnection";

    private final static String openConnectionFailedResourceKey = "ConnectHandler.openConnectionFailed";

    private final static String openConnectionCredentialsFailedResourceKey = "ConnectHandler.openConnectionCredentialsFailed";

    private final static String openConnectionCannotReachServerResourceKey = "ConnectHandler.openConnectionReachServerFailed";

    private final IJMXConnectionManager jMXConnectionManager;

    private final IConnectionConfiguration connectionConfiguration;

    private final static Log log = LogFactory
            .getLog(OpenJMXConnectionJob.class);

    /**
     * Constructs a job
     * 
     * @param display
     * @param jMXConnectionManager
     * @param connectionConfiguration
     */
    public OpenJMXConnectionJob(Display display,
            IJMXConnectionManager jMXConnectionManager,
            IConnectionConfiguration connectionConfiguration) {
        super(display, Messages.getLabelString(openConnectionResourceKey)
                + connectionConfiguration.getHost() + ":"
                + connectionConfiguration.getPort());
        this.connectionConfiguration = connectionConfiguration;
        this.jMXConnectionManager = jMXConnectionManager;
        this.setUser(true);
    }

    @Override
    public IStatus run(IProgressMonitor monitor) {
        String errorMessage = Messages
                .getLabelString(openConnectionFailedResourceKey);
        try {
            monitor.beginTask(Messages
                    .getLabelString(openConnectionResourceKey)
                    + " " + connectionConfiguration.getName(),
                    IProgressMonitor.UNKNOWN);
            log.info("OpenJob started for connection "
                    + connectionConfiguration);
            jMXConnectionManager
                    .openConnectionConfiguration(connectionConfiguration);
            return new Status(Status.OK, Activator.PLUGIN_ID, errorMessage);
        } catch (AuthenticationException ae) {
            log
                    .warn("OpenJob failed with AuthenticationException for connection: "
                            + connectionConfiguration);
            errorMessage = Messages
                    .getLabelString(openConnectionCredentialsFailedResourceKey);
        } catch (Throwable ioe) {
            log.warn("OpenJob failed with Throwable for connection: "
                    + connectionConfiguration);
            errorMessage = Messages
                    .getLabelString(openConnectionCannotReachServerResourceKey)
                    + " " + connectionConfiguration.toString() + ".";
        } finally {
            monitor.done();
        }
        return new Status(Status.ERROR, Activator.PLUGIN_ID, errorMessage);

    }
}
