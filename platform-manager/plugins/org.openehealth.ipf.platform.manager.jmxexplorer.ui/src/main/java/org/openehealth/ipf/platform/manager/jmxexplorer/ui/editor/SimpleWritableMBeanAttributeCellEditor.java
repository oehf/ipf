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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanSimpleWritableAttributePropertyDescriptor;

/**
 * Editor for editting the attribute value only in the PropertiesView.
 * 
 * @author Mitko Kolev
 */
public class SimpleWritableMBeanAttributeCellEditor extends
        MBeanAttributeCellEditorAbstract {

    /**
     * @param parent
     */
    private final MBeanSimpleWritableAttributePropertyDescriptor selectionListener;

    public SimpleWritableMBeanAttributeCellEditor(Composite parent,
            MBeanSimpleWritableAttributePropertyDescriptor selectionListener) {
        super(parent);
        this.selectionListener = selectionListener;
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        selectionListener.widgetDefaultSelected(e);
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent e) {
        selectionListener.widgetSelected(e);
    }

}
