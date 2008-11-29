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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.connection.ui.utils.sorter.TableColumnViewerSorter;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.adapter.FlowInfoAdapter;
import org.openehealth.ipf.platform.manager.flowmanager.ui.adapter.FlowManagerAdapterFactory;
import org.openehealth.ipf.platform.manager.flowmanager.ui.editor.FlowManagerEditor;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * Delegates the flow results to the viewer.
 * 
 * @author Mitko Kolev
 */
public class FlowResultVisualizer implements Observer, MouseListener {

    private volatile TableViewer tableViewer;

    private IConnectionConfiguration activeConnectionConfigurationContext;

    private final Log log = LogFactory.getLog(FlowResultVisualizer.class);

    private final ViewPart part;

    public FlowResultVisualizer(ViewPart part, Composite parent) {
        createResultPart(parent);
        this.part = part;
    }

    private void createResultPart(Composite parent) {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;

        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(1, false));
        group.setText(Messages.getLabelString("manage.results.group")); //$NON-NLS-1$
        tableViewer = new TableViewer(group, SWT.MULTI | SWT.VIRTUAL
                | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.getTable().addMouseListener(this);

        Table table = tableViewer.getTable();
        table.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableViewerColumn tc0 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc1 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc2 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc3 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc4 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc5 = new TableViewerColumn(tableViewer, SWT.CENTER);
        TableViewerColumn tc6 = new TableViewerColumn(tableViewer, SWT.CENTER);

        tc0.getColumn().setText(Messages.getLabelString("manage.flow.id")); //$NON-NLS-1$
        tc0.setLabelProvider(new FlowInfoLabelProvider(0));
        tc1.getColumn().setText(Messages.getLabelString("manage.flow.status")); //$NON-NLS-1$
        tc1.setLabelProvider(new FlowInfoLabelProvider(1));
        tc2.getColumn().setText(Messages.getLabelString("manage.flow.time")); //$NON-NLS-1$
        tc2.setLabelProvider(new FlowInfoLabelProvider(2));
        tc3.getColumn().setText(
                Messages.getLabelString("manage.flow.application")); //$NON-NLS-1$
        tc3.setLabelProvider(new FlowInfoLabelProvider(3));
        tc4.getColumn().setText(
                Messages.getLabelString("manage.flow.connection")); //$NON-NLS-1$
        tc4.setLabelProvider(new FlowInfoLabelProvider(4));
        tc5.getColumn().setText(Messages.getLabelString("manage.flow.ack")); //$NON-NLS-1$
        tc5.setLabelProvider(new FlowInfoLabelProvider(5));

        tc6.getColumn().setText(
                Messages.getLabelString("manage.flow.ack.expected")); //$NON-NLS-1$
        tc6.setLabelProvider(new FlowInfoLabelProvider(6));

        tc0.getColumn().setWidth(80);
        tc1.getColumn().setWidth(80);
        tc2.getColumn().setWidth(120);
        tc3.getColumn().setWidth(120);
        tc4.getColumn().setWidth(100);
        tc5.getColumn().setWidth(60);
        tc6.getColumn().setWidth(90);

        new TableColumnViewerSorter(tableViewer, tc0) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                IFlowInfo info1 = adapter1.getFlowInfo();
                IFlowInfo info2 = adapter2.getFlowInfo();
                return new Long(info1.getIdentifier()).compareTo(new Long(info2
                        .getIdentifier()));
            }
        };
        new TableColumnViewerSorter(tableViewer, tc1) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                IFlowInfo info1 = adapter1.getFlowInfo();
                IFlowInfo info2 = adapter2.getFlowInfo();
                return info1.getStatus().compareTo(info2.getStatus());
            }
        };
        new TableColumnViewerSorter(tableViewer, tc2) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                IFlowInfo info1 = adapter1.getFlowInfo();
                IFlowInfo info2 = adapter2.getFlowInfo();
                return info1.getCreationTime().compareTo(
                        info2.getCreationTime());
            }
        };
        new TableColumnViewerSorter(tableViewer, tc3) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                IFlowInfo info1 = adapter1.getFlowInfo();
                IFlowInfo info2 = adapter2.getFlowInfo();
                return info1.getApplication().compareTo(info2.getApplication());
            }
        };

        new TableColumnViewerSorter(tableViewer, tc4) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                IConnectionConfiguration connectionConfiguration1 = (IConnectionConfiguration) adapter1
                        .getAdapter(IConnectionConfiguration.class);
                IConnectionConfiguration connectionConfiguration2 = (IConnectionConfiguration) adapter2
                        .getAdapter(IConnectionConfiguration.class);
                if (connectionConfiguration1 != null
                        && connectionConfiguration2 != null)
                    return connectionConfiguration1.getName().compareTo(
                            connectionConfiguration2.getName());
                return 0;
            }
        };
        new TableColumnViewerSorter(tableViewer, tc5) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                int ack = adapter1.getFlowInfo().getAckCount();
                int ackOther = adapter2.getFlowInfo().getAckCount();
                if (ack < ackOther)
                    return -1;
                if (ack > ackOther)
                    return 1;
                return 0;
            }
        };
        new TableColumnViewerSorter(tableViewer, tc6) {

            @Override
            protected int doCompare(Viewer viewer, Object e1, Object e2) {
                FlowInfoAdapter adapter1 = (FlowInfoAdapter) e1;
                FlowInfoAdapter adapter2 = (FlowInfoAdapter) e2;
                int ackExpected = adapter1.getFlowInfo().getAckCountExpected();
                int ackOtherExpected = adapter2.getFlowInfo()
                        .getAckCountExpected();
                if (ackExpected < ackOtherExpected)
                    return -1;
                if (ackExpected > ackOtherExpected)
                    return 1;
                return 0;
            }
        };
        Activator.getFlowManagerApplicationController().addObserver(this);

    }

    /**
     * The viewer of this part (will be refactored to a tree)
     * 
     * @return
     */
    public ColumnViewer getViewer() {
        return tableViewer;
    }

    /**
     * called internaly.
     */
    public void dispose() {
        Activator.getFlowManagerApplicationController().deleteObserver(this);
        if (!tableViewer.getTable().isDisposed()) {
            tableViewer.getTable().dispose();
            tableViewer.getContentProvider().dispose();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ConnectionEvent) {
            ConnectionEvent event = (ConnectionEvent) arg;
            IConnectionConfiguration connectionConfiguration = event
                    .getConnectionConfigurationContext();

            UpdateGuiStateRunnable runnable = new UpdateGuiStateRunnable();

            switch (event.getType()) {
            case FlowManagerEvent.FLOWMANAGER_VIEW_CHANGE:
                this.activeConnectionConfigurationContext = connectionConfiguration;
                JobUtils.runSafe(runnable);
                break;
            case FlowManagerEvent.FLOWMANAGER_APPLICATION_CHANGED:
                break;
            case FlowManagerEvent.FLOWS_RECEIVED:
                if (this.activeConnectionConfigurationContext != null
                        && this.activeConnectionConfigurationContext
                                .equals(connectionConfiguration)) {
                    JobUtils.runSafe(runnable);
                }
                break;
            case FlowManagerEvent.FLOWMANAGER_UNREGISTERED:
                if (this.activeConnectionConfigurationContext != null
                        && this.activeConnectionConfigurationContext
                                .equals(connectionConfiguration)) {
                    ClearTableRunnable clearTableRunnable = new ClearTableRunnable();
                    JobUtils.runSafe(clearTableRunnable);
                }
                break;
            }
        }
    }

    private final class ClearTableRunnable implements Runnable {

        public synchronized void run() {
            List<FlowInfoAdapter> nullInput = new ArrayList<FlowInfoAdapter>();
            tableViewer.setInput(nullInput);
            tableViewer.refresh();
        }
    }

    private final class UpdateGuiStateRunnable implements Runnable {

        @Override
        public synchronized void run() {
            try {

                if (PlatformUI.getWorkbench().isClosing()) {
                    return;
                }
                IEditorPart editor = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage()
                        .getActiveEditor();
                if (editor == null) {
                    // clear it
                    tableViewer.setInput(new ArrayList<FlowInfoAdapter>());
                    tableViewer.refresh();
                }
                if (editor instanceof FlowManagerEditor) {
                    // always update according to the currently active editor.
                    IConnectionConfiguration connectionConfigurationOfTheEditor = (IConnectionConfiguration) editor
                            .getEditorInput().getAdapter(
                                    IConnectionConfiguration.class);

                    List<IFlowInfo> flows = Activator
                            .getFlowManagerApplicationController()
                            .getFlowManagerRepository().getFlowInfos(
                                    connectionConfigurationOfTheEditor);

                    List<FlowInfoAdapter> adapters = FlowManagerAdapterFactory
                            .createFlowInfoAdapters(
                                    connectionConfigurationOfTheEditor, flows);
                    tableViewer.setInput(adapters);
                    tableViewer.refresh();
                }
            } catch (Throwable t) {
                log.error("Update table flow results table failed!", t);
            }
        }
    }

    // private final class UpdateGuiStateRunnableForConnection implements
    // Runnable {
    //
    // private final IConnection connection;
    //
    // public UpdateGuiStateRunnableForConnection(IConnection connection) {
    // this.connection = connection;
    // }
    //
    // @Override
    // public void run() {
    // updateStateOfGuiComponents(connection);
    // }
    // }

    // private void updateStateOfGuiComponents(IConnection connection) {
    // // the enabled state is not bound with the connection state.
    // // it is bound with the flowManager state
    // IFlowManagerRepository repository = Activator
    // .getFlowManagerApplicationController()
    // .getFlowManagerRepository();
    // boolean stateIsEnabled = true;
    // if (connection != null) {
    // if (!contorller.isFlowManagerRegistered(connection)) {
    // stateIsEnabled = false;
    // }
    // } else {
    // stateIsEnabled = false;
    // }
    // if (!stateIsEnabled) {
    // if (!tableViewer.getTable().isDisposed()) {
    // this.tableViewer.setInput(null);
    // }
    // } else {
    // if (!tableViewer.getTable().isDisposed()) {
    // this.tableViewer.getTable().setEnabled(true);
    // }
    // }
    // }

    public IConnectionConfiguration getActiveConnectionConfigurationContext() {
        return activeConnectionConfigurationContext;
    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDoubleClick(MouseEvent e) {

    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseDown(MouseEvent e) {
        part.setFocus();
    }

    /**
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void mouseUp(MouseEvent e) {

    }
}
