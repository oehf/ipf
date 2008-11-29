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

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

/**
 * In case of RAP applications the Display object in the UI thread is associated
 * with the user session. Therefore Display.getCurrent() and
 * Display.getDefault() return null. In order to keep the session, every worker
 * thread must know to what UI session it belongs.
 * <p>
 * 
 * @author Mitko Kolev
 */
public abstract class SessionSafeWorkerJob extends Job {

    private final Display display;

    public SessionSafeWorkerJob(Display display, String name) {
        super(name);
        if (display == null) {
            throw new IllegalArgumentException(
                    "Creating SessionSafeWorkerThread with null display!");
        }
        this.display = display;
    }

    /**
     * The cached display for this job.
     * 
     * @return
     */
    public Display getDisplay() {
        return display;
    }

}
