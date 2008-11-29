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
package org.openehealth.ipf.platform.manager.connection.ui.utils.jobs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

/**
 * Contains thread related methods. The class helps in a RAP environment.
 * 
 * @author Mitko Kolev
 */
public class JobUtils {

    private static Log log = LogFactory.getLog(JobUtils.class);

    /**
     * Executes safely the runnable in the Eclipse UI thread. If the Thread
     * cannot be associated with a UI session in RAP, does nothing.
     * 
     * @param runnable
     */
    public static void runSafe(Runnable runnable) {

        IJobManager jobMan = Job.getJobManager();
        Job job = jobMan.currentJob();
        if (job instanceof SessionSafeWorkerJob) {
            SessionSafeWorkerJob sessionWorkerJob = (SessionSafeWorkerJob) job;
            sessionWorkerJob.getDisplay().asyncExec(runnable);
        } else if (Display.getCurrent() != null) {
            runnable.run();
        } else {
            Thread t = Thread.currentThread();
            if (Display.getDefault() == null) {
                // this is the RAP case
                log
                        .error("Updating the GUI from a notitfication forwarder is not handle currently in RAP! (Unable to determine the UI session)");
            } else {
                if (t.getName().contains("ClientNotifForwarder")) {
                    Display.getDefault().asyncExec(runnable);
                    return;
                }
            }
            throw new IllegalStateException(
                    "Updating the GUI from a non SessionSafeWorkerJob!");
        }
    }
}
