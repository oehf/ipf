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
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Provides IPropertySource for a collection of Attributes
 * 
 * @author Mitko Kolev
 */
public class MBeanCollectionAttributePropertySource implements IPropertySource2 {

    private final List<Object> objects;

    private final MBeanAttributeInfo attributeInfo;

    public MBeanCollectionAttributePropertySource(List<Object> objects,
            MBeanAttributeInfo attributeInfo) {
        this.objects = objects;
        this.attributeInfo = attributeInfo;
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
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
        for (int t = 0; t < objects.size(); t++) {
            Object thisObject = objects.get(t);
            String type = this.attributeInfo.getType();
            if (thisObject instanceof ObjectName) {
                type = ((ObjectName) thisObject).getKeyProperty("Type");
                if (type == null) {
                    type = ((ObjectName) thisObject).getDomain();
                }
            }
            PropertyDescriptor pd = new PropertyDescriptor(thisObject,
                    this.attributeInfo.getName() + "[" + t + "]");

            pd.setCategory(type);
            descriptors.add(pd);
        }
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        for (int t = 0; t < objects.size(); t++) {
            Object on = objects.get(t);
            if (on.equals(id)) {
                return MBeanUtils.getPropertySourceObject(on, attributeInfo);
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
    }
}
