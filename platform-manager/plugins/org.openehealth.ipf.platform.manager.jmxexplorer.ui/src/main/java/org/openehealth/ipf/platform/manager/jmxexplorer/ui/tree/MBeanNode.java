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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.openehealth.ipf.platform.manager.connection.ui.tree.Node;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanPropertySource;

/**
 * Node which represents a MBean element.
 * 
 * @author Mitko Kolev
 */
public class MBeanNode extends Node {

    private MBeanInfo mbeanInfo;

    private final IPropertySource2 propertySouce;

    /**
     * Creates mbean node with the given Name and ObjectName
     * 
     * @param name
     */
    public MBeanNode(String name, ObjectName objectName, MBeanInfo mbeanInfo) {
        super(name);
        this.addObjectName(objectName);
        this.mbeanInfo = mbeanInfo;
        this.propertySouce = new MBeanPropertySource(mbeanInfo, objectName);
    }

    /**
     * The MBean nodes have only one object Name
     * 
     * @return
     */
    public ObjectName getObjectName() {
        return this.getObjectNames().get(0);
    }

    public MBeanInfo getMbeanInfo() {
        return mbeanInfo;
    }

    public void setMbeanInfo(MBeanInfo mbeanInfo) {
        this.mbeanInfo = mbeanInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource2.class
                || adapter == IPropertySource.class) {
            return propertySouce;
        } else if (adapter == MBeanNode.class) {
            return this;
        }
        return super.getAdapter(adapter);
    }
}
