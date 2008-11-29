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
package org.openehealth.ipf.platform.manager.jmxexplorer.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.SimpleType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;

/**
 * Implementation of {@link IMBeanServerConnectionFacade}.
 * 
 * @see IMBeanServerConnectionFacade
 * 
 * @author Mitko Kolev
 */
public class MBeanServerConnectionFacadeImpl implements
        IMBeanServerConnectionFacade {

    private final Log log = LogFactory
            .getLog(MBeanServerConnectionFacadeImpl.class);

    private final JMXExplorerMediatorImpl jmxExplorerMediator;
    private final MBeanServerConnection mBeanServerConnection;

    public MBeanServerConnectionFacadeImpl(
            MBeanServerConnection mBeanServerConnection,
            JMXExplorerMediatorImpl jmxExplorerMediator) {
        this.mBeanServerConnection = mBeanServerConnection;
        this.jmxExplorerMediator = jmxExplorerMediator;
    }

    @Override
    public Set<ObjectName> getObjectNames() throws IOException {
        Set<ObjectName> mBeanNames = mBeanServerConnection.queryNames(null,
                null);
        return mBeanNames;
    }

    @Override
    public MBeanInfo getMBeanInfo(ObjectName objectName)
            throws InstanceNotFoundException, IntrospectionException,
            IOException {
        try {

            MBeanInfo mbeanInfo = mBeanServerConnection
                    .getMBeanInfo(objectName);
            return mbeanInfo;
        } catch (ReflectionException e) {
            log.error("Cannot reflect the MBeanInfo:", e);
            throw new IOException(e);
        }

    }

    @Override
    public Object readAttributeValue(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, String attributeName) throws IOException,
            AttributeNotFoundException, InstanceNotFoundException,
            MBeanException {
        try {
            return mBeanServerConnection
                    .getAttribute(objectName, attributeName);
        } catch (ReflectionException re) {
            log.error("Cannot reflect the Attribute value:", re);
            throw new IOException(re);
        }
    }

    /**
     * Writes this attribute value to the MBEan server.
     * 
     * @param connectionConfiguration
     *            the connection context.
     * @param objectName
     *            the ObjectName of the specified MBean
     * @param attributeInfo
     *            the AtributeInfo object.
     * @param attributeValue
     *            the new Value
     */
    public void writeAttributeValue(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanAttributeInfo attributeInfo,
            String attributeValue) {
        // convert
        Object value = convertStringValueToObjectValue(attributeInfo.getType(),
                attributeValue);
        Attribute attribute = new Attribute(attributeInfo.getName(), value);
        JMXExplorerEvent event;
        try {
            mBeanServerConnection.setAttribute(objectName, attribute);

            event = new JMXExplorerEvent(connectionConfiguration,
                    JMXExplorerEvent.ATTRIBUTE_VALUE_CHANGED, objectName,
                    attribute, attributeInfo);
        } catch (Exception t) {
            // we fould exception, no mather what, the attribute value was not
            // set
            log.error("writeAttributeValue failed", t);
            event = new JMXExplorerEvent(connectionConfiguration,
                    JMXExplorerEvent.CHANGE_ATTRIBUTE_VALUE_ERROR, objectName,
                    t, attributeInfo);
            return;
        }
        jmxExplorerMediator.notifyObserversWithJMXEvent(event);
    }

    /**
     * Invokes the specified operationInfo in the given connection. The
     * parameters follows the MbeanServerConnection.invoke conventions;
     * 
     * @param connectionConfiguration
     *            the connection context
     * @param objectName
     *            the ObjectName of the MBean
     * @param operationInfo
     *            The OperationInfo for this operation
     * @param parameterValues
     *            The parameterValues for the operation.
     * @param parameterSignatures
     *            The parameter signatures of the operation.
     */
    public void invokeOperation(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanOperationInfo operationInfo,
            List<String> parameterValues, List<String> parameterSignatures) {

        JMXExplorerEvent event;
        try {
            List<Object> parameterObjectValues = new ArrayList<Object>();
            for (int t = 0; t < parameterValues.size(); t++) {
                String textValue = parameterValues.get(t);
                Object value = convertStringValueToObjectValue(
                        parameterSignatures.get(t), textValue);
                parameterObjectValues.add(value);
            }

            String[] parameterSignaturesString = new String[parameterSignatures
                    .size()];
            Object result = mBeanServerConnection.invoke(objectName,
                    operationInfo.getName(), parameterObjectValues.toArray(),
                    parameterSignatures.toArray(parameterSignaturesString));

            event = new JMXExplorerEvent(connectionConfiguration,
                    JMXExplorerEvent.INVOKE_OPERATION_RESULT, objectName,
                    result, operationInfo);
        } catch (Throwable t) {
            log.error("invokeOperation failed", t);
            event = new JMXExplorerEvent(connectionConfiguration,
                    JMXExplorerEvent.INVOKE_OPERATION_ERROR, objectName, t,
                    operationInfo);
        }
        jmxExplorerMediator.notifyObserversWithJMXEvent(event);
    }

    @SuppressWarnings("unchecked")
    private Object convertStringValueToObjectValue(String type,
            String stringValue) {
        Object objectValue = stringValue;
        if (stringValue != null) {
            try {
                Class c = Class.forName(type);
                if (c.isAssignableFrom(String.class)
                        || c.isAssignableFrom(CharSequence.class)) {
                    return stringValue;
                } else if (c.isAssignableFrom(Date.class)) {
                    try {
                        return DateFormat.getDateInstance().parse(stringValue);
                    } catch (ParseException pe) {
                        // return the default value
                        return objectValue;
                    }
                } else if (c.isAssignableFrom(SimpleType.class)) {
                    return objectValue;

                }

            } catch (ClassNotFoundException e1) {
                // do nothing, allow execution of code below
            }
            if (!type.contains(".")) {
                // refine for primitive types
                try {
                    if (type.equals("boolean")) {
                        objectValue = new Boolean(stringValue);
                    } else if (type.toLowerCase().equals("int")) {
                        objectValue = Integer.parseInt(stringValue);
                    } else if (type.toLowerCase().equals("byte")) {
                        objectValue = Byte.parseByte(stringValue);
                    } else if (type.toLowerCase().equals("short")) {
                        objectValue = Short.parseShort(stringValue);
                    } else if (type.toLowerCase().equals("long")) {
                        objectValue = Long.parseLong(stringValue);
                    } else if (type.toLowerCase().equals("double")) {
                        objectValue = Double.parseDouble(stringValue);
                    } else if (type.toLowerCase().equals("float")) {
                        objectValue = Float.parseFloat(stringValue);
                    } else if (type.toLowerCase().equals("char")) {
                        objectValue = new Character(stringValue.charAt(0));
                    }
                } catch (Exception e) {
                    // do not invoke a call
                }
            } else {
                if (type.contains(".")) {
                    // refine for primitive type wrappers
                    try {
                        if (type.equals(Boolean.class.getName())) {
                            objectValue = new Boolean(stringValue);
                        } else if (type.equals(Integer.class.getName())) {
                            objectValue = Integer.parseInt(stringValue);
                        } else if (type.equals(Byte.class.getName())) {
                            objectValue = Byte.parseByte(stringValue);
                        } else if (type.equals(Short.class.getName())) {
                            objectValue = Short.parseShort(stringValue);
                        } else if (type.equals(Long.class.getName())) {
                            objectValue = Long.parseLong(stringValue);
                        } else if (type.equals(Double.class.getName())) {
                            objectValue = Double.parseDouble(stringValue);
                        } else if (type.equals(Float.class.getName())) {
                            objectValue = Float.parseFloat(stringValue);
                        } else if (type.equals(Character.class.getName())) {
                            objectValue = new Character(stringValue.charAt(0));
                        }
                    } catch (Exception e) {
                        // do not invoke a call
                    }
                }
            }
        }
        return objectValue;
    }

}
