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

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanAttributePropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Node which visualizes MBean Attribute
 * 
 * @author Mitko Kolev
 */
public class MBeanAttributeNode extends MBeanNode {

    private MBeanAttributeInfo attributeInfo;

    private final IPropertySource propertySouce;

    /**
     * @param name
     * @param objectName
     */
    public MBeanAttributeNode(String name, ObjectName objectName,
            MBeanInfo mbeanInfo, MBeanAttributeInfo attributeInfo,
            IConnectionConfiguration connectionConfiguration) {
        super(name, objectName, mbeanInfo);
        this.attributeInfo = attributeInfo;
        this.propertySouce = new MBeanAttributePropertySource(
                connectionConfiguration, objectName, attributeInfo);
    }

    public MBeanAttributeInfo getAttributeInfo() {
        return attributeInfo;
    }

    public void setAttributeInfo(MBeanAttributeInfo attributeInfo) {
        this.attributeInfo = attributeInfo;
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

    /**
     * Performs throughout check if the attribute can be changed.
     * 
     * @return false for attributes which are complex types or can not be
     *         changed.
     */
    public boolean isWritable() {
        boolean isSimpleWritable = MBeanUtils.isSimpleWritableType(
                getObjectName(), attributeInfo);
        return isSimpleWritable;
    }

}
