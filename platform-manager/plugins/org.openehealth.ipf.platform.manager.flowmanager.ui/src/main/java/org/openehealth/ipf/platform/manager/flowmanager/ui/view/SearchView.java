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
package org.openehealth.ipf.platform.manager.flowmanager.ui.view;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.editor.ConnectionEditorInput;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.ui.editor.FlowManagerEditor;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;

/**
 * Viewer for the flow results.
 * 
 * @see FlowResultVisualizer
 * 
 * @author Mitko Kolev
 */
public class SearchView extends ViewPart implements Observer {

    private FlowResultVisualizer resultVisualizer;

    private final Log log = LogFactory.getLog(SearchView.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createPartControl(Composite parent) {

        resultVisualizer = new FlowResultVisualizer(this, parent);
        getSite().registerContextMenu(
                createContextMenu(resultVisualizer.getViewer()),
                resultVisualizer.getViewer());
        Activator.getFlowManagerApplicationController().addObserver(this);

    }

    public MenuManager createContextMenu(Viewer viewer) {
        MenuManager manager = createMenuManager();
        Menu menu = manager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        return manager;
    }

    public static MenuManager createMenuManager() {
        MenuManager manager = new MenuManager();
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        return manager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        this.getSite().setSelectionProvider(resultVisualizer.getViewer());
        IStatusLineManager statusline = this.getViewSite().getActionBars()
                .getStatusLineManager();
        IConnectionConfiguration connectionConfiguration = resultVisualizer
                .getActiveConnectionConfigurationContext();
        if (connectionConfiguration != null) {
            statusline.setMessage(null, connectionConfiguration.getName());
        }
        openFlowManagerEditor(connectionConfiguration);

    }

    /**
     * Sets the active editor the flowManagerEditor, when the view accepts focus
     * 
     * @param connectionConfiguration
     *            the connection for which the editor will be opened
     */
    private void openFlowManagerEditor(
            IConnectionConfiguration connectionConfiguration) {
        if (connectionConfiguration == null)
            return;
        if (this.getSite().getPage().getActiveEditor() == null)
            return;
        if (this.getSite().getPage().getActiveEditor() instanceof FlowManagerEditor)
            return;
        ConnectionEditorInput input = new ConnectionEditorInput(
                connectionConfiguration);
        try {
            String editorId = FlowManagerEditor.class.getName();
            if (editorId != null) {
                this.getSite().getPage().openEditor(input, editorId, true);
            }
        } catch (PartInitException e1) {
            log.error("Cannto open view", e1);
        }
    }

    @Override
    public void dispose() {
        Activator.getFlowManagerApplicationController().deleteObserver(this);
        super.dispose();
        resultVisualizer.dispose();
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof FlowManagerEvent) {
            FlowManagerEvent event = (FlowManagerEvent) arg;

            if (event.getType() == FlowManagerEvent.FLOWS_RECEIVED) {
                UpdateGuiStateRunnable runnable = new UpdateGuiStateRunnable();
                JobUtils.runSafe(runnable);
            }
        }

    }

    /**
     * Just activates this part
     * <p>
     * Copyright (c) 2008 <a
     * href="http://www.intercomponentware.com">InterComponentWare AG</a>
     * 
     * @author Mitko Kolev i000174
     */
    final class UpdateGuiStateRunnable implements Runnable {

        public void run() {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().bringToTop(SearchView.this);
        }
    }

}
