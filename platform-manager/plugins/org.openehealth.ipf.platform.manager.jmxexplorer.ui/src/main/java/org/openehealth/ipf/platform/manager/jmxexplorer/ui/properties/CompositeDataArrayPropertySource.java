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

import javax.management.MBeanFeatureInfo;
import javax.management.openmbean.CompositeData;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Property source for the composite array. Could contain nested property
 * sources, if the array elements are data which are supported as
 * IPropertySource2
 * 
 * @see IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class CompositeDataArrayPropertySource implements IPropertySource2 {

    private final CompositeData[] compositeDataArray;

    private final MBeanFeatureInfo featureInfo;

    public CompositeDataArrayPropertySource(Object parent,
            CompositeData[] compositeDataArray, MBeanFeatureInfo featureInfo) {
        this.compositeDataArray = compositeDataArray;
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
    public IPropertyDescriptor[] getPropertyDescriptors() {

        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        for (int t = 0; t < compositeDataArray.length; t++) {
            CompositeDataPropertySource cdps = new CompositeDataPropertySource(
                    compositeDataArray[t], featureInfo);

            IPropertyDescriptor descriptor = new PropertyDescriptor(cdps,
                    compositeDataArray[t].getCompositeType().getTypeName()
                            + "[" + t + "]");
            descriptors.add(descriptor);
        }
        IPropertyDescriptor[] descriptorsArray = new IPropertyDescriptor[descriptors
                .size()];
        return descriptors.toArray(descriptorsArray);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof CompositeDataPropertySource) {
            CompositeDataPropertySource cdps = (CompositeDataPropertySource) id;
            // String value =
            // cdps.getCompositeData().getCompositeType().getTypeName();
            return cdps;
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