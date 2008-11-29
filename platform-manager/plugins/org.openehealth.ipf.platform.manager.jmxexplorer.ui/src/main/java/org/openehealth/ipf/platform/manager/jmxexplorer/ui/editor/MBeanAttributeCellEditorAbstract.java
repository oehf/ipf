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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Provides cell editor with a button set;
 * 
 * @author Mitko Kolev
 */
public abstract class MBeanAttributeCellEditorAbstract extends CellEditor
        implements SelectionListener {

    private Composite composite;

    private Text text;

    private static String SET_BUTTON_KEY = "attribute.editor.set.button.text";

    private static String setButtonText;
    static {
        setButtonText = Messages.getLabelString(SET_BUTTON_KEY);
    }

    protected MBeanAttributeCellEditorAbstract(Composite parent) {
        super(parent);
    }

    /**
     * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createControl(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginBottom = 0;
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginLeft = 0;
        layout.verticalSpacing = 0;

        composite.setLayout(layout);

        text = new Text(composite, SWT.PUSH);
        text.setEditable(true);
        text.setEnabled(true);

        GridData textLayoutData = new GridData(SWT.FILL, SWT.NONE, true, false,
                1, 1);
        textLayoutData.heightHint = 15;
        textLayoutData.minimumWidth = 40;
        text.setLayoutData(textLayoutData);

        Button setButton = new Button(composite, SWT.PUSH);
        GridData bLayoutData = new GridData(SWT.NONE, SWT.NONE, false, false,
                1, 1);
        bLayoutData.heightHint = 15;
        setButton.setLayoutData(bLayoutData);
        setButton.addSelectionListener(this);
        setButton.setText(setButtonText);
        return composite;
    }

    /**
     * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
     */
    @Override
    protected Object doGetValue() {
        return text.getText();
    }

    /**
     * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
     */
    @Override
    protected void doSetFocus() {
    }

    /**
     * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
     */
    @Override
    protected void doSetValue(Object value) {
        text.setText(String.valueOf(value));

    }

}
