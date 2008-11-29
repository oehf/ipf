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
package org.openehealth.ipf.platform.manager.jmxexplorer;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * * Proxies the MBean server connection. Provides reusable code for some JMX
 * operations.
 * 
 * @see MBeanServerConnection
 * 
 * @author Mitko Kolev
 */
public interface IMBeanServerConnectionFacade {

    /**
     * Gets the MBeanInfo for the objectName
     * 
     * @param connectionConfiguration
     *            the connection context
     * @param objectName
     *            the Name for with the MBeanInfo will be returned
     * @return the returned MBeanInfo
     * @throws IOException
     *             thrown when a problem occurs
     */
    public MBeanInfo getMBeanInfo(ObjectName objectName)
            throws InstanceNotFoundException, IntrospectionException,
            IOException;

    public Set<ObjectName> getObjectNames() throws IOException;

    public Object readAttributeValue(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, String attributeName) throws IOException,
            AttributeNotFoundException, InstanceNotFoundException,
            MBeanException;

    /**
     * Writes the attribute value to the server. The listeners are notified with
     * the updated value with a JMXExplorerEvent.
     * 
     * @param connectionConfiguration
     * @param objectName
     * @param attributeInfo
     * @param attributeValue
     */
    public void writeAttributeValue(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanAttributeInfo attributeInfo,
            String attributeValue);

    /**
     * Invokes the operation in the specified connection context. The listeners
     * are notified with the updated value with a JMXExplorerEvent.
     * 
     * @param connectionConfiguration
     * @param objectName
     * @param operationInfo
     * @param parameterValues
     * @param parameterSignatures
     * @return
     */
    public void invokeOperation(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanOperationInfo operationInfo,
            List<String> parameterValues, List<String> parameterSignatures);
}
