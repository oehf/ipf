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

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.junit.Test;

/**
 * Tests whether there are changes in the application window configuration.
 * 
 * @author Mitko Kolev
 */
public class ApplicationWorkbenchWindowAdvisorTest extends TestCase {

    private ApplicationWorkbenchWindowAdvisor workbenchWindowAdvisor;
    private IWorkbenchWindowConfigurer windowConfigurer;

    @Override
    public void setUp() {
        windowConfigurer = new WorkbenchWindowConfigurerMock();

    }

    @Test
    public void testWindowSettings() {
        workbenchWindowAdvisor = new ApplicationWorkbenchWindowAdvisor(
                this.windowConfigurer);
        workbenchWindowAdvisor.preWindowOpen();

        assertTrue(windowConfigurer.getShowCoolBar() == false);
        assertTrue(windowConfigurer.getShowFastViewBars() == true);
        assertTrue(windowConfigurer.getShowProgressIndicator() == true);
        assertTrue(windowConfigurer.getShowPerspectiveBar() == true);
        assertTrue(windowConfigurer.getShowStatusLine() == true);
        assertTrue(windowConfigurer.getShowMenuBar() == true);

    }
}
