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
import java.util.Arrays;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Provides PropertySource for an MBean and its descriptor.
 * 
 * @author Mitko Kolev
 */
public class MBeanPropertySource extends MBeanDescritorPropertySource {

    private final static String infoDisplayName;

    private final static String descriptionDisplayName;

    private final static String classNameDisplayName;

    private final static String objectNameStringDisplayName;

    private static final String CLASS_NAME_KEY = "mbean.className";

    private static final String DESCRIPTION_NAME_KEY = "mbean.description";

    private final static String OBJECT_NAME_KEY = "mbean.objectName";

    static {
        objectNameStringDisplayName = Messages.getLabelString(OBJECT_NAME_KEY);
        classNameDisplayName = Messages.getLabelString(CLASS_NAME_KEY);
        descriptionDisplayName = Messages.getLabelString(DESCRIPTION_NAME_KEY);
        infoDisplayName = Messages.getLabelString(INFO_CATEGORY_KEY);
    }

    private final ObjectName objectName;

    private final MBeanInfo mbeanInfo;

    /**
     * Contstructs PropertySource for an MBeanInfo
     * 
     * @param mbeanInfo
     *            the MBeanInfo of the target MBean
     * @param objectName
     *            the ObjectName of the target MBean
     */
    public MBeanPropertySource(MBeanInfo mbeanInfo, ObjectName objectName) {
        // create also its descriptor's properties
        super(mbeanInfo.getDescriptor());
        this.mbeanInfo = mbeanInfo;
        this.objectName = objectName;
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

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        PropertyDescriptor pd = new PropertyDescriptor(CLASS_NAME_KEY,
                classNameDisplayName);
        pd.setCategory(infoDisplayName);
        pd.setDescription(mbeanInfo.getClassName());
        descriptors.add(pd);

        pd = new PropertyDescriptor(DESCRIPTION_NAME_KEY,
                descriptionDisplayName);
        pd.setDescription(mbeanInfo.getDescription());
        pd.setCategory(infoDisplayName);
        descriptors.add(pd);

        pd = new PropertyDescriptor(OBJECT_NAME_KEY,
                objectNameStringDisplayName);
        pd.setCategory(infoDisplayName);
        pd.setDescription(MBeanUtils.createObjectNameDescription(objectName));
        descriptors.add(pd);

        descriptors.addAll(Arrays.asList(super.getPropertyDescriptors()));
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof String) {
            String key = (String) id;
            if (key.equals(CLASS_NAME_KEY)) {
                return mbeanInfo.getClassName();
            } else if (key.equals(DESCRIPTION_NAME_KEY)) {
                return mbeanInfo.getDescription();
            } else if (key.equals(OBJECT_NAME_KEY)) {
                return MBeanUtils.createObjectNameDescription(objectName);
            }
        }
        return super.getPropertyValue(id);
    }
}
