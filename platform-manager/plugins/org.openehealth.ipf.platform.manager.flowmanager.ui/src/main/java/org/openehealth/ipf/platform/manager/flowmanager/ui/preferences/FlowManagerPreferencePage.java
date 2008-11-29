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
package org.openehealth.ipf.platform.manager.flowmanager.ui.preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * Preferences page for the FlowManager plug-in.
 * 
 * @see org.eclipse.ui.IWorkbenchPreferencePage
 * 
 * @author Mitko Kolev
 */
public class FlowManagerPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

    Text timeoutText;

    private int lastValueOfTimeoutAfterSearch;

    private final Log log = LogFactory.getLog(FlowManagerPreferencePage.class);

    public FlowManagerPreferencePage() {
    }

    public FlowManagerPreferencePage(String title) {
        super(title);
    }

    public FlowManagerPreferencePage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent) {
        Preferences preferences = Activator.getDefault().getPluginPreferences();

        lastValueOfTimeoutAfterSearch = preferences
                .getInt(FlowManagerPreferencesConstants.REFRESH_DELAY_KEY);

        Composite options = new Composite(parent, SWT.NONE);

        options.setLayout(new GridLayout(3, false));

        CLabel label = new CLabel(options, SWT.NONE);
        label.setText(Messages.getLabelString("manage.options.refresh.delay"));
        label
                .setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,
                        1, 1));

        label.setToolTipText(Messages
                .getLabelString("manage.options.refresh.delay.tooltip"));
        timeoutText = new Text(options, SWT.SINGLE | SWT.BORDER);
        timeoutText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false,
                1, 1));
        timeoutText.setTextLimit(10);
        timeoutText.setText(String.valueOf(lastValueOfTimeoutAfterSearch));
        CLabel ms = new CLabel(options, SWT.NONE);
        ms.setText("ms");
        ms.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false, 1, 1));
        return options;
    }

    @Override
    public void init(IWorkbench workbench) {

    }

    @Override
    protected void performApply() {

        performOk();
    }

    @Override
    public boolean performCancel() {
        return true;
    }

    @Override
    protected void performDefaults() {
        super.performDefaults();
        timeoutText.setText(String
                .valueOf(FlowManagerPreferencesConstants.REFRESH_DELAY_DEFAULT_VALUE));
    }

    @Override
    public boolean performOk() {
        String valueText = timeoutText.getText();
        try {
            int value = Integer.parseInt(valueText);
            this.setValid(true);
            IFlowManagerApplicationController applicationController = Activator
                    .getFlowManagerApplicationController();
            applicationController.onSetTimeoutForReply(value);
            Preferences preferences = Activator.getDefault()
                    .getPluginPreferences();
            preferences.setValue(FlowManagerPreferencesConstants.REFRESH_DELAY_KEY, value);
        } catch (NumberFormatException e) {

            // do nothing
        } catch (Throwable t) {
            log.error("Throwable on ok for flow manager preferences", t);
        }
        return true;
    }

}
