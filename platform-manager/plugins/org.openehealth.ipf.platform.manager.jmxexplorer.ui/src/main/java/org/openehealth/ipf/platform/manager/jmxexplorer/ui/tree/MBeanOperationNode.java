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
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanOperationPropertySource;

/**
 * Node which represents a MBean operation.
 * 
 * @author Mitko Kolev
 */
public class MBeanOperationNode extends MBeanNode {

    /**
     * @param name
     * @param objectName
     */

    private MBeanOperationInfo operationInfo;

    private final IPropertySource propertySouce;

    public MBeanOperationNode(String name, ObjectName objectName,
            MBeanInfo mbeanInfo, MBeanOperationInfo operationInfo,
            IConnectionConfiguration connectionConfiguration) {
        super(name, objectName, mbeanInfo);
        this.operationInfo = operationInfo;
        this.propertySouce = new MBeanOperationPropertySource(
                connectionConfiguration, objectName, operationInfo);
    }

    public MBeanOperationInfo getOperationInfo() {
        return operationInfo;
    }

    public void setOperationInfo(MBeanOperationInfo operationInfo) {
        this.operationInfo = operationInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter) {
        if (adapter == IPropertySource2.class
                || adapter == IPropertySource.class) {
            return propertySouce;
        }
        return super.getAdapter(adapter);
    }

}
