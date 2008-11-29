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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties;

import java.util.ArrayList;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationsGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Constructs IPropertySource for a set of MBeanOperations. Used in the
 * properties view.
 * 
 * @see MBeanOperationsGroupNode
 * @see MBeanOperationInfo
 * @see IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class MBeanOperationsGroupPropertySource implements IPropertySource2 {

    private final MBeanInfo mbeanInfo;

    public MBeanOperationsGroupPropertySource(MBeanInfo mbeanInfo) {
        this.mbeanInfo = mbeanInfo;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource2#isPropertyResettable(java.lang.Object)
     */
    @Override
    public boolean isPropertyResettable(Object id) {
        return false;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource2#isPropertySet(java.lang.Object)
     */
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    @Override
    public Object getEditableValue() {
        return null;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        MBeanOperationInfo[] infos = mbeanInfo.getOperations();
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
        for (int t = 0; t < infos.length; t++) {
            MBeanOperationInfo operationInfo = infos[t];
            PropertyDescriptor pd = new PropertyDescriptor(operationInfo,
                    operationInfo.getName());
            pd.setDescription(operationInfo.getDescription());
            descriptors.add(pd);
        }

        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof MBeanOperationInfo) {
            MBeanOperationInfo oi = (MBeanOperationInfo) id;
            return MBeanUtils.buildOperationSignature(oi);
        }
        return null;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
     */
    @Override
    public void resetPropertyValue(Object id) {

    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
    }
}
