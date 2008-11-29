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

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.views.TreeFilter;

/**
 * The public final fields are used to get the property's value from this
 * propertySource which is returned as adapter of every attribute node.
 * <p>
 * <b>IMPORTANT!</b> Uses setFilterFlags to register keys for which this
 * PropertySource is a match in a regular expression.
 * <p>
 * 
 * @see PropertyDescriptor#setFilterFlags(String[])
 * @see TreeFilter
 * @author Mitko Kolev
 */
public class MBeanAttributePropertySource extends MBeanDescritorPropertySource {

    /**
     * Key for the description property of this attribute
     */
    public final static String DESCRIPTION_KEY = "attribute.description";

    /**
     * Key for the type property of this attribute
     */
    public final static String TYPE_KEY = "attribute.type";

    /**
     * Key for the name property of this attribute
     */
    public final static String NAME_KEY = "attribute.name";

    /**
     * Key for the writable property of this attribute
     */
    public final static String WRITABLE_KEY = "attribute.writable";

    /**
     * Key for the isIs property of this attribute
     */
    public final static String IS_IS_KEY = "attribute.isIs";

    /**
     * Key for the value property of this attribute
     */
    public final static String VALUE_KEY = "attribute.value";

    /**
     * Key for the unavailable value
     */
    public final static String ATTRIBUTE_NOT_AVAILABLE_KEY = "attribute.not.available";

    // final properties for internal use
    private final static String ATTRIBUTE_GROUP_KEY = "attributes.group";

    private final static String EDITABLE_GROUP_KEY = "attributes.editable";

    private final static String descriptionDisplayName;

    private final static String typeDisplayName;

    private final static String nameDisplayName;

    private final static String READABLE_KEY = "attribute.readable";

    private final static String readableDisplayName;

    private final static String writableDisplayName;

    private final static String isIsDisplayName;

    private final static String propertiesCategoryDisplayName;

    private final static String editableCategoryDisplayName;

    private final static String attributeNotAvailableDisplayName;

    private final static Log log = LogFactory
            .getLog(MBeanDescritorPropertySource.class);

    static {
        descriptionDisplayName = Messages.getLabelString(DESCRIPTION_KEY);
        typeDisplayName = Messages.getLabelString(TYPE_KEY);
        nameDisplayName = Messages.getLabelString(NAME_KEY);
        readableDisplayName = Messages.getLabelString(READABLE_KEY);
        writableDisplayName = Messages.getLabelString(WRITABLE_KEY);
        isIsDisplayName = Messages.getLabelString(IS_IS_KEY);
        propertiesCategoryDisplayName = Messages
                .getLabelString(ATTRIBUTE_GROUP_KEY);
        editableCategoryDisplayName = Messages
                .getLabelString(EDITABLE_GROUP_KEY);
        attributeNotAvailableDisplayName = Messages
                .getLabelString(ATTRIBUTE_NOT_AVAILABLE_KEY);
    }

    private final IConnectionConfiguration connectionConfiguration;

    private final ObjectName objectName;

    private final MBeanAttributeInfo attributeInfo;

    /**
     * Constructs MbeanAttributePropertySource
     * 
     * @param connectionConfiguration
     *            the connection to which this property belongs
     * @param objectName
     *            the ObjectName of the MBean to which this property belongs
     * @param attributeInfo
     *            the AttributeInfo for this property
     */
    public MBeanAttributePropertySource(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanAttributeInfo attributeInfo) {
        super(attributeInfo.getDescriptor());
        this.objectName = objectName;
        this.attributeInfo = attributeInfo;
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
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();
        PropertyDescriptor pd = new PropertyDescriptor(DESCRIPTION_KEY,
                descriptionDisplayName);
        pd.setAlwaysIncompatible(true);
        pd.setCategory(propertiesCategoryDisplayName);
        descriptors.add(pd);

        pd = new PropertyDescriptor(TYPE_KEY, typeDisplayName);
        pd.setCategory(propertiesCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(NAME_KEY, nameDisplayName);
        pd.setFilterFlags(new String[] { propertiesCategoryDisplayName,
                nameDisplayName });
        pd.setAlwaysIncompatible(true);
        pd.setCategory(propertiesCategoryDisplayName);
        descriptors.add(pd);

        pd = new PropertyDescriptor(READABLE_KEY, readableDisplayName);
        pd.setCategory(propertiesCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(WRITABLE_KEY, writableDisplayName);
        pd.setCategory(propertiesCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        if (MBeanUtils.isSimpleWritableType(objectName, attributeInfo)) {
            pd = new MBeanSimpleWritableAttributePropertyDescriptor(
                    connectionConfiguration, objectName, attributeInfo,
                    VALUE_KEY, attributeInfo.getName());
        } else {
            pd = new PropertyDescriptor(VALUE_KEY, attributeInfo.getName());
        }
        pd.setAlwaysIncompatible(true);
        pd.setCategory(editableCategoryDisplayName);
        pd.setFilterFlags(new String[] { this.objectName.toString(),
                propertiesCategoryDisplayName, attributeInfo.getName() });
        descriptors.add(pd);

        pd = new PropertyDescriptor(IS_IS_KEY, isIsDisplayName);
        pd.setCategory(propertiesCategoryDisplayName);
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
                return attributeInfo.getDescription();
            } else if (idValue.equals(NAME_KEY)) {
                return attributeInfo.getName();
            } else if (idValue.equals(TYPE_KEY)) {
                return attributeInfo.getType();
            } else if (idValue.equals(READABLE_KEY)) {
                return new Boolean(attributeInfo.isReadable());
            } else if (idValue.equals(WRITABLE_KEY)) {
                return new Boolean(attributeInfo.isWritable());
            } else if (idValue.equals(IS_IS_KEY)) {
                return new Boolean(attributeInfo.isIs());
            } else if (idValue.equals(VALUE_KEY)) {

                IJMXExplorerMediator mediator = Activator.getDefault()
                        .getJMXExplorerMediator();
                try {
                    if (!attributeInfo.isReadable()) {
                        return "";
                    }
                    IMBeanServerConnectionFacade facade = mediator
                            .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);

                    Object attribute = facade.readAttributeValue(
                            connectionConfiguration, objectName, attributeInfo
                                    .getName());
                    return MBeanUtils.getPropertySourceObject(attribute,
                            attributeInfo);

                    // return MBeanUtils.readAttributeValue(c, this.objectName,
                    // attributeInfo);
                } catch (Throwable e) {
                    // print only the message header
                    log.error("Unable to get attribute value" + idValue + " "
                            + e.getMessage());
                    return attributeNotAvailableDisplayName;
                }
            }
        }
        return super.getPropertyValue(id);
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
