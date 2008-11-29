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

import org.eclipse.swt.widgets.Composite;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;

/**
 * Base Interface representing editting functionality of a MBeanNode.
 * 
 * @author Mitko Kolev
 */
public interface IMBeanNodeEditor {

    /**
     * Creates the UI
     * 
     * @param parent
     */
    public void createControl(Composite parent);

    /**
     * Returns the MBeanNode which is edited.
     * 
     * @return
     */
    public MBeanNode getTargetNode();

    /**
     * Returns the UI control
     * 
     * @return
     */
    public Composite getControl();

    /**
     * Returns if the control should be decorated as "selected"
     * 
     * @return
     */
    public boolean decorateContentsAsSelected();

    /**
     * Should update the control accordingly.
     * 
     * @param event
     */
    public void valueChanged(JMXExplorerEvent event);

    /**
     * Enables or disables the control
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled);

    /**
     * Clean-up logic
     */
    public void dispose();
}
