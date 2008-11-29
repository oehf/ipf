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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.WindowManager;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

/**
 * @author Mitko Kolev
 */
public class WorkbenchConfigurerMock implements IWorkbenchConfigurer {
    private boolean saveAndRestore = false;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#declareImage(java.lang
     * .String, org.eclipse.jface.resource.ImageDescriptor, boolean)
     */
    @Override
    public void declareImage(String symbolicName, ImageDescriptor descriptor,
            boolean shared) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchConfigurer#emergencyClose()
     */
    @Override
    public void emergencyClose() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchConfigurer#emergencyClosing()
     */
    @Override
    public boolean emergencyClosing() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#getData(java.lang.String)
     */
    @Override
    public Object getData(String key) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#getExitOnLastWindowClose
     * ()
     */
    @Override
    public boolean getExitOnLastWindowClose() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchConfigurer#getSaveAndRestore()
     */
    @Override
    public boolean getSaveAndRestore() {
        return saveAndRestore;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#getWindowConfigurer(org
     * .eclipse.ui.IWorkbenchWindow)
     */
    @Override
    public IWorkbenchWindowConfigurer getWindowConfigurer(
            IWorkbenchWindow window) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchConfigurer#getWorkbench()
     */
    @Override
    public IWorkbench getWorkbench() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#getWorkbenchWindowManager
     * ()
     */
    @Override
    public WindowManager getWorkbenchWindowManager() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#openFirstTimeWindow()
     */
    @Override
    public void openFirstTimeWindow() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchConfigurer#restoreState()
     */
    @Override
    public IStatus restoreState() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#restoreWorkbenchWindow
     * (org.eclipse.ui.IMemento)
     */
    @Override
    public IWorkbenchWindowConfigurer restoreWorkbenchWindow(IMemento memento)
            throws WorkbenchException {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#setData(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void setData(String key, Object data) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#setExitOnLastWindowClose
     * (boolean)
     */
    @Override
    public void setExitOnLastWindowClose(boolean enabled) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchConfigurer#setSaveAndRestore(boolean
     * )
     */
    @Override
    public void setSaveAndRestore(boolean enabled) {
        this.saveAndRestore = enabled;

    }
}
