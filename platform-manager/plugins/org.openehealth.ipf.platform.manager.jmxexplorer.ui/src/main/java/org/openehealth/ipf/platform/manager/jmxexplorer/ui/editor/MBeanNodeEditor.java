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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor;

import java.util.Observable;
import java.util.Observer;

import javax.management.ObjectName;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.console.OutputConsole;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;

/**
 * Represents all attributes and operation of a node. Populates the Editor
 * component.
 * 
 * @author Mitko Kolev
 */
public class MBeanNodeEditor extends EditorPart implements Observer {

    private ScrolledComposite sc;

    private IMBeanNodeEditor editor;

    private OutputConsole console;

    public MBeanNodeEditor() {
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        setSite(site);
        setInput(input);
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) input
                .getAdapter(IConnectionConfiguration.class);
        setPartName(connectionConfiguration.getName());
        console = new OutputConsole(connectionConfiguration);
        Activator.getDefault().getJMXExplorerMediator().addObserver(this);

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        IEditorInput editorInput = this.getEditorInput();
        Node node = (Node) editorInput.getAdapter(Node.class);
        Composite editorControl = new Composite(parent, SWT.NONE);
        editorControl.setFocus();
        initializeEditorControl(editorControl, node);

    }

    @Override
    public void setFocus() {
        // allow the scrolled composite to scroll itself
        sc.setFocus();
    }

    private void initializeEditorControl(Composite parent, Node node) {

        parent.setLayout(new GridLayout(1, true));

        sc = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setLayout(new GridLayout(1, false));
        sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        Composite content = new Composite(sc, SWT.NONE);
        content.setLayout(new GridLayout(1, false));
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        if (node instanceof MBeanOperationNode) {
            if (node.getParent() instanceof MBeanOperationsGroupNode) {
                editor = new MBeanOperationsGroupEditor(console,
                        (MBeanOperationsGroupNode) node.getParent(),
                        (MBeanOperationNode) node);
            }

        } else if (node instanceof MBeanOperationsGroupNode) {

            editor = new MBeanOperationsGroupEditor(console,
                    (MBeanOperationsGroupNode) node);

        } else if (node instanceof MBeanAttributeNode) {
            if (node.getParent() instanceof MBeanAttributesGroupNode) {
                editor = new MBeanAttributesGroupEditor(console,
                        (MBeanAttributesGroupNode) node.getParent(),
                        (MBeanAttributeNode) node);
            }

        } else if (node instanceof MBeanAttributesGroupNode) {
            editor = new MBeanAttributesGroupEditor(console,
                    (MBeanAttributesGroupNode) node);
        }
        if (editor != null) {
            editor.createControl(content);
        }
        Point size = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        sc.setMinSize(size);
        sc.setContent(content);
    }

    @Override
    public void dispose() {
        Activator.getDefault().getJMXExplorerMediator().deleteObserver(this);
        // the editor is not disposed automatically
        if (editor != null)
            editor.dispose();
        super.dispose();
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof JMXExplorerEvent) {
            // respond to attribute change events
            JMXExplorerEvent event = (JMXExplorerEvent) arg;
            ObjectName targetObjectName = event.getObjectName();
            IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this
                    .getEditorInput()
                    .getAdapter(IConnectionConfiguration.class);
            switch (event.getType()) {
            case JMXExplorerEvent.INVOKE_OPERATION_RESULT:
            case JMXExplorerEvent.INVOKE_OPERATION_ERROR:
            case JMXExplorerEvent.CHANGE_ATTRIBUTE_VALUE_ERROR:
            case JMXExplorerEvent.ATTRIBUTE_VALUE_CHANGED:
                if (connectionConfiguration.equals(event
                        .getConnectionConfigurationContext())) {
                    // if this is the same Attribute, update it
                    if (editor != null) {
                        if (editor.getTargetNode().getObjectName().equals(
                                targetObjectName)) {
                            UpdateRunnable runnable = new UpdateRunnable(event);
                            JobUtils.runSafe(runnable);
                        }
                    }
                }
                break;
            }
        } else if (arg instanceof ConnectionEvent) {
            // respond to connection events
            ConnectionEvent event = (ConnectionEvent) arg;
            IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this
                    .getEditorInput()
                    .getAdapter(IConnectionConfiguration.class);
            if (connectionConfiguration.equals(event
                    .getConnectionConfigurationContext())) {
                switch (event.getType()) {
                case ConnectionEvent.CONNECTION_REMOVED:
                case ConnectionEvent.JMX_CONNECTION_CLOSED:
                    SetEnabledRunnable runnableDisabled = new SetEnabledRunnable(
                            false);
                    JobUtils.runSafe(runnableDisabled);
                    break;
                case ConnectionEvent.JMX_CONNECTION_OPEN:
                    SetEnabledRunnable runnableEnable = new SetEnabledRunnable(
                            true);
                    JobUtils.runSafe(runnableEnable);
                    break;
                }
            }
        }
    }

    protected class SetEnabledRunnable implements Runnable {

        private final boolean enabled;

        public SetEnabledRunnable(boolean update) {
            this.enabled = update;
        }

        public void run() {
            editor.setEnabled(enabled);
        }
    }

    protected class UpdateRunnable implements Runnable {

        JMXExplorerEvent event;

        public UpdateRunnable(JMXExplorerEvent event) {
            this.event = event;
        }

        public void run() {
            editor.valueChanged(event);
        }
    }

}
