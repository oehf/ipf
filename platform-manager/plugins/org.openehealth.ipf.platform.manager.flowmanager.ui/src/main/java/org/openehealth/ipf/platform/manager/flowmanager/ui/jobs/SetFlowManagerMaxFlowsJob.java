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
package org.openehealth.ipf.platform.manager.flowmanager.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.SessionSafeWorkerJob;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * 
 * UI job to execute setMaxFlows
 * 
 * @see org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController#setMaxFlows(IConnectionConfiguration,
 *      String)
 * 
 * @author Mitko Kolev
 */
public class SetFlowManagerMaxFlowsJob extends SessionSafeWorkerJob {

    private final IConnectionConfiguration connectionConfiguration;

    private final String newData;

    private static final String setMaxFlowsText = "handlers.setting.max.flows.text";

    private static final String setMaxFlowsFailedText = "handlers.setting.max.flows.failed";

    public SetFlowManagerMaxFlowsJob(Display display,
            IConnectionConfiguration connectionConfiguration, String newData) {
        super(display, Messages.getLabelString(setMaxFlowsText) + "@"
                + connectionConfiguration.getHost() + ":"
                + connectionConfiguration.getPort());
        this.connectionConfiguration = connectionConfiguration;
        this.newData = newData;
        this.setUser(true);
    }

    @Override
    public synchronized IStatus run(IProgressMonitor monitor) {
        IFlowManagerApplicationController flowMangerApplicationController = Activator
                .getFlowManagerApplicationController();

        try {
            monitor.beginTask(this.getName(), 1);
            monitor.worked(1);
            flowMangerApplicationController.setMaxFlows(
                    connectionConfiguration, newData);
            return Status.OK_STATUS;
        } catch (Throwable ioe) {
            return new Status(Status.ERROR, Activator.PLUGIN_ID, Messages
                    .getLabelString(setMaxFlowsFailedText)
                    + " " + connectionConfiguration.toString() + ".");
        } finally {
            monitor.done();
        }
    }
}