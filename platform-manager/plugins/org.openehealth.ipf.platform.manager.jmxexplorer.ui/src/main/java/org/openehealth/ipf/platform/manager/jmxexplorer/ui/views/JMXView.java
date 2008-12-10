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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.JMXConnectionNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.JMXConnectionTree;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * The Main view which hosts the JMX Connections. Connections are showed in the
 * view with show-in command from the connections view. The view is observer of
 * the connection repostiory.
 * 
 * @author Mitko Kolev
 */
public class JMXView extends ViewPart implements Observer, MouseListener {

    private final static Log log = LogFactory.getLog(JMXView.class);

    private TreeViewer viewer;

    private DrillDownAdapter drillDownAdapter;

    private Action expandAll;

    private Action collapseAll;

    private JMXTreeContentProvider contentProvider;

    private JMXTreeLableProvider labelProvider;

    private final static String expandAllText;

    private final static String collapseAllText;

    static {
        expandAllText = Messages.getLabelString("JMXTree.expand.children");
        collapseAllText = Messages.getLabelString("JMXTree.collapse.children");
    }

    public JMXView() {
        super();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
        TreeFilter treeFilter = new TreeFilter();
        FilteredTree filteredTree = new FilteredTree(parent, SWT.BORDER
                | SWT.VIRTUAL | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL,
                treeFilter);
        viewer = filteredTree.getViewer();
        viewer.setUseHashlookup(true);

        drillDownAdapter = new DrillDownAdapter(viewer);
        // handle the content provider
        IJMXExplorerMediator mediator = Activator.getDefault()
                .getJMXExplorerMediator();

        contentProvider = new JMXTreeContentProvider(this.getViewSite());
        viewer.setContentProvider(contentProvider);

        labelProvider = new JMXTreeLableProvider();
        viewer.setLabelProvider(labelProvider);

        viewer.setSorter(new ViewerSorter());
        viewer.setInput(getViewSite());
        makeActions();
        createContextMenu(viewer);
        viewer.getTree().addMouseListener(this);
        contributeToActionBars();
        mediator.addObserver(this);

        List<IConnectionConfiguration> connectionConfigurations = mediator
                .getConnectionConfigurations();
        for (IConnectionConfiguration connection : connectionConfigurations) {
            showConnectionConfigurationInViewer(connection);
        }
    }

    private void createContextMenu(TreeViewer viewer) {
        final MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                manager.add(new Separator(
                        IWorkbenchActionConstants.MB_ADDITIONS));
                JMXView.this.fillContextMenu(manager);
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

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(expandAll);
        manager.add(new Separator());
        manager.add(collapseAll);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(expandAll);
        manager.add(collapseAll);
        manager.add(new Separator());
        // drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        drillDownAdapter.addNavigationActions(manager);
    }

    @SuppressWarnings("unchecked")
    List<Node> getSelectionNodes(ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection is = (IStructuredSelection) selection;
            if (is.isEmpty()) {
                return null;
            }
            List elements = is.toList();
            return elements;
        }
        return new ArrayList<Node>();
    }

    private void makeActions() {
        expandAll = new Action() {

            @Override
            public void run() {
                ISelection selection = viewer.getSelection();
                List<Node> nodes = getSelectionNodes(selection);
                for (Node node : nodes) {
                    viewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
                }
            }
        };
        expandAll.setText(expandAllText);
        expandAll.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_TOOL_REDO));

        collapseAll = new Action() {

            @Override
            public void run() {
                ISelection selection = viewer.getSelection();
                List<Node> nodes = getSelectionNodes(selection);
                for (Node node : nodes) {
                    viewer.collapseToLevel(node, TreeViewer.ALL_LEVELS);
                }
            }
        };
        collapseAll.setText(collapseAllText);
        collapseAll.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_TOOL_UNDO));
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();

        this.getSite().getPage().activate(this);
        this.getSite().setSelectionProvider(viewer);

    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDoubleClick(MouseEvent e) {
        try {
            IHandlerService services = (IHandlerService) getSite().getService(
                    IHandlerService.class);
            services
                    .executeCommand(
                            "org.openehealth.ipf.platform.manager.jmxexplorer.ui.open.jmxeditor",
                            null);
        } catch (NotHandledException ex) {
            log.error("The double click on a connection is not handled", ex);
        } catch (ExecutionException ex) {
            log.error("Double click execution exception: ", ex);
        } catch (NotDefinedException ex) {
            log.error("Not defined double click action");
        } catch (NotEnabledException ex) {
            log.error("Double click action not enabled for this type");
        }
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

    /**
     * Updates the ID_PROP_SHEET view, if a node has changed.
     */
    private void updatePropertiesPage() {
        // upate the tree;
        IWorkbenchPage page = this.getSite().getPage();

        IViewReference[] views = page.getViewReferences();
        for (int t = 0; t < views.length; t++) {
            IViewReference reference = views[t];
            if (reference.getId().equals(IPageLayout.ID_PROP_SHEET)) {
                IViewPart part = page.findView(views[t].getId());
                if (part instanceof PropertySheet && part != null) {
                    IPage propertySheetPage = ((PropertySheet) part)
                            .getCurrentPage();
                    if (propertySheetPage instanceof PropertySheetPage
                            && propertySheetPage != null) {
                        ((PropertySheetPage) propertySheetPage).refresh();
                    }
                }
            }
        }
    }

    /**
     * Removes from the tree viewer the connection and all nodes associated with
     * it.
     * 
     * @param connectionConfiguration
     */
    public void removeFromView(IConnectionConfiguration connectionConfiguration) {
        JMXConnectionTree root = contentProvider.getTree();
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
                        viewer.refresh();
                        return;
                    }
                }

            }
        }
    }

    /**
     * Updates a node in the tree for the given connection and objectName
     * 
     * @param connectionConfiguration
     * @param objectName
     */
    private void updateNode(IConnectionConfiguration connectionConfiguration,
            ObjectName objectName) {
        IContentProvider contentProvider = viewer.getContentProvider();

        if (contentProvider instanceof JMXTreeContentProvider) {
            JMXTreeContentProvider jmxContentProvider = (JMXTreeContentProvider) contentProvider;
            JMXConnectionTree tree = jmxContentProvider.getTree();
            Node[] connectionNodes = tree.getChildren();
            for (Node connectionNode : connectionNodes) {
                if (connectionNode instanceof JMXConnectionNode) {
                    JMXConnectionNode jmxConnectionNode = (JMXConnectionNode) connectionNode;
                    IConnectionConfiguration nodeConnectionConfiguration = (IConnectionConfiguration) jmxConnectionNode
                            .getAdapter(IConnectionConfiguration.class);
                    if (nodeConnectionConfiguration == null)
                        continue;
                    if (nodeConnectionConfiguration
                            .equals(connectionConfiguration)) {
                        updatePropertiesPage();
                    }
                }
            }
        }
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof JMXExplorerEvent) {
            final JMXExplorerEvent event = (JMXExplorerEvent) arg;
            final IConnectionConfiguration connectionConfiguration = event
                    .getConnectionConfigurationContext();

            switch (event.getType()) {
            case JMXExplorerEvent.ATTRIBUTE_VALUE_CHANGED:
            case JMXExplorerEvent.INVOKE_OPERATION_RESULT:

                UpdateNodeRunnable runnable = new UpdateNodeRunnable(
                        connectionConfiguration, event.getObjectName());
                JobUtils.runSafe(runnable);
                return;
            }
        } else if (arg instanceof ConnectionEvent) {
            final ConnectionEvent event = (ConnectionEvent) arg;
            final IConnectionConfiguration connectionConfiguration = event
                    .getConnectionConfigurationContext();
            switch (event.getType()) {
            case ConnectionEvent.CONNECTION_REMOVED:
                ConnectionRemovedRunnable connectionRemovedRunnable = new ConnectionRemovedRunnable(
                        connectionConfiguration);
                JobUtils.runSafe(connectionRemovedRunnable);
                break;
            case ConnectionEvent.JMX_CONNECTION_CLOSED:
                ConnectionClosedRunnable connectionClosedRunnable = new ConnectionClosedRunnable(
                        connectionConfiguration);
                JobUtils.runSafe(connectionClosedRunnable);
                break;
            case ConnectionEvent.JMX_CONNECTION_OPEN:
                ConnectionOpenRunnable connectionOpenRunnable = new ConnectionOpenRunnable(
                        connectionConfiguration);
                JobUtils.runSafe(connectionOpenRunnable);
                break;

            case ConnectionEvent.CONNECTION_ADDED:
                ConnectionAddedRunnable connectionAddedRunnable = new ConnectionAddedRunnable(
                        connectionConfiguration);
                JobUtils.runSafe(connectionAddedRunnable);
                break;
            }
        }
    }

    /**
     * Updates a node and its children, which have the objectName. Updates also
     * the properties view.
     */
    protected class UpdateNodeRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        private final ObjectName objectName;

        public UpdateNodeRunnable(
                IConnectionConfiguration connectionConfiguration,
                ObjectName objectName) {
            this.connectionConfiguration = connectionConfiguration;
            this.objectName = objectName;
        }

        public void run() {
            updateNode(connectionConfiguration, objectName);
        }

    }

    /**
     * Removes the connection from the view.
     */
    final class ConnectionRemovedRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        public ConnectionRemovedRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        public void run() {
            removeFromView(connectionConfiguration);

        }
    }

    /**
     * Removes the old connection node. Adss a new JMXConnectionNode with no
     * children.
     */
    final class ConnectionClosedRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        public ConnectionClosedRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        public void run() {
            if (connectionConfiguration == null)
                return;
            JMXConnectionTree tree = contentProvider.getTree();
            for (Node connectionNode : tree.getChildren()) {
                IConnectionConfiguration currentConnectionConfiguration = (IConnectionConfiguration) connectionNode
                        .getAdapter(IConnectionConfiguration.class);
                if (connectionConfiguration
                        .equals(currentConnectionConfiguration)) {
                    tree.removeChild(tree
                            .getChildByName(connectionConfiguration.getName()));
                    JMXConnectionNode node = new JMXConnectionNode(
                            connectionConfiguration, connectionConfiguration
                                    .getName());
                    tree.addChild(node);
                    break;
                }
            }
            viewer.refresh();
        }
    }

    /**
     * IF a node with the name of the connection exists, and if the node has no
     * children, creates a new node with the same connection and populates the
     * MBeans in it.
     */
    final class ConnectionOpenRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        public ConnectionOpenRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        public void run() {
            if (connectionConfiguration == null)
                return;
            JMXConnectionTree tree = contentProvider.getTree();
            boolean shouldUpdate = false;
            for (Node node : tree.getChildren()) {
                IConnectionConfiguration connectionConfigurationAlreadyIn = (IConnectionConfiguration) node
                        .getAdapter(IConnectionConfiguration.class);
                if (connectionConfigurationAlreadyIn != null) {
                    if (connectionConfigurationAlreadyIn
                            .equals(connectionConfiguration)) {
                        if (!node.hasChildren()) {
                            // update the tree only if the connection was
                            // offline (has no children)
                            shouldUpdate = true;
                        }
                        break;
                    }
                }
            }
            // if the connection has not been added, return
            if (!shouldUpdate)
                return;
            // update the table
            JMXConnectionNode node = new JMXConnectionNode(
                    connectionConfiguration, connectionConfiguration.getName());
            try {
                node.initialize();
            } catch (IOException ioe) {
                log.error(ioe);
            } finally {
                // remove the old one.
                tree.removeChild(tree.getChildByName(node.getName()));
                // add the new one
                tree.addChild(node);
            }
            viewer.refresh();
        }
    }

    /**
     * Shows the connection in the viewer.
     * 
     * @author Mitko Kolev
     */
    final class ConnectionAddedRunnable implements Runnable {
        private final IConnectionConfiguration connection;

        public ConnectionAddedRunnable(IConnectionConfiguration connection) {
            this.connection = connection;

        }

        @Override
        public void run() {
            showConnectionConfigurationInViewer(connection);
        }
    }

    /**
     * Must execute in the UI Thread. Adds a connection to the node of
     * connections and activates the view.
     * 
     * @param connectionConfiguration
     */
    public void showConnectionConfigurationInViewer(
            IConnectionConfiguration connectionConfiguration) {
        JMXConnectionTree tree = contentProvider.getTree();
        for (Node node : tree.getChildren()) {
            IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) node
                    .getAdapter(IConnectionConfiguration.class);
            if (thisConnectionConfiguration != null) {
                if (thisConnectionConfiguration.equals(connectionConfiguration)) {
                    return;
                }
            }
        }
        // update the table
        JMXConnectionNode node = new JMXConnectionNode(connectionConfiguration,
                connectionConfiguration.getName());
        IJMXExplorerMediator mediator = Activator.getDefault()
                .getJMXExplorerMediator();
        List<IConnectionConfiguration> openConnections = mediator
                .getOpenConnectionConfigurations();
        try {
            tree.addChild(node);
            if (openConnections.contains(connectionConfiguration)) {
                node.initialize();
            }
        } catch (IOException ioe) {
            log.error(ioe);
        }

        viewer.refresh();
    }

    @Override
    public void dispose() {
        IJMXExplorerMediator mediator = Activator.getDefault()
                .getJMXExplorerMediator();
        // clean up
        mediator.deleteObserver(this);

        super.dispose();
        if (contentProvider != null)
            contentProvider.dispose();
        if (labelProvider != null)
            labelProvider.dispose();
    }

}
