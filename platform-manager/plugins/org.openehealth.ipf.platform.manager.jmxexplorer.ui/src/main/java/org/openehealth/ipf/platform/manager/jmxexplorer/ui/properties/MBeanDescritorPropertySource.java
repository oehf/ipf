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

import javax.management.Descriptor;
import javax.management.RuntimeOperationsException;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Provides properties for every javax.management.Descriptor object. Register
 * filters for the PropertyDescriptors's values.
 * 
 * @see Descriptor
 * @see PropertyDescriptor#setFilterFlags(String[])
 * 
 * @author Mitko Kolev
 */
public abstract class MBeanDescritorPropertySource implements IPropertySource2 {

    private final Descriptor targetDescriptor;

    private final static String descriptorCategoryName;

    private final static String DESCRIPTOR_KEY_ID = "descriptor";

    private final static String DESCRIPTOR_VALID_KEY_ID = "descriptor.valid";

    private final static String DESCRIPTOR_VALID_DESCRIPTION_KEY_ID = "descriptor.valid.description";

    private final static String descriptorValidDisplayName;

    private final static String descriptorValidDescription;

    protected static final String INFO_CATEGORY_KEY = "mbean.info.category";

    static {
        descriptorCategoryName = Messages.getLabelString(DESCRIPTOR_KEY_ID);
        descriptorValidDisplayName = Messages
                .getLabelString(DESCRIPTOR_VALID_KEY_ID);
        descriptorValidDescription = Messages
                .getLabelString(DESCRIPTOR_VALID_DESCRIPTION_KEY_ID);
    }

    /**
     * Constructs this propertySource with a target descriptor.
     * 
     * @param targetDescriptor
     */
    public MBeanDescritorPropertySource(Descriptor targetDescriptor) {
        this.targetDescriptor = targetDescriptor;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();

        if (targetDescriptor == null) {
            return null;
        }
        PropertyDescriptor pd = new PropertyDescriptor("Descriptor@"
                + DESCRIPTOR_VALID_KEY_ID, descriptorValidDisplayName);
        pd.setCategory(descriptorCategoryName);
        pd.setDescription(descriptorValidDescription);
        descriptors.add(pd);

        String[] fieldNames = targetDescriptor.getFieldNames();
        if (fieldNames != null) {
            for (int t = 0; t < fieldNames.length; t++) {

                pd = createDescriptorPropertyDescriptor(fieldNames[t]);
                Object filter = getPropertyValue(pd.getId());
                pd.setFilterFlags(new String[] { filter == null ? "" : filter
                        .toString() });
                descriptors.add(pd);
            }
            return descriptors.toArray(new IPropertyDescriptor[descriptors
                    .size()]);
        }
        return null;
    }

    /**
     * The IDs of the property descriptors are Descriptor@<propertyname>
     * Descriptors are in general not editable!
     * 
     * @param propertyName
     * @return
     */
    private PropertyDescriptor createDescriptorPropertyDescriptor(
            String propertyName) {
        PropertyDescriptor pd = new PropertyDescriptor("Descriptor@"
                + propertyName, propertyName);
        pd.setCategory(descriptorCategoryName);
        pd.setAlwaysIncompatible(true);
        Object value = this.getPropertyValue("Descriptor@" + propertyName);
        pd.setDescription(value == null ? "" : value.toString());
        return pd;
    }

    private boolean isDescriptorProperty(Object id) {
        if (id == null)
            return false;
        if (id instanceof String) {
            if (((String) id).startsWith("Descriptor@"))
                return true;
        }
        return false;
    }

    private String getDescriptorPropertyNameFromID(Object ID) {
        if (isDescriptorProperty(ID)) {
            return ID.toString().substring("Descriptor@".length());
        }
        return "";
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (isDescriptorProperty(id)) {
            String key = getDescriptorPropertyNameFromID(id);
            if (key.equals(DESCRIPTOR_VALID_KEY_ID)) {
                boolean valid = false;
                try {
                    valid = targetDescriptor.isValid();
                } catch (RuntimeOperationsException re) {
                    valid = false;
                }
                return valid;
            }
            return targetDescriptor.getFieldValue(key);
        }
        return null;
    }
}
