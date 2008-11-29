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
package org.openehealth.ipf.platform.manager.connection.ui.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.IJMXConnectionManager;
import org.openehealth.ipf.platform.manager.connection.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.connection.ui.utils.messages.Messages;

/**
 * Basic properties of Connection.
 * 
 * @see IConnectionConfiguration
 * @see org.eclipse.ui.views.properties.IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class JMXConnectionPropertiesSource implements IPropertySource2 {

    private final IPropertyDescriptor[] connectionPropertyDescriptors;

    private final static String hostDisplayName;

    private final static String nameDisplayName;

    private final static String nameDescription;

    private final static String hostDescription;

    private final static String portDisplayName;

    private final static String portDescription;

    private final static String activeDisplayName;

    private final static String activeDescription;

    // not used
    // private final static String typeDisplayName;
    //
    // private final static String typeDescription;

    private final static String userNameDisplayName;

    private final static String userNameDescription;

    private static final String CONNECTION_HOST_ID = "connection.host";

    private static final String CONNECTION_PORT_ID = "connection.port";

    private static final String CONNECTION_ACTIVE_ID = "connection.active";

    // not used
    // private static final String CONNECTION_TYPE_ID = "connection.type";

    private static final String CONNECTION_USERNAME_ID = "connection.username";

    private static final String CONNECTION_NAME_ID = "connection.name";

    static {
        // initialize the properties just once
        hostDisplayName = Messages
                .getLabelString("ConnectionPropertiesSource.host");
        nameDisplayName = Messages
                .getLabelString("ConnectionPropertiesSource.connnection.name");
        nameDescription = Messages
                .getLabelString("ConnectionPropertiesSource.connnection.name.description");
        portDisplayName = Messages
                .getLabelString("ConnectionPropertiesSource.port");
        hostDescription = Messages
                .getLabelString("ConnectionPropertiesSource.host.description");
        portDescription = Messages
                .getLabelString("ConnectionPropertiesSource.port.description");
        activeDisplayName = Messages
                .getLabelString("ConnectionPropertiesSource.active");
        activeDescription = Messages
                .getLabelString("ConnectionPropertiesSource.active.description");
        // typeDisplayName = Messages
        // .getLabelString("ConnectionPropertiesSource.type");
        // typeDescription = Messages
        // .getLabelString("ConnectionPropertiesSource.type.description");
        userNameDisplayName = Messages
                .getLabelString("ConnectionPropertiesSource.username");
        userNameDescription = Messages
                .getLabelString("ConnectionPropertiesSource.username.description");
    }

    private final IJMXConnectionManager jMXConnectionManager;

    private final IConnectionConfiguration connectionConfiguration;

    public JMXConnectionPropertiesSource(IConnectionConfiguration connnection) {
        this.connectionConfiguration = connnection;
        connectionPropertyDescriptors = initializePropertyDescriptors();
        jMXConnectionManager = Activator.getDefault().getJMXConnectionManager();
    }

    /**
     * Returns the descriptors of the properties for the connection
     * 
     * @return
     */
    private IPropertyDescriptor[] initializePropertyDescriptors() {
        List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        PropertyDescriptor descriptor = new PropertyDescriptor(
                CONNECTION_HOST_ID, hostDisplayName);
        descriptor.setDescription(hostDescription);
        descriptor.setAlwaysIncompatible(true);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(CONNECTION_PORT_ID, portDisplayName);
        descriptor.setDescription(portDescription);
        descriptor.setAlwaysIncompatible(true);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(CONNECTION_NAME_ID, nameDisplayName);
        descriptor.setDescription(nameDescription);
        descriptor.setAlwaysIncompatible(true);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(CONNECTION_ACTIVE_ID,
                activeDisplayName);
        descriptor.setDescription(activeDescription);
        descriptor.setAlwaysIncompatible(true);
        descriptors.add(descriptor);
        // do not show it
        // descriptor = new PropertyDescriptor(CONNECTION_TYPE_ID,
        // typeDisplayName);
        // descriptor.setDescription(typeDescription);
        // descriptor.setAlwaysIncompatible(true);
        // descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(CONNECTION_USERNAME_ID,
                userNameDisplayName);
        descriptor.setDescription(userNameDescription);
        descriptor.setAlwaysIncompatible(true);
        descriptors.add(descriptor);

        IPropertyDescriptor[] descriptorsArray = new IPropertyDescriptor[descriptors
                .size()];
        return descriptors.toArray(descriptorsArray);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    @Override
    public Object getEditableValue() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return connectionPropertyDescriptors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
     * .lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id.equals(CONNECTION_HOST_ID)) {
            return connectionConfiguration.getHost();
        } else if (id.equals(CONNECTION_PORT_ID)) {
            return connectionConfiguration.getPort();
        } else if (id.equals(CONNECTION_ACTIVE_ID)) {
            return jMXConnectionManager.isConnected(connectionConfiguration);
        }
        // else if (id.equals(CONNECTION_TYPE_ID)) {
        // return "RMI";
        // }
        else if (id.equals(CONNECTION_USERNAME_ID)) {
            return connectionConfiguration.getAuthenticationCredentials()
                    .getUserName();
        } else
            return "";

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
     * .Object)
     */
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
     * .lang.Object)
     */
    @Override
    public void resetPropertyValue(Object id) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void setPropertyValue(Object id, Object value) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource2#isPropertyResettable
     * (java.lang.Object)
     */
    @Override
    public boolean isPropertyResettable(Object id) {
        return false;
    }
}
