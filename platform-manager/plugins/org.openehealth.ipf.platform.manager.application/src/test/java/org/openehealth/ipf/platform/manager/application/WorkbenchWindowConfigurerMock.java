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
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.presentations.AbstractPresentationFactory;

/**
 * Mock class for workbench configurer.
 * 
 * @author Mitko Kolev
 */
public class WorkbenchWindowConfigurerMock implements
        IWorkbenchWindowConfigurer {
    private boolean showCoolbar = false;
    private int shellStyle;
    private boolean showMenuBar;
    private boolean showPerspectiveBar;
    private boolean showProgressIndicator;
    private boolean showStatusLine;
    private boolean showFastViewBars;
    private String title;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#addEditorAreaTransfer
     * (org.eclipse.swt.dnd.Transfer)
     */
    @Override
    public void addEditorAreaTransfer(Transfer transfer) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * configureEditorAreaDropListener(org.eclipse.swt.dnd.DropTargetListener)
     */
    @Override
    public void configureEditorAreaDropListener(
            DropTargetListener dropTargetListener) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createCoolBarControl
     * (org.eclipse.swt.widgets.Composite)
     */
    @Override
    public Control createCoolBarControl(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createMenuBar()
     */
    @Override
    public Menu createMenuBar() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createPageComposite
     * (org.eclipse.swt.widgets.Composite)
     */
    @Override
    public Control createPageComposite(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#createStatusLineControl
     * (org.eclipse.swt.widgets.Composite)
     */
    @Override
    public Control createStatusLineControl(Composite parent) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getActionBarConfigurer
     * ()
     */
    @Override
    public IActionBarConfigurer getActionBarConfigurer() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getData(java.lang
     * .String)
     */
    @Override
    public Object getData(String key) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getInitialSize()
     */
    @Override
    public Point getInitialSize() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getPresentationFactory
     * ()
     */
    @Override
    public AbstractPresentationFactory getPresentationFactory() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShellStyle()
     */
    @Override
    public int getShellStyle() {
        return shellStyle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowCoolBar()
     */
    @Override
    public boolean getShowCoolBar() {
        return showCoolbar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowFastViewBars
     * ()
     */
    @Override
    public boolean getShowFastViewBars() {
        return this.showFastViewBars;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowMenuBar()
     */
    @Override
    public boolean getShowMenuBar() {
        return showMenuBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowPerspectiveBar
     * ()
     */
    @Override
    public boolean getShowPerspectiveBar() {
        return showPerspectiveBar;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * getShowProgressIndicator()
     */
    @Override
    public boolean getShowProgressIndicator() {
        return showProgressIndicator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getShowStatusLine()
     */
    @Override
    public boolean getShowStatusLine() {
        return showStatusLine;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchWindowConfigurer#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.application.IWorkbenchWindowConfigurer#getWindow()
     */
    @Override
    public IWorkbenchWindow getWindow() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#getWorkbenchConfigurer
     * ()
     */
    static WorkbenchConfigurerMock configurer;

    @Override
    public IWorkbenchConfigurer getWorkbenchConfigurer() {
        if (configurer == null)
            configurer = new WorkbenchConfigurerMock();
        return configurer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#saveState(org.eclipse
     * .ui.IMemento)
     */
    @Override
    public IStatus saveState(IMemento memento) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setData(java.lang
     * .String, java.lang.Object)
     */
    @Override
    public void setData(String key, Object data) {
        throw new UnsupportedOperationException("operation not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setInitialSize(
     * org.eclipse.swt.graphics.Point)
     */
    @Override
    public void setInitialSize(Point initialSize) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setPresentationFactory
     * (org.eclipse.ui.presentations.AbstractPresentationFactory)
     */
    @Override
    public void setPresentationFactory(AbstractPresentationFactory factory) {
        throw new UnsupportedOperationException("operation not supported");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShellStyle(int)
     */
    @Override
    public void setShellStyle(int shellStyle) {
        this.shellStyle = shellStyle;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowCoolBar(
     * boolean)
     */
    @Override
    public void setShowCoolBar(boolean show) {
        this.showCoolbar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowFastViewBars
     * (boolean)
     */
    @Override
    public void setShowFastViewBars(boolean enable) {
        this.showFastViewBars = enable;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowMenuBar(
     * boolean)
     */
    @Override
    public void setShowMenuBar(boolean show) {
        this.showMenuBar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowPerspectiveBar
     * (boolean)
     */
    @Override
    public void setShowPerspectiveBar(boolean show) {
        this.showPerspectiveBar = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.eclipse.ui.application.IWorkbenchWindowConfigurer#
     * setShowProgressIndicator(boolean)
     */
    @Override
    public void setShowProgressIndicator(boolean show) {
        this.showProgressIndicator = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setShowStatusLine
     * (boolean)
     */
    @Override
    public void setShowStatusLine(boolean show) {
        this.showStatusLine = show;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.application.IWorkbenchWindowConfigurer#setTitle(java.lang
     * .String)
     */
    @Override
    public void setTitle(String title) {
        this.title = title;

    }

}
