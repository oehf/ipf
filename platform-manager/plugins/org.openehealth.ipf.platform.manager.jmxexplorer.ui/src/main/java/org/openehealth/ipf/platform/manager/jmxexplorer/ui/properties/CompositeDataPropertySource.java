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
import java.util.Set;

import javax.management.MBeanFeatureInfo;
import javax.management.openmbean.CompositeData;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Property source for the {link@ CompositeData} type.
 * 
 * @see CompositeData
 * @see IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class CompositeDataPropertySource implements IPropertySource2 {

    private final CompositeData compositeData;

    private final MBeanFeatureInfo featureInfo;

    /**
     * Initializes the CompositeDataPropertySource with compositeData and
     * featureInfo
     * 
     * @param compositeData
     * @param featureInfo
     *            the MBeanFeature info of the MBean operation, attribute or
     *            notification
     * 
     * @see MBeanFeatureInfo
     */
    public CompositeDataPropertySource(CompositeData compositeData,
            MBeanFeatureInfo featureInfo) {
        this.compositeData = compositeData;
        this.featureInfo = featureInfo;
    }

    /**
     * Returns this composite data object associated with this
     * CompositeDataPropertySource
     * 
     * @return
     */
    public CompositeData getCompositeData() {
        return compositeData;
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
        Set<String> keys = compositeData.getCompositeType().keySet();

        for (String key : keys) {
            PropertyDescriptor pd = new PropertyDescriptor(key, compositeData
                    .getCompositeType().getDescription(key));
            pd.setCategory(compositeData.getCompositeType().getTypeName());
            descriptors.add(pd);
        }
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof String) {
            String key = (String) id;
            Object value = compositeData.get(key);
            Object object = MBeanUtils.getPropertySourceObject(value,
                    this.featureInfo);
            return object;
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