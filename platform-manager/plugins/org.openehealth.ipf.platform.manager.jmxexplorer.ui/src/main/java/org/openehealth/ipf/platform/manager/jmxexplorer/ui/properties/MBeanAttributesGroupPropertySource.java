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

import java.io.IOException;
import java.util.ArrayList;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.OperationsException;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributesGroupNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Constructs IPropertySource for a set of MBeanAttributes (
 * {@link MBeanAttributesGroupNode}). Used in the properties view.
 * 
 * @see MBeanAttributesGroupNode
 * @see MBeanAttributeInfo
 * @see IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class MBeanAttributesGroupPropertySource implements IPropertySource2 {

    private final ObjectName objectName;

    private final MBeanInfo mbeanInfo;

    private final IConnectionConfiguration connectionConfiguration;

    public MBeanAttributesGroupPropertySource(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanInfo mbeanInfo) {
        this.objectName = objectName;
        this.mbeanInfo = mbeanInfo;
        this.connectionConfiguration = connectionConfiguration;
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
        MBeanAttributeInfo[] infos = mbeanInfo.getAttributes();
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
        for (int t = 0; t < infos.length; t++) {
            MBeanAttributeInfo attributeInfo = infos[t];
            PropertyDescriptor pd;
            if (attributeInfo.isWritable()) {
                pd = new MBeanSimpleWritableAttributePropertyDescriptor(
                        connectionConfiguration, objectName, attributeInfo,
                        attributeInfo, attributeInfo.getName());
            } else {
                pd = new PropertyDescriptor(attributeInfo, attributeInfo
                        .getName());

            }
            descriptors.add(pd);
        }

        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof MBeanAttributeInfo) {
            MBeanAttributeInfo ai = (MBeanAttributeInfo) id;
            IJMXExplorerMediator mediator = Activator.getDefault()
                    .getJMXExplorerMediator();

            try {
                IMBeanServerConnectionFacade facade = mediator
                        .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);
                Object attribute = facade.readAttributeValue(
                        connectionConfiguration, objectName, ai.getName());
                return MBeanUtils.getPropertySourceObject(attribute, ai);
            } catch (IOException e) {
                return "";
            } catch (OperationsException e) {
                return "";

            } catch (MBeanException e) {
                return "";

            }
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
