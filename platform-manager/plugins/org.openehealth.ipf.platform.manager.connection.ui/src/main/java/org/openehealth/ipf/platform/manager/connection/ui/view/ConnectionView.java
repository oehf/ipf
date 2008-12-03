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
package org.openehealth.ipf.platform.manager.connection.ui.view;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.editor.ConnectionEditorInput;
import org.openehealth.ipf.platform.manager.connection.ui.jobs.OpenJMXConnectionJob;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.connection.ui.tree.ConnectionNode;
import org.openehealth.ipf.platform.manager.connection.ui.tree.JMXContentProvider;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.connection.ui.utils.ConnectionUtils;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.connection.ui.utils.messages.Messages;
import org.openehealth.ipf.platform.manager.connection.ui.utils.sorter.TreeColumnViewerSorter;

/**
 * <p>
 * View which visualizes the connection definitions.
 * 
 * @author Mitko Kolev
 */
public class ConnectionView extends ViewPart implements
        ISelectionChangedListener, Observer, MouseListener, KeyListener {

    private DrillDownAdapter drillDownAdapter;

    private TreeViewer viewer;
    // a fixed provider (JMX) can have different providers in the future.
    private JMXContentProvider contentProvider;

    private final Log log = LogFactory.getLog(ConnectionView.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        log.info("Creating the connection editor");
        IJMXConnectionManager jMXConnectionManager = Activator.getDefault()
                .getJMXConnectionManager();
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        drillDownAdapter = new DrillDownAdapter(viewer);
        // handle the content provider
        contentProvider = new JMXContentProvider(this.getViewSite(),
                jMXConnectionManager);
        viewer.setContentProvider(contentProvider);

        viewer.setUseHashlookup(true);
        viewer.getTree().setLinesVisible(true);
        viewer.getTree().setHeaderVisible(true);

        TreeViewerColumn tc0 = new TreeViewerColumn(viewer, SWT.CENTER);
        TreeViewerColumn tc1 = new TreeViewerColumn(viewer, SWT.CENTER);
        TreeViewerColumn tc2 = new TreeViewerColumn(viewer, SWT.CENTER);

        tc0.getColumn().setText(Messages.getLabelString("connection.name"));
        tc0.setLabelProvider(new JMXConnectionLabelProvider(0));
        tc0.getColumn().setWidth(140);
        tc1.getColumn().setText(Messages.getLabelString("connection.host"));
        tc1.setLabelProvider(new JMXConnectionLabelProvider(1));
        tc1.getColumn().setWidth(100);
        tc2.getColumn().setText(Messages.getLabelString("connection.port"));
        tc2.setLabelProvider(new JMXConnectionLabelProvider(2));
        tc2.getColumn().setWidth(60);

        new TreeColumnViewerSorter(viewer, tc0) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IAdaptable adapter1 = (IAdaptable) e1;
                IAdaptable adapter2 = (IAdaptable) e2;
                IConnectionConfiguration info1 = (IConnectionConfiguration) adapter1
                        .getAdapter(IConnectionConfiguration.class);
                IConnectionConfiguration info2 = (IConnectionConfiguration) adapter2
                        .getAdapter(IConnectionConfiguration.class);
                return info1.getName().compareTo(info2.getName());
            }
        };

        new TreeColumnViewerSorter(viewer, tc1) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IAdaptable adapter1 = (IAdaptable) e1;
                IAdaptable adapter2 = (IAdaptable) e2;
                IConnectionConfiguration info1 = (IConnectionConfiguration) adapter1
                        .getAdapter(IConnectionConfiguration.class);
                IConnectionConfiguration info2 = (IConnectionConfiguration) adapter2
                        .getAdapter(IConnectionConfiguration.class);
                return info1.getHost().compareTo(info2.getHost());
            }
        };
        new TreeColumnViewerSorter(viewer, tc2) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                IAdaptable adapter1 = (IAdaptable) e1;
                IAdaptable adapter2 = (IAdaptable) e2;
                IConnectionConfiguration info1 = (IConnectionConfiguration) adapter1
                        .getAdapter(IConnectionConfiguration.class);
                IConnectionConfiguration info2 = (IConnectionConfiguration) adapter2
                        .getAdapter(IConnectionConfiguration.class);
                return new Integer(info1.getPort()).compareTo(new Integer(info2
                        .getPort()));
            }
        };
        viewer.setInput(getViewSite());
        viewer.getTree().addMouseListener(this);

        this.getSite().setSelectionProvider(viewer);
        viewer.addSelectionChangedListener(this);

        // add a popup menu
        createContextMenu(viewer);
        jMXConnectionManager.addObserver(this);
    }

    private void createContextMenu(TreeViewer viewer) {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                manager.add(new Separator(
                        IWorkbenchActionConstants.MB_ADDITIONS));
                // menuMgr.add(new Separator());
                // drillDownAdapter.addNavigationActions(menuMgr);
            }
        });
        menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        getSite().registerContextMenu(menuMgr, viewer);
        menuMgr.add(new Separator());
        drillDownAdapter.addNavigationActions(menuMgr);
        // add the properties action
        // menuMgr.add(new Separator());
        // menuMgr.add(new PropertyDialogAction(getSite(), viewer));

        Control viewerControl = viewer.getControl();
        Menu menu = menuMgr.createContextMenu(viewerControl);
        viewerControl.setMenu(menu);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {
        getSite().getPage().activate(this);
        getSite().setSelectionProvider(viewer);
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        IConnectionConfiguration connectionConfiguration = ConnectionUtils
                .getSingleConnectionConfigurationObject(event.getSelection());
        if (connectionConfiguration != null) {
            if (connectionConfiguration == null)
                return;
            IStatusLineManager statusline = this.getViewSite().getActionBars()
                    .getStatusLineManager();
            statusline.setMessage(null, connectionConfiguration.getName());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {

        final ConnectionEvent event = (ConnectionEvent) arg;
        final IConnectionConfiguration connectionConfiguration = event
                .getConnectionConfigurationContext();

        if (event.getType() == ConnectionEvent.CONNECTION_REMOVED) {
            IWorkbenchPage page = this.getSite().getPage();
            ConnectionRemovedRunnable runnable = new ConnectionRemovedRunnable(
                    connectionConfiguration, page);
            JobUtils.runSafe(runnable);

        } else if ((event.getType() == ConnectionEvent.JMX_CONNECTION_CLOSED)
                || (event.getType() == ConnectionEvent.JMX_CONNECTION_OPEN)) {
            // update the table
            UpdateTreeRunnable runnable = new UpdateTreeRunnable(
                    connectionConfiguration);
            JobUtils.runSafe(runnable);
        }
        if (event.getType() == ConnectionEvent.CONNECTION_ADDED) {
            // set the table input
            AddNewConnectionRunnable runnable = new AddNewConnectionRunnable(
                    connectionConfiguration);
            JobUtils.runSafe(runnable);
        }

    }

    final class ConnectionRemovedRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        private final IWorkbenchPage page;

        public ConnectionRemovedRunnable(
                IConnectionConfiguration connectionConfiguration,
                IWorkbenchPage page) {
            this.connectionConfiguration = connectionConfiguration;
            this.page = page;
        }

        public void run() {
            // update the table
            ConnectionNode root = contentProvider.getTree();
            // update the table
            if (root.hasChildren()) {
                Node[] nodes = root.getChildren();
                for (int t = 0; t < nodes.length; t++) {
                    Node n = nodes[t];
                    IConnectionConfiguration treeConnectionConfiguration = (IConnectionConfiguration) n
                            .getAdapter(IConnectionConfiguration.class);
                    if (treeConnectionConfiguration != null) {
                        if (treeConnectionConfiguration
                                .equals(connectionConfiguration)) {
                            root.removeChild(n);
                        }
                    }
                }
                closeAllEditors(connectionConfiguration);
                viewer.refresh();

            }
            ConnectionEditorInput connectionEditorInput = new ConnectionEditorInput(
                    connectionConfiguration);
            IEditorPart deletedConnectionEditor = page
                    .findEditor(connectionEditorInput);
            if (deletedConnectionEditor != null) {
                page.closeEditor(deletedConnectionEditor, false);
            }
        }

        private void closeAllEditors(
                IConnectionConfiguration connectionConfiguration) {
            if (connectionConfiguration == null)
                return;
            IEditorReference[] references = page.getEditorReferences();
            for (IEditorReference reference : references) {
                IEditorPart editor = reference.getEditor(false);
                try {
                    IConnectionConfiguration editorConnectionConfiguration = (IConnectionConfiguration) reference
                            .getEditorInput().getAdapter(
                                    IConnectionConfiguration.class);
                    if (editorConnectionConfiguration != null
                            && editorConnectionConfiguration
                                    .equals(connectionConfiguration)) {
                        page.closeEditor(editor, true);
                    }
                } catch (PartInitException pe) {
                    log.error("Cannot close Editor for connetion "
                            + connectionConfiguration, pe);
                    continue;
                }
            }
        }
    }

    final class AddNewConnectionRunnable implements Runnable {

        IConnectionConfiguration connectionConfiguration;

        AddNewConnectionRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        public void run() {
            ConnectionNode root = contentProvider.getTree();
            // update the table
            if (root.hasChildren()) {
                Node[] nodes = root.getChildren();
                for (int t = 0; t < nodes.length; t++) {
                    Node n = nodes[t];
                    IConnectionConfiguration treeConnectionConfiguration = (IConnectionConfiguration) n
                            .getAdapter(IConnectionConfiguration.class);
                    if (treeConnectionConfiguration != null) {
                        if (treeConnectionConfiguration
                                .equals(connectionConfiguration)) {
                            // we have it already
                            log.error("Adding a node which already exists!");
                            return;
                        }
                    }
                }
            }
            root.addChild(new ConnectionNode(connectionConfiguration,
                    connectionConfiguration.getName()));
            viewer.refresh();

        }
    }

    final class UpdateTreeRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        public UpdateTreeRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        public void run() {
            viewer.update(new ConnectionNode(connectionConfiguration,
                    connectionConfiguration.getName()), null);
            IToolBarManager manager = ConnectionView.this.getViewSite()
                    .getActionBars().getToolBarManager();
            manager.update(true);

        }
    }

    private void openDefaultEditor() {
        IJMXConnectionManager jMXConnectionManager = Activator.getDefault()
                .getJMXConnectionManager();
        IConnectionConfiguration connectionConfiguration = ConnectionUtils
                .getSingleConnectionConfigurationObject(viewer.getSelection());
        if (connectionConfiguration == null)
            return;

        ConnectionEditorInput input = new ConnectionEditorInput(
                connectionConfiguration);
        try {
            String editorId = getDefaultEditorId(connectionConfiguration);
            if (editorId != null) {

                this.getSite().getPage().openEditor(input, editorId, true);
            }
        } catch (PartInitException e1) {
            log.error("Cannto open view", e1);
        }
        if (!jMXConnectionManager.isConnected(connectionConfiguration)) {
            log.info("Openning connection: " + connectionConfiguration);
            new OpenJMXConnectionJob(Display.getCurrent(), Activator
                    .getDefault().getJMXConnectionManager(),
                    connectionConfiguration).schedule();
        }
    }

    private String getDefaultEditorId(
            IConnectionConfiguration connectionConfiguration) {
        IEditorRegistry editorReg = PlatformUI.getWorkbench()
                .getEditorRegistry();
        IEditorDescriptor descriptor = editorReg
                .getDefaultEditor(connectionConfiguration.getName()
                        + "."
                        + ConnectionEditorInput.CONNECTION_EDITOR_FILE_EXTENSION_ID);
        if (descriptor != null) {
            return descriptor.getId();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        setFocus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.keyCode == SWT.CR) {
            openDefaultEditor();
        }
    }

    @Override
    public void dispose() {
        Activator.getDefault().getJMXConnectionManager().deleteObserver(this);
        viewer.getTree().removeMouseListener(this);
        viewer.removeSelectionChangedListener(this);
        super.dispose();
    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDoubleClick(MouseEvent e) {
        openDefaultEditor();

    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDown(MouseEvent e) {
        setFocus();

    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseUp(MouseEvent e) {
    }

}
