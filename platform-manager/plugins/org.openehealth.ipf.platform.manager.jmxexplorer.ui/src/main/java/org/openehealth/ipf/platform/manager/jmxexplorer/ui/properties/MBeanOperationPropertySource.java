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
import java.util.List;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * The public final fields are used to get the property's value from this
 * propertySource which is returned as adapter of every attribute node.
 * <p>
 * <b>IMPORTANT!</b> Uses setFilterFlags to register keys for which this
 * PropertySource is a match in a regular expression. This is used for
 * filtering.
 * <p>
 * 
 * @see PropertyDescriptor#setFilterFlags(String[])
 * @see IPropertySource2
 * 
 * @author Mitko Kolev
 */
public class MBeanOperationPropertySource extends MBeanDescritorPropertySource {

    public final static String OPERATION_SIGNATURE_KEY = "operation.signature";

    public final static String RETRUN_TYPE_KEY = "operation.returnType";

    // for internal use
    private final static String DESCRIPTION_KEY = "operation.description";

    private final static String NAME_KEY = "operation.name";

    private final static String IMPACT_KEY = "operation.impact";

    private final static String PARAMETER_TYPE_KEY = "operation.parameter.type";

    private final static String PARAMETER_DESCRIPTION_KEY = "operation.parameter.description";

    private final static String impactDisplayName;

    private final static String descriptionDisplayName;

    private final static String nameDisplayName;

    private final static String returnTypeDisplayName;

    private final static String OPEARATION_CATEGORY_KEY = "operation.category";

    private final static String operationCategoryDisplayName;

    private final static String PARAMETERS_CATEGORY_KEY = "operation.parameters.category";

    private final static String parameterCategoryDisplayName;

    private final static String parameterDescriptionDisplayName;

    private final static String PARAMETER_NAME_KEY = "operation.parameter.name";

    private final static String parameterNameDisplayName;

    private final static String parameterTypeDisplayName;

    private final static String operationSignatureDisplayName;

    private final static String IMPACT_INFO_KEY = "operation.impact.info";

    private final static String operationImpactInfoDisplayName;

    private final static String IMPACT_ACTION_KEY = "operation.impact.action";

    private final static String operationImpactActionDisplayName;

    private final static String IMPACT_ACTION_INFO_KEY = "operation.impact.action.info";

    private final static String operationImpactActionInfoDisplayName;

    private final static String IMPACT_UNKNOWN_KEY = "operation.impact.unknown";;

    private final static String operationImpactUnknownDisplayName;

    static {
        descriptionDisplayName = Messages.getLabelString(DESCRIPTION_KEY);
        impactDisplayName = Messages.getLabelString(IMPACT_KEY);
        nameDisplayName = Messages.getLabelString(NAME_KEY);
        returnTypeDisplayName = Messages.getLabelString(RETRUN_TYPE_KEY);
        parameterTypeDisplayName = Messages.getLabelString(PARAMETER_TYPE_KEY);
        parameterNameDisplayName = Messages.getLabelString(PARAMETER_NAME_KEY);
        parameterDescriptionDisplayName = Messages
                .getLabelString(PARAMETER_DESCRIPTION_KEY);
        parameterCategoryDisplayName = Messages
                .getLabelString(PARAMETERS_CATEGORY_KEY);
        operationCategoryDisplayName = Messages
                .getLabelString(OPEARATION_CATEGORY_KEY);
        operationSignatureDisplayName = Messages
                .getLabelString(OPERATION_SIGNATURE_KEY);

        operationImpactInfoDisplayName = Messages
                .getLabelString(IMPACT_INFO_KEY);
        operationImpactActionDisplayName = Messages
                .getLabelString(IMPACT_ACTION_KEY);
        operationImpactActionInfoDisplayName = Messages
                .getLabelString(IMPACT_ACTION_INFO_KEY);
        operationImpactUnknownDisplayName = Messages
                .getLabelString(IMPACT_UNKNOWN_KEY);
    }

    private final MBeanOperationInfo operationInfo;

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
    public MBeanOperationPropertySource(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanOperationInfo operationInfo) {
        super(operationInfo.getDescriptor());
        this.operationInfo = operationInfo;
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
     * Returns the MBeanOperationInfo of the MBean for this operation
     * 
     * @return
     */
    public MBeanOperationInfo getOperationInfo() {
        return operationInfo;
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
        pd.setCategory(operationCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(RETRUN_TYPE_KEY, returnTypeDisplayName);
        pd.setCategory(operationCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(NAME_KEY, nameDisplayName);
        // register keys for which this PropertySource is a match
        pd.setFilterFlags(new String[] { this.objectName.toString(),
                MBeanUtils.buildOperationSignature(this.operationInfo),
                operationCategoryDisplayName, descriptionDisplayName });
        pd.setCategory(operationCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(IMPACT_KEY, impactDisplayName);
        pd.setCategory(operationCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        pd = new PropertyDescriptor(OPERATION_SIGNATURE_KEY,
                operationSignatureDisplayName);
        pd.setCategory(operationCategoryDisplayName);
        pd.setAlwaysIncompatible(true);
        descriptors.add(pd);

        // operation params
        descriptors.addAll(createOperationParametersPropertyDescriptors());
        // operation descriptor
        descriptors.addAll(Arrays.asList(super.getPropertyDescriptors()));
        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

    private List<IPropertyDescriptor> createOperationParametersPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        for (int t = 0; t < operationInfo.getSignature().length; t++) {
            PropertyDescriptor pd = new PropertyDescriptor(
                    PARAMETER_DESCRIPTION_KEY + "@@" + String.valueOf(t),
                    parameterDescriptionDisplayName);
            pd.setCategory(parameterCategoryDisplayName + " "
                    + String.valueOf(t + 1));
            pd.setAlwaysIncompatible(true);
            descriptors.add(pd);

            pd = new PropertyDescriptor(PARAMETER_NAME_KEY + "@@"
                    + String.valueOf(t), parameterNameDisplayName);
            pd.setCategory(parameterCategoryDisplayName + " "
                    + String.valueOf(t + 1));
            pd.setAlwaysIncompatible(true);
            descriptors.add(pd);

            pd = new PropertyDescriptor(PARAMETER_TYPE_KEY + "@@"
                    + String.valueOf(t), parameterTypeDisplayName);
            pd.setCategory(parameterCategoryDisplayName + " "
                    + String.valueOf(t + 1));
            pd.setAlwaysIncompatible(true);
            descriptors.add(pd);
        }
        return descriptors;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        if (id instanceof String) {
            String idValue = (String) id;
            if (idValue.equals(DESCRIPTION_KEY)) {
                return operationInfo.getDescription();
            } else if (idValue.equals(NAME_KEY)) {
                return operationInfo.getName();
            } else if (idValue.equals(RETRUN_TYPE_KEY)) {
                return operationInfo.getReturnType();
            } else if (idValue.equals(OPERATION_SIGNATURE_KEY)) {
                return MBeanUtils.buildOperationSignature(this.operationInfo);
            } else if (idValue.equals(IMPACT_KEY)) {
                // print the real impact level of the operation
                switch (operationInfo.getImpact()) {
                case MBeanOperationInfo.INFO:
                    return operationImpactInfoDisplayName;
                case MBeanOperationInfo.ACTION:
                    return operationImpactActionDisplayName;
                case MBeanOperationInfo.ACTION_INFO:
                    return operationImpactActionInfoDisplayName;
                case MBeanOperationInfo.UNKNOWN:
                    return operationImpactUnknownDisplayName;
                default:
                    return operationInfo.getImpact();
                }

            } else if (idValue.indexOf("@@") > 0) {
                int atAtIndex = idValue.indexOf("@@");
                String parameterIndexString = idValue.substring(atAtIndex + 2);
                int paramerterIndex = Integer.parseInt(parameterIndexString);
                MBeanParameterInfo[] parameterInfo = operationInfo
                        .getSignature();
                String paramerterName = idValue.substring(0, atAtIndex);
                if (paramerterName.equals(PARAMETER_NAME_KEY)) {
                    return parameterInfo[paramerterIndex].getName();
                } else if (paramerterName.equals(PARAMETER_DESCRIPTION_KEY)) {
                    return parameterInfo[paramerterIndex].getDescription();
                } else if (paramerterName.equals(PARAMETER_TYPE_KEY)) {
                    return parameterInfo[paramerterIndex].getType();
                }
            }
        }
        return super.getPropertyValue(id);
    }
}
