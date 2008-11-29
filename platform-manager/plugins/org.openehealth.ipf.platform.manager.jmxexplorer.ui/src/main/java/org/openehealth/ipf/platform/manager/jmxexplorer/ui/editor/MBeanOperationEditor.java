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
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.jmxexplorer.IJMXExplorerMediator;
import org.openehealth.ipf.platform.manager.jmxexplorer.IMBeanServerConnectionFacade;
import org.openehealth.ipf.platform.manager.jmxexplorer.JMXExplorerEvent;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.console.OutputConsole;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.tree.MBeanOperationNode;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.MBeanUtils;

/**
 * Implementation of {@link IMBeanNodeEditor} for MBean operation.
 * 
 * @author Mitko Kolev
 */
public class MBeanOperationEditor implements IMBeanNodeEditor,
        SelectionListener {

    private final MBeanOperationNode operationNode;

    private Composite compositeEditor;

    private Button operationButton;

    private final List<Text> parameterControls;

    private final OutputConsole console;

    private final IConnectionConfiguration connectionConfiguration;

    public MBeanOperationEditor(OutputConsole console,
            MBeanOperationNode operationNode) {
        this.operationNode = operationNode;
        this.parameterControls = new ArrayList<Text>();
        connectionConfiguration = (IConnectionConfiguration) operationNode
                .getAdapter(IConnectionConfiguration.class);
        this.console = console;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        compositeEditor = new Composite(parent, SWT.NONE);
        compositeEditor.setLayout(new GridLayout(3, false));
        compositeEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));
        //compositeEditor.setText(operationNode.getOperationInfo().getName());
        compositeEditor.setToolTipText(this.operationNode.getOperationInfo()
                .getDescription());

        Label returnTypeLabel = new Label(compositeEditor, SWT.NONE);
        returnTypeLabel.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false,
                false, 1, 1));
        String returnType = this.operationNode.getOperationInfo()
                .getReturnType();
        returnTypeLabel.setText(MBeanUtils
                .getClassNameWithoutPackage(returnType));
        returnTypeLabel.setToolTipText(returnType);

        operationButton = new Button(compositeEditor, SWT.PUSH);
        operationButton.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false,
                false, 1, 1));
        operationButton.setText(operationNode.getOperationInfo().getName());
        operationButton.setToolTipText(operationNode.getOperationInfo()
                .getDescription());
        operationButton.addSelectionListener(this);

        Composite parametersComposite = new Composite(compositeEditor, SWT.NONE);

        MBeanParameterInfo[] parameters = this.operationNode.getOperationInfo()
                .getSignature();
        GridLayout layout = new GridLayout(parameters.length + 2, false);

        parametersComposite.setLayout(layout);
        parametersComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                true, false, 1, 1));
        Label begin = new Label(parametersComposite, SWT.NONE);
        begin.setText("(");
        begin.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
        for (int t = 0; t < parameters.length; t++) {

            MBeanParameterInfo parameter = parameters[t];
            createParameterControl(parametersComposite, parameter,
                    (t > 0 && t < parameters.length - 1) ? ", " : "");
        }
        Label end = new Label(parametersComposite, SWT.NONE);
        end.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
        end.setText(");");
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getTargetNode()
     */
    @Override
    public MBeanNode getTargetNode() {
        return operationNode;
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#getControl()
     */
    @Override
    public Composite getControl() {
        return compositeEditor;
    }

    private void createParameterControl(Composite parent,
            MBeanParameterInfo info, String labelText) {
        Composite parameterComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        parameterComposite.setLayout(layout);

        layout.marginBottom = 0;
        layout.marginTop = 0;
        layout.horizontalSpacing = 10;
        layout.marginLeft = 10;
        layout.marginRight = 10;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        GridData layoutData = new GridData(SWT.NONE, SWT.NONE, false, false);
        parameterComposite.setLayoutData(layoutData);

        Label label = new Label(parameterComposite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
        label.setText(labelText
                + MBeanUtils.getClassNameWithoutPackage(info.getType()));
        label.setToolTipText(info.getType());
        label.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));

        Text textValue = new Text(parameterComposite, SWT.BORDER);
        textValue.setText(info.getName() == null ? "" : info.getName());
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.minimumWidth = 80;
        data.widthHint = 80;
        textValue.setLayoutData(data);
        textValue.setToolTipText(info.getDescription());
        textValue.selectAll();

        this.parameterControls.add(textValue);

    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#decorateContentsAsSelected()
     */
    @Override
    public boolean decorateContentsAsSelected() {
        FontRegistry fontRegistry = new FontRegistry(Display.getCurrent(), true);
        operationButton.setFont(fontRegistry.getBold(fontRegistry.defaultFont()
                .toString()));
        return true;
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

        MBeanOperationInfo operationInfo = this.operationNode
                .getOperationInfo();
        MBeanParameterInfo[] paramInfos = operationInfo.getSignature();
        List<String> parameterSignatures = new ArrayList<String>();
        // set parameter infos
        for (int i = 0; i < paramInfos.length; i++) {
            parameterSignatures.add(paramInfos[i].getType());
        }
        // set parameter values with info from the texts
        List<String> parameterValues = new ArrayList<String>();
        for (int t = 0; t < parameterControls.size(); t++) {
            Text textControl = parameterControls.get(t);
            String textValue = textControl.getText();
            parameterValues.add(textValue);
        }

        // construct the message
        StringBuffer buffer = new StringBuffer();

        buffer.append(operationNode.getOperationInfo().getReturnType());
        buffer.append(" ");
        buffer.append(operationNode.getOperationInfo().getName());
        buffer.append("( ");
        for (int t = 0; t < parameterValues.size(); t++) {
            buffer.append(parameterValues.get(t));
            if (t < parameterValues.size() - 1) {
                buffer.append(", ");
            }
        }
        buffer.append(");");
        // print in bold
        console.printMessage(buffer.toString(), true);

        // invoke the method
        try {
            IJMXExplorerMediator mediator = Activator.getDefault()
                    .getJMXExplorerMediator();

            IMBeanServerConnectionFacade facade = mediator
                    .getMBeanServerConnectionConfigurationFacade(connectionConfiguration);

            facade.invokeOperation(connectionConfiguration, operationNode
                    .getObjectName(), operationInfo, parameterValues,
                    parameterSignatures);
        } catch (IOException ioe) {
            // print the error in the console
            console.printMessage(connectionConfiguration.getName() + " "
                    + ioe.getLocalizedMessage(), false);
        }
    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#valueChanged()
     */
    @Override
    public void valueChanged(JMXExplorerEvent event) {
        Object value = event.getValue();
        switch (event.getType()) {
        case JMXExplorerEvent.INVOKE_OPERATION_ERROR:
            if (event.getSource().equals(this.operationNode.getOperationInfo())) {
                if (value instanceof Throwable) {
                    Throwable t = (Throwable) value;
                    console.printThrowable(t);
                }
            }
            break;
        case JMXExplorerEvent.INVOKE_OPERATION_RESULT:
            if (event.getSource().equals(this.operationNode.getOperationInfo())) {
                Object result = event.getValue();
                if (result != null) {
                    // /String attributeValue =
                    // MBeanUtils.convertPropertySourceValueToString(result);
                    Object propertySource = MBeanUtils.getPropertySourceObject(
                            result, this.operationNode.getOperationInfo());
                    String stringValue = MBeanUtils
                            .convertPropertySourceValueToString(propertySource,
                                    new StringBuffer());
                    console.printMessage(stringValue, false);
                } else {
                    console.printMessage("", false);
                }
            }
            break;
        }

    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean editable) {
        operationButton.setEnabled(editable);
        for (Text parameterControl : parameterControls) {
            parameterControl.setEnabled(editable);
        }

    }

    /**
     * @see org.openehealth.ipf.platform.manager.jmxexplorer.ui.editor.IMBeanNodeEditor#dispose()
     */
    @Override
    public void dispose() {
    }
}