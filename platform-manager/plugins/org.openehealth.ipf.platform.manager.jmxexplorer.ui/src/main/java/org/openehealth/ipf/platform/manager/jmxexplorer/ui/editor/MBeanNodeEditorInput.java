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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Editor input for editting MBean
 * 
 * @see IEditorInput
 * @see MBeanNode
 * 
 * @author Mitko Kolev
 */
public class MBeanNodeEditorInput implements IEditorInput {

    private final MBeanNode node;

    private static final String tooltipText;
    static {
        tooltipText = Messages
                .getLabelString("jmx.explorer.editor.tooltip.text");
    }

    public MBeanNodeEditorInput(MBeanNode node) {
        this.node = node;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#exists()
     */
    @Override
    public boolean exists() {
        return false;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {

        return null;
    }

    /**
     * Returns the name of the MBean.
     * 
     * @see org.eclipse.ui.IEditorInput#getName()
     */
    @Override
    public String getName() {
        return this.node.getName();
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getPersistable()
     */
    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    /**
     * @see org.eclipse.ui.IEditorInput#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        return tooltipText;
    }

    /**
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == Node.class) {
            return node;
        }
        return node.getAdapter(adapter);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MBeanNodeEditorInput other = (MBeanNodeEditorInput) obj;

        IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) this
                .getAdapter(IConnectionConfiguration.class);
        IConnectionConfiguration otherConnectionConfiguration = (IConnectionConfiguration) other
                .getAdapter(IConnectionConfiguration.class);

        if (thisConnectionConfiguration == null) {
            if (otherConnectionConfiguration != null)
                return false;
        } else if (!thisConnectionConfiguration
                .equals(otherConnectionConfiguration))
            return false;
        return true;
    }

}
