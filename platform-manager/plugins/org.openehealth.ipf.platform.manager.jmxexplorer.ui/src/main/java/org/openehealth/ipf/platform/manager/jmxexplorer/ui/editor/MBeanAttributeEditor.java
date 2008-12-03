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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertySource;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.console.OutputConsole;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.properties.MBeanAttributePropertySource;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanAttributeNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Implementation of {@link IMBeanNodeEditor} for MBean attributes.
 * 
 * @author Mitko Kolev
 */
public class MBeanAttributeEditor implements IMBeanNodeEditor,
        SelectionListener {

    private final static String setButtonText;

    private final Log log = LogFactory.getLog(MBeanAttributeEditor.class);

    static {
        setButtonText = Messages
                .getLabelString("attribute.editor.set.button.text");

    }

    private Text valueText;

    private Button set;

    private Composite composite;

    private Label nameLabel;

    private final MBeanAttributeNode attributeNode;

    private final IConnectionConfiguration connectionConfiguration;

    private final OutputConsole console;

    public MBeanAttributeEditor(OutputConsole console,
            MBeanAttributeNode attributeNode) {
        this.attributeNode = attributeNode;
        this.connectionConfiguration = (IConnectionConfiguration) attributeNode
                .getAdapter(IConnectionConfiguration.class);
        this.console = console;
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
    public void widgetSelected(SelectionEvent se) {
        writeAttributeValue();
    }

    private void writeAttributeValue() {
        if (valueText.getText() == null)
            return;
        String textValue = valueText.getText().trim();

        IJMXExplorerMediator jmxExplorerMediator = Activator.getDefault()
                .getJMXExplorerMediator();
        try {
            IMBeanServerConnectionFacade facade = jmxExplorerMediator
                    .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);
            facade.writeAttributeValue(connectionConfiguration, attributeNode
                    .getObjectName(), attributeNode.getAttributeInfo(),
                    textValue);
        } catch (IOException ioe) {
            // report the error
            console.printMessage(connectionConfiguration.getName() + " "
                    + ioe.getLocalizedMessage(), false);
            log.debug("Connection is not accessible", ioe);
        }

    }

    public void decorateEditorContent() {
        // we have clicken on the attribute and we explicitly want to display it
        valueText.selectAll();
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.MBeanNodeEditorAbstract#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(final Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));
        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        layoutData.minimumWidth = 300;
        composite.setLayoutData(layoutData);

        nameLabel = new Label(composite, SWT.NONE);
        composite.addMouseListener(new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                composite.setFocus();
            }

            @Override
            public void mouseUp(MouseEvent e) {

            }

        });

        GridData typeLabelLayoutData = new GridData(SWT.NONE, SWT.NONE, true,
                false, 1, 1);
        nameLabel.setLayoutData(typeLabelLayoutData);
        nameLabel.setToolTipText(attributeNode.getAttributeInfo()
                .getDescription());
        nameLabel.setText(attributeNode.getAttributeInfo().getName());

        /**
         * SWT.NO_FOCUS allows this control not to accept focus, when the editor
         * is shown. this allows the mouse to scroll.
         */
        valueText = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.NO_FOCUS);

        GridData valueTextGridData = new GridData(SWT.FILL, SWT.NONE, true,
                true, 1, 1);
        valueTextGridData.minimumWidth = 350;
        valueTextGridData.widthHint = 350;
        valueText.setLayoutData(valueTextGridData);

        valueText.setText(readAttributeValue());

        set = new Button(composite, SWT.NONE);
        set.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false, 1, 1));
        set.setText(setButtonText);

        if (!attributeNode.isWritable()) {
            set.setEnabled(false);
            valueText.setEditable(false);
        } else {
            set.addSelectionListener(this);
        }
        /**
         * Allow the scroll to work
         */

    }

    private String readAttributeValue() {
        // obtain the value from the properties and set it to the text
        IPropertySource source = (IPropertySource) this.attributeNode
                .getAdapter(IPropertySource.class);

        Object value = source
                .getPropertyValue(MBeanAttributePropertySource.VALUE_KEY);
        // convert the value of complex property types if necessary
        String stringValue = MBeanUtils.convertPropertySourceValueToString(
                value, new StringBuffer());
        return stringValue;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.MBeanNodeEditorAbstract#getTargetNode()
     */
    @Override
    public MBeanNode getTargetNode() {
        return attributeNode;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getControl()
     */
    @Override
    public Composite getControl() {
        return composite;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#decorateContentsAsSelected()
     */
    @Override
    public boolean decorateContentsAsSelected() {
        FontRegistry fontRegistry = new FontRegistry(Display.getCurrent());
        nameLabel.setFont(fontRegistry.getBold(fontRegistry.defaultFont()
                .toString()));
        return true;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#valueChanged()
     */
    @Override
    public void valueChanged(JMXExplorerEvent event) {
        Object value = event.getValue();
        switch (event.getType()) {
        case JMXExplorerEvent.CHANGE_ATTRIBUTE_VALUE_ERROR:
            if (event.getSource().equals(this.attributeNode.getAttributeInfo())) {
                if (value instanceof Throwable) {
                    Throwable t = (Throwable) value;
                    console.printOutputThrowable(t);
                }
            }
            break;
        case JMXExplorerEvent.ATTRIBUTE_VALUE_CHANGED:
            if (event.getSource().equals(this.attributeNode.getAttributeInfo())) {
                String attributeValue;
                if (value instanceof String) {
                    attributeValue = (String) value;
                } else {
                    attributeValue = readAttributeValue();
                }
                // print the result to the console
                console.printOutputMessage(this.attributeNode.getName() + " = "
                        + attributeValue, false);

                this.valueText.setText(attributeValue);
                log.debug("Editor for attribute "
                        + this.attributeNode.getName() + " updated");
            }
            break;
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        try {
            valueText.setEnabled(enabled);
            set.setEnabled(enabled);
        } catch (Throwable t) {
            log.error("Error setting enabled state", t);
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#dispose()
     */
    @Override
    public void dispose() {
    }
}
