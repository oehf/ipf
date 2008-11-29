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

import java.io.IOException;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.SimpleWritableMBeanAttributeCellEditor;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;

/**
 * This class is selection listener for the cell editor. Does not support
 * OpenMBeans.
 * 
 * @see MBeanAttributeInfo
 * @see SimpleWritableMBeanAttributeCellEditor
 * 
 * @author Mitko Kolev
 */
public class MBeanSimpleWritableAttributePropertyDescriptor extends
        PropertyDescriptor implements SelectionListener {

    private static final Log log = LogFactory
            .getLog(MBeanSimpleWritableAttributePropertyDescriptor.class);
    private SimpleWritableMBeanAttributeCellEditor editor;

    private final IConnectionConfiguration connectionConfiguration;

    private final ObjectName objectName;

    private final MBeanAttributeInfo attributeInfo;

    /**
     * @param id
     * @param displayName
     */
    public MBeanSimpleWritableAttributePropertyDescriptor(
            IConnectionConfiguration connectionConfiguration,
            ObjectName objectName, MBeanAttributeInfo attributeInfo, Object id,
            String displayName) {
        super(id, displayName);
        this.connectionConfiguration = connectionConfiguration;
        this.objectName = objectName;
        this.attributeInfo = attributeInfo;
        this.setAlwaysIncompatible(true);
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertyDescriptor#createPropertyEditor(Composite)
     */
    @Override
    public CellEditor createPropertyEditor(Composite parent) {
        editor = new SimpleWritableMBeanAttributeCellEditor(parent, this);
        if (getValidator() != null)
            editor.setValidator(getValidator());
        return editor;
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent see) {
        String stringValue = (String) editor.getValue();
        if (stringValue == null || stringValue.equals("")) {
            return;
        }
        IJMXExplorerMediator mediator = Activator.getDefault()
                .getJMXExplorerMediator();
        try {
            IMBeanServerConnectionFacade facade = mediator
                    .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);

            facade.writeAttributeValue(connectionConfiguration, objectName,
                    attributeInfo, stringValue);
        } catch (IOException ioe) {
            log.error("Cannot write the attribute value", ioe);
        }

    }
}
