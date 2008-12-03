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
package org.openehealth.ipf.platform.manager.application;

import junit.framework.TestCase;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * @author Mitko Kolev
 */
public class ApplicationWorkbenchAdvisorTest extends TestCase {
    private ApplicationWorkbenchAdvisor workbenchAdvisor;
    private IWorkbenchConfigurer configurer;
    private IWorkbenchWindowConfigurer windowConfigurer;

    @Override
    public void setUp() {
        workbenchAdvisor = new ApplicationWorkbenchAdvisor();
        configurer = new WorkbenchConfigurerMock();
        windowConfigurer = new WorkbenchWindowConfigurerMock();

    }

    /**
     * The perspective id must be the same in the defining plug-in!
     */
    public void testIsPerspectiveIdUnchanged() {
        assertTrue(ApplicationWorkbenchAdvisor.PERSPECTIVE_ID
                .equals("org.openehealth.ipf.platform.manager.connection.ui.perspective.DefaultPerspective"));
    }

    public void testIsSaveAndRestore() {
        workbenchAdvisor.initialize(configurer);
        assertTrue(configurer.getSaveAndRestore());
    }

    public void testCreateWorkbenchAdvisor() {
        WorkbenchWindowAdvisor advisor = workbenchAdvisor
                .createWorkbenchWindowAdvisor(windowConfigurer);
        assertTrue(advisor != null);
        assertTrue(advisor instanceof ApplicationWorkbenchWindowAdvisor);
    }
}
