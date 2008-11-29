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

/**
 * 
 * UI job to execute changeFlowFiltering
 * 
 * @see org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController#changeFlowFiltering(IConnectionConfiguration)
 * 
 * @author Mitko Kolev
 */
public class ChangeFlowManagerFlowFilteringJob extends SessionSafeWorkerJob {

    private final IConnectionConfiguration connectionConfiguration;

    public ChangeFlowManagerFlowFilteringJob(Display display,
            IConnectionConfiguration connectionConfiguration) {
        super(display, connectionConfiguration.getHost() + ":"
                + connectionConfiguration.getPort());
        this.connectionConfiguration = connectionConfiguration;
        this.setUser(true);
    }

    @Override
    public synchronized IStatus run(IProgressMonitor monitor) {
        IFlowManagerApplicationController flowMangerApplicationController = Activator
                .getFlowManagerApplicationController();

        try {
            monitor.beginTask(this.getName(), 1);
            flowMangerApplicationController
                    .changeFlowFiltering(connectionConfiguration);
            monitor.worked(1);
            return Status.OK_STATUS;
        } catch (Throwable t) {
            return new Status(Status.ERROR, Activator.PLUGIN_ID,
                    connectionConfiguration.toString());
        } finally {
            monitor.done();
        }

    }
}
