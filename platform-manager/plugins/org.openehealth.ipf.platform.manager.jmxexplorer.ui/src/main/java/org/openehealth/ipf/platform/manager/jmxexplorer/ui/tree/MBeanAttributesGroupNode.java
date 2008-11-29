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
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanAttributesGroupPropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Node which represents a group of MBean attributes.
 * 
 * @see MBeanAttributesGroupPropertySource
 * 
 * @author Mitko Kolev
 */
public class MBeanAttributesGroupNode extends MBeanNode {

    private final IPropertySource2 propertySouce;

    /**
     * @param name
     */
    public MBeanAttributesGroupNode(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanInfo mbeanInfo) {
        super(Messages.getLabelString("mbean.attributes"), objectName,
                mbeanInfo);
        propertySouce = new MBeanAttributesGroupPropertySource(
                connectionConfiguration, objectName, mbeanInfo);
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
