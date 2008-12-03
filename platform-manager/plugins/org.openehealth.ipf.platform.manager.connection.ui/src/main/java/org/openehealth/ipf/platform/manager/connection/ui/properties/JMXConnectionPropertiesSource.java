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
import org.openehealth.ipf.platform.manager.connection.mock.JMXConnectionManagerImplMock;
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

    final static String userNameDisplayName;

    final static String userNameDescription;

    static final String CONNECTION_NAME_ID = "connection.name";

    static final String CONNECTION_NAME_DESCRIPTION_ID = "connection.name.description";

    static final String CONNECTION_HOST_ID = "connection.host";

    static final String CONNECTION_HOST_DESCRIPTION_ID = "connection.host.description";

    static final String CONNECTION_PORT_ID = "connection.port";

    static final String CONNECTION_PORT_DESCRIPTION_ID = "connection.port.description";

    static final String CONNECTION_OPEN_ID = "connection.open";

    static final String CONNECTION_OPEN_DESCRIPTION_ID = "connection.open.description";

    static final String CONNECTION_USERNAME_ID = "connection.username";

    static final String CONNECTION_USERNAME_DESCRIPTION_ID = "connection.username.description";

    static {
        // initialize the properties just once
        hostDisplayName = Messages.getLabelString(CONNECTION_HOST_ID);
        hostDescription = Messages
                .getLabelString(CONNECTION_HOST_DESCRIPTION_ID);

        nameDisplayName = Messages.getLabelString(CONNECTION_NAME_ID);
        nameDescription = Messages
                .getLabelString(CONNECTION_NAME_DESCRIPTION_ID);

        portDisplayName = Messages.getLabelString(CONNECTION_PORT_ID);
        portDescription = Messages
                .getLabelString(CONNECTION_PORT_DESCRIPTION_ID);

        activeDisplayName = Messages.getLabelString(CONNECTION_OPEN_ID);
        activeDescription = Messages
                .getLabelString(CONNECTION_OPEN_DESCRIPTION_ID);

        userNameDisplayName = Messages.getLabelString(CONNECTION_USERNAME_ID);
        userNameDescription = Messages
                .getLabelString(CONNECTION_USERNAME_DESCRIPTION_ID);

    }

    private final IJMXConnectionManager jMXConnectionManager;

    private final IConnectionConfiguration connection;

    public JMXConnectionPropertiesSource(IConnectionConfiguration connnection) {
        this.connection = connnection;
        connectionPropertyDescriptors = initializePropertyDescriptors();
        if (Activator.getDefault() != null) {
            // if we run in a OSGi environment, the activator getdefault will
            // not fail.
            jMXConnectionManager = Activator.getDefault()
                    .getJMXConnectionManager();
        } else {
            // used for the tests
            jMXConnectionManager = new JMXConnectionManagerImplMock();
        }

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

        descriptor = new PropertyDescriptor(CONNECTION_OPEN_ID,
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
            return connection.getHost();
        } else if (id.equals(CONNECTION_PORT_ID)) {
            return connection.getPort();
        } else if (id.equals(CONNECTION_OPEN_ID)) {
            return jMXConnectionManager.isConnected(connection);
        } else if (id.equals(CONNECTION_USERNAME_ID)) {
            return connection.getAuthenticationCredentials().getUserName();
        } else if (id.equals(CONNECTION_NAME_ID)) {
            return connection.getName();
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
