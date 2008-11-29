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
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;

/**
 * Implementation of {@link IMBeanNodeEditor} for a group of
 * {@link MBeanAttributeEditor} objects.
 * 
 * @author Mitko Kolev
 */
public class MBeanAttributesGroupEditor implements IMBeanNodeEditor {

    private final MBeanAttributesGroupNode attributesGroupNode;

    private Group compositAttributesEditor;

    private final MBeanAttributeNode selectionNode;

    private final List<MBeanAttributeEditor> attributeEditors;

    private final OutputConsole console;

    public MBeanAttributesGroupEditor(OutputConsole console,
            MBeanAttributesGroupNode attributesGroupNode) {
        this(console, attributesGroupNode, null);
    }

    public MBeanAttributesGroupEditor(OutputConsole console,
            MBeanAttributesGroupNode attributesGroupNode,
            MBeanAttributeNode selectionNode) {
        this.attributesGroupNode = attributesGroupNode;
        this.selectionNode = selectionNode;
        this.attributeEditors = new ArrayList<MBeanAttributeEditor>();
        this.console = console;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.GroupEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        compositAttributesEditor = new Group(parent, SWT.NONE);
        compositAttributesEditor.setText(attributesGroupNode.getParent()
                .getName());
        compositAttributesEditor.setLayout(new GridLayout(1, true));
        compositAttributesEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, false, 1, 1));
        Node[] attributeNodes = attributesGroupNode.getChildren();

        if (attributeNodes != null) {
            MBeanAttributeEditor editorToDecorate = null;
            for (int t = 0; t < attributeNodes.length; t++) {
                Node node = attributeNodes[t];
                if (node instanceof MBeanAttributeNode) {
                    MBeanAttributeNode targetNode = (MBeanAttributeNode) node;
                    MBeanAttributeEditor attributeEditor = new MBeanAttributeEditor(
                            console, targetNode);

                    attributeEditor.createControl(compositAttributesEditor);
                    // add the instance for later use;
                    attributeEditors.add(attributeEditor);

                    if (targetNode == selectionNode) {
                        editorToDecorate = attributeEditor;
                    }
                }
                if (editorToDecorate != null) {
                    editorToDecorate.decorateContentsAsSelected();
                }
            }
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.MBeanNodeEditorAbstract#getTargetNode()
     */
    @Override
    public MBeanNode getTargetNode() {
        return attributesGroupNode;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getControl()
     */
    @Override
    public Composite getControl() {
        return compositAttributesEditor;
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
        for (MBeanAttributeEditor editor : attributeEditors) {
            editor.valueChanged(event);
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (MBeanAttributeEditor editor : attributeEditors) {
            editor.setEnabled(enabled);
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#dispose()
     */
    @Override
    public void dispose() {
        for (MBeanAttributeEditor editor : attributeEditors) {
            editor.dispose();
        }
    }
}