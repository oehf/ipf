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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.console.OutputConsole;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;

/**
 * Implementation of {@link IMBeanNodeEditor} for a group of
 * {@link MBeanOperationEditor} objects.
 * 
 * @author Mitko Kolev
 */
public class MBeanOperationsGroupEditor implements IMBeanNodeEditor {

    private final MBeanOperationsGroupNode operationsGroupNode;

    private Group compositeOperationsEditor;

    private final MBeanOperationNode selectionNode;

    private final List<MBeanOperationEditor> editors;

    private final OutputConsole console;

    public MBeanOperationsGroupEditor(OutputConsole console,
            MBeanOperationsGroupNode operationsGroupNode) {
        this(console, operationsGroupNode, null);
    }

    public MBeanOperationsGroupEditor(OutputConsole console,
            MBeanOperationsGroupNode operationsGroupNode,
            MBeanOperationNode selectionNode) {
        this.operationsGroupNode = operationsGroupNode;
        this.console = console;
        this.selectionNode = selectionNode;
        this.editors = new ArrayList<MBeanOperationEditor>();
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        compositeOperationsEditor = new Group(parent, SWT.NONE);
        compositeOperationsEditor.setText(operationsGroupNode.getParent()
                .getName());
        compositeOperationsEditor.setLayout(new GridLayout(1, true));
        compositeOperationsEditor.setLayoutData(new GridData(SWT.FILL,
                SWT.FILL, true, false, 1, 1));

        Node[] operationsNodes = operationsGroupNode.getChildren();
        if (operationsNodes != null) {
            MBeanOperationEditor editorToDecorate = null;
            // fill all operations in
            for (int t = 0; t < operationsNodes.length; t++) {
                Node node = operationsNodes[t];
                if (node instanceof MBeanOperationNode) {
                    MBeanOperationNode operationNoode = (MBeanOperationNode) node;
                    MBeanOperationEditor operationEditor = new MBeanOperationEditor(
                            console, operationNoode);
                    editors.add(operationEditor);
                    operationEditor.createControl(compositeOperationsEditor);
                    if (operationNoode == selectionNode) {
                        editorToDecorate = operationEditor;
                    }
                }
            }
            if (editorToDecorate != null) {
                editorToDecorate.decorateContentsAsSelected();
            }

        }

    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getTargetNode()
     */
    @Override
    public MBeanNode getTargetNode() {
        return operationsGroupNode;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getControl()
     */
    @Override
    public Composite getControl() {
        return compositeOperationsEditor;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#decorateContentsAsSelected()
     */
    @Override
    public boolean decorateContentsAsSelected() {

        return false;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#valueChanged()
     */
    @Override
    public void valueChanged(JMXExplorerEvent event) {
        for (MBeanOperationEditor editor : editors) {
            editor.valueChanged(event);
        }

    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (MBeanOperationEditor editor : editors) {
            editor.setEnabled(enabled);
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#dispose()
     */
    @Override
    public void dispose() {
        for (MBeanOperationEditor editor : editors) {
            editor.dispose();
        }
    }

}
