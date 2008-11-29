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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.CompositeDataArrayPropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.CompositeDataPropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanCollectionAttributePropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.TabularDataPropertySource;

/**
 * Helper methods concerning MBeans
 * 
 * @author Mitko Kolev
 */
public class MBeanUtils {

    private final static Log log = LogFactory.getLog(MBeanUtils.class);

    /**
     * Converts the primitive types to their Object types
     * 
     * @param type
     * @param stringValue
     * @return
     */
    /*
     * Converts the attribute value to PropertySource if necessary
     * 
     * @param attributeValue
     * 
     * @param attributeInfo
     * 
     * @return
     */
    public static Object getPropertySourceObject(Object attributeValue,
            MBeanFeatureInfo featureInfo) {
        if (attributeValue == null)
            return "";
        if (attributeValue instanceof CompositeData) {
            return new CompositeDataPropertySource(
                    (CompositeData) attributeValue, featureInfo);
        } else if (attributeValue.getClass().isArray()) {
            if (attributeValue instanceof CompositeData[]) {
                // this is the more general case
                return new CompositeDataArrayPropertySource(attributeValue,
                        (CompositeData[]) attributeValue, featureInfo);
            }
            if (featureInfo instanceof MBeanAttributeInfo) {
                if (attributeValue instanceof Object[]) {
                    // if the array is not of primitive type
                    return new MBeanCollectionAttributePropertySource(Arrays
                            .asList((Object[]) attributeValue),
                            (MBeanAttributeInfo) featureInfo);
                } else {
                    return new MBeanCollectionAttributePropertySource(
                            Arrays
                                    .asList(convertPrimitiveArrayToObjectArray(attributeValue)),
                            (MBeanAttributeInfo) featureInfo);
                }
            }
        } else if (attributeValue instanceof TabularData) {
            return new TabularDataPropertySource((TabularData) attributeValue,
                    featureInfo);
        }
        return convertPropertySourceValueToString(attributeValue,
                new StringBuffer(""));
    }

    /**
     * Makes an object array from an array of primitives
     * 
     * @param object
     * @return
     */
    protected static Object[] convertPrimitiveArrayToObjectArray(Object object) {
        if (object.getClass().isArray()) {
            List<Object> objectValues = new ArrayList<Object>();
            if (object instanceof int[]) {
                int[] primitives = (int[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }

            } else if (object instanceof long[]) {
                long[] primitives = (long[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }

            } else if (object instanceof boolean[]) {
                boolean[] primitives = (boolean[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }

            } else if (object instanceof char[]) {
                char[] primitives = (char[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }

            } else if (object instanceof float[]) {
                float[] primitives = (float[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }
            } else if (object instanceof short[]) {
                short[] primitives = (short[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }
            } else if (object instanceof double[]) {
                double[] primitives = (double[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }
            } else if (object instanceof byte[]) {
                byte[] primitives = (byte[]) object;
                for (int t = 0; t < primitives.length; t++) {
                    objectValues.add(primitives[t]);
                }
            }
            return objectValues.toArray();
        }
        log.error("Must be never entered!");
        Object[] foo = new Object[] { new Object() };
        return foo;

    }

    public static String createObjectNameDescription(ObjectName objectName) {

        String domain = objectName.getDomain();
        String params = objectName.getKeyPropertyListString();
        return domain + ":" + params;
    }

    @SuppressWarnings("unchecked")
    public static boolean isSimpleWritableType(ObjectName objectName,
            MBeanAttributeInfo attributeInfo) {

        String type = attributeInfo.getType();
        if (type == null)
            return false;
        if (!attributeInfo.isWritable()) {
            return false;
        }

        // provide editor for the simple types
        if (type.toLowerCase().equals("boolean")) {
            return true;
        } else if (type.toLowerCase().equals("int")) {
            return true;
        } else if (type.toLowerCase().equals("byte")) {
            return true;
        } else if (type.toLowerCase().equals("short")) {
            return true;
        } else if (type.toLowerCase().equals("long")) {
            return true;
        } else if (type.toLowerCase().equals("double")) {
            return true;
        } else if (type.toLowerCase().equals("float")) {
            return true;
        } else if (type.toLowerCase().equals("char")) {
            return true;
        }
        // is simple type?
        try {
            Class c = Class.forName(type);
            if (c.isAssignableFrom(String.class)
                    || c.isAssignableFrom(CharSequence.class)) {
                return true;
            } else if (c.isAssignableFrom(Date.class)) {
                return true;
            } else if (c.isArray())
                return false;
            else if (c.isEnum())
                return false;
            else if (c.isAssignableFrom(SimpleType.class)) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            return false;
        }
        return false;
    }

    /**
     * Returns the name of the class without package
     * 
     * @param className
     * @return for java.lang.String returns String
     */
    public static String getClassNameWithoutPackage(String className) {
        if (className == null) {
            return null;
        }
        if (className.equals("[J")) {
            return "long []";
        } else if (className.equals("[Z")) {
            return "boolean []";
        } else if (className.equals("[B")) {
            return "byte []";
        } else if (className.equals("[C")) {
            return "char []";
        } else if (className.equals("[S")) {
            return "short []";
        } else if (className.equals("[I")) {
            return "int []";
        } else if (className.equals("[F")) {
            return "float []";
        } else if (className.equals("[D")) {
            return "double []";
        }
        boolean isParameterTypeClass = className.lastIndexOf(".") > 0;
        if (isParameterTypeClass) {
            return className.substring(className.lastIndexOf(".") + 1);
        } else {
            return className;
        }
    }

    /**
     * It makes sense to call this function with PropertySource instance value.
     * Returns the string representation of the attribute value. If the value is
     * array or if the value is array returns the array in the form [element 1
     * \n, element 2 \n ... element n \n]
     * 
     * @param propertySourceValue
     * @return the String value
     */
    public static String convertPropertySourceValueToString(
            Object propertySourceValue, StringBuffer appendOffset) {
        if (propertySourceValue == null)
            return "";
        if (propertySourceValue instanceof ObjectName) {
            ObjectName on = (ObjectName) propertySourceValue;
            StringBuffer s = new StringBuffer();
            s.append(on.getDomain() + ":")
                    .append(on.getKeyPropertyListString());
            return s.toString();
        } else if (propertySourceValue instanceof IPropertySource) {
            StringBuffer buffer = new StringBuffer();
            IPropertySource sourceValue = (IPropertySource) propertySourceValue;
            IPropertyDescriptor[] descriptors = sourceValue
                    .getPropertyDescriptors();

            buffer.append("").append("{");
            for (int t = 0; t < descriptors.length; t++) {
                Object valueIn = sourceValue.getPropertyValue(descriptors[t]
                        .getId());
                // properties are denoted with [
                buffer.append("\n").append(appendOffset).append("\t [");
                buffer.append(descriptors[t].getDisplayName()).append("=");
                // recursive call
                StringBuffer attributeAppend = new StringBuffer();
                attributeAppend.append(appendOffset).append("\t");
                buffer.append(convertPropertySourceValueToString(valueIn,
                        attributeAppend));
                buffer.append("]");
            }
            // end of this complex property
            buffer.append("\n").append(appendOffset).append("}");

            return buffer.toString();
        }
        return propertySourceValue.toString();
    }

    /**
     * Builds signature in the form returnType name( P pname...);
     * 
     * @return
     */
    public static String buildOperationSignature(
            MBeanOperationInfo operationInfo) {
        StringBuffer b = new StringBuffer();
        MBeanParameterInfo[] parameterInfo = operationInfo.getSignature();
        String returnType = operationInfo.getReturnType();
        boolean isReturnTypeClass = returnType.lastIndexOf(".") > 0;
        if (isReturnTypeClass) {
            b.append(returnType.substring(returnType.lastIndexOf(".") + 1));
        } else {
            b.append(returnType);
        }
        b.append(" ");
        b.append(operationInfo.getName());
        b.append("(");
        for (int t = 0; t < parameterInfo.length; t++) {
            String parameterType = parameterInfo[t].getType();
            b.append(MBeanUtils.getClassNameWithoutPackage(parameterType));
            b.append(" ").append(parameterInfo[t].getName());
            if (t < parameterInfo.length - 1) {
                b.append(", ");
            }
        }
        b.append(");");
        return b.toString();
    }
}