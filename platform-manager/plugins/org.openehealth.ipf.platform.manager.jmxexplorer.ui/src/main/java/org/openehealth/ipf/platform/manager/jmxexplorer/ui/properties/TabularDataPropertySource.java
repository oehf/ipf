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
import java.util.Set;

import javax.management.MBeanFeatureInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * 
 * Provides IProperty source for @link{TabularData}.
 * 
 * @see TabularData
 * @see IPropertySource
 * 
 * @author Mitko Kolev
 */
public class TabularDataPropertySource implements IPropertySource2 {

    private final TabularData tabularData;

    private final MBeanFeatureInfo featureInfo;

    public TabularDataPropertySource(TabularData tabularData,
            MBeanFeatureInfo featureInfo) {
        this.tabularData = tabularData;
        this.featureInfo = featureInfo;
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
    @SuppressWarnings("unchecked")
    public IPropertyDescriptor[] getPropertyDescriptors() {

        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        Set keys = tabularData.keySet();
        for (Object key : keys) {
            PropertyDescriptor pd = new PropertyDescriptor(key, tabularData
                    .getTabularType().getDescription());
            pd.setCategory(tabularData.getTabularType().getTypeName());
            descriptors.add(pd);
        }
        IPropertyDescriptor[] descriptorsArray = new IPropertyDescriptor[descriptors
                .size()];
        return descriptors.toArray(descriptorsArray);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getPropertyValue(Object id) {
        if (id instanceof List) {
            List list = (List) id;
            Object[] arrayKey = list.toArray();
            CompositeData data = tabularData.get(arrayKey);
            if (data != null) {
                Object object = MBeanUtils.getPropertySourceObject(data,
                        this.featureInfo);
                return object;

            }
        }
        return "";
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