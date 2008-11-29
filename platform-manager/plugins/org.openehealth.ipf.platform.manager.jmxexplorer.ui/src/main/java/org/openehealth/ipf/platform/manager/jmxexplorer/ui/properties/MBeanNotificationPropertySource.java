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

import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Represents property source of MBeanNotification.
 * 
 * @see MBeanNotificationInfo
 * 
 * @author Mitko Kolev
 */
public class MBeanNotificationPropertySource extends
        MBeanDescritorPropertySource {

    private final static String DESCRIPTION_KEY = "notification.description";

    private final static String NAME_KEY = "notification.name";

    private final static String NOTIFICATION_TYPES_KEY = "notification.types";

    private final static String descriptionDisplayName;

    private final static String nameDisplayName;

    private final static String category;

    private final static String notificationTypesDisplayName;

    static {
        descriptionDisplayName = Messages.getLabelString(DESCRIPTION_KEY);
        nameDisplayName = Messages.getLabelString(NAME_KEY);
        category = Messages.getLabelString("notification.category");
        notificationTypesDisplayName = Messages
                .getLabelString(NOTIFICATION_TYPES_KEY);
    }

    private final MBeanNotificationInfo notificationInfo;

    private final ObjectName objectName;

    private final IConnectionConfiguration connectionConfiguration;

    /**
     * Constructs MBeanOperationPropertySource
     * 
     * @param connectionConfiguration
     *            the connection to which this opeartion belongs
     * @param objectName
     *            the ObjectName of the MBean to which this property belongs
     * @param attributeInfo
     *            the AttributeInfo for this property
     */
    public MBeanNotificationPropertySource(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanNotificationInfo notificationInfo) {
        super(notificationInfo.getDescriptor());
        this.notificationInfo = notificationInfo;
        this.objectName = objectName;
        this.connectionConfiguration = connectionConfiguration;
    }

    /**
     * @return Returns the connection to which this operation property source
     *         belongs
     */
    public IConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    /**
     * Returns the objectName of the MBean for this operation
     * 
     * @return
     */
    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * Returns the MBeanNotificationInfo of the MBean for this notification
     * 
     * @return
     */
    public MBeanNotificationInfo getOperationInfo() {
        return notificationInfo;
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
        PropertyDescriptor pd = new PropertyDescriptor(DESCRIPTION_KEY,
                descriptionDisplayName);
        pd.setCategory(category);
        descriptors.add(pd);

        pd = new PropertyDescriptor(NAME_KEY, nameDisplayName);
        // register keys for which this PropertySource is a match
        pd.setFilterFlags(new String[] { this.objectName.toString(), category,
                descriptionDisplayName });
        pd.setCategory(category);
        descriptors.add(pd);

        pd = new PropertyDescriptor(NOTIFICATION_TYPES_KEY,
                notificationTypesDisplayName);
        pd.setCategory(category);
        descriptors.add(pd);

        descriptors.addAll(Arrays.asList(super.getPropertyDescriptors()));
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof String) {
            String idValue = (String) id;
            if (idValue.equals(DESCRIPTION_KEY)) {
                return notificationInfo.getDescription();
            } else if (idValue.equals(NAME_KEY)) {
                notificationInfo.getName();
                return notificationInfo.getName();
            } else if (idValue.equals(NOTIFICATION_TYPES_KEY)) {
                notificationInfo.getNotifTypes();
                String[] notificationTypes = notificationInfo.getNotifTypes();
                if (notificationTypes != null) {
                    StringBuffer buffer = new StringBuffer("[ ");

                    for (int t = 0; t < notificationTypes.length; t++) {
                        buffer.append(notificationTypes[t]);
                        if (t < notificationTypes.length - 1)
                            buffer.append(",");
                    }
                    buffer.append(" ]");
                    return buffer.toString();
                }
                return "";
            }
        }
        return super.getPropertyValue(id);
    }
}
