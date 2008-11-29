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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.jmxexplorer.ui.util.Messages;

/**
 * Preference page for the JMX Explorer View
 * 
 * @see IWorkbenchPreferencePages
 * @see PreferencePage
 * 
 * @author Mitko Kolev
 */
public class JMXExplorerPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage {

    private static final Log log = LogFactory
            .getLog(JMXExplorerPreferencePage.class);

    private Button groupMbeansByType;

    private Button groupMbeansByNaturalOrder;

    private Button groupMbeansOrderCanonically;

    public JMXExplorerPreferencePage() {
    }

    public JMXExplorerPreferencePage(String title) {
        super(title);
    }

    public JMXExplorerPreferencePage(String title, ImageDescriptor image) {
        super(title, image);
    }

    @Override
    protected Control createContents(Composite parent) {
        Preferences preferences = Activator.getDefault().getPluginPreferences();

        boolean groupMbeansByTypeSelected = preferences
                .getBoolean(PreferencesInitializer.GROUP_BY_TYPE_FIRST);
        boolean groupMbeansByCreationSelected = preferences
                .getBoolean(PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER);
        boolean groupMbeanscCanonicallySelected = preferences
                .getBoolean(PreferencesInitializer.GROUP_CANONICALLY);

        Composite options = new Composite(parent, SWT.NONE);
        options.setLayout(new GridLayout(1, false));

        Group jmxTreeGroup = new Group(options, SWT.NONE);
        jmxTreeGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true,
                false, 1, 1));
        jmxTreeGroup.setLayout(new GridLayout(3, false));
        jmxTreeGroup.setText(Messages
                .getLabelString("jmx.tree.nodes.group.text"));

        groupMbeansByType = new Button(jmxTreeGroup, SWT.RADIO);
        groupMbeansByType.setText(Messages
                .getLabelString("group.mbeans.by.type.text"));
        groupMbeansByType.setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false,
                false, 3, 1));
        groupMbeansByType.setToolTipText(Messages
                .getLabelString("group.mbeans.by.type.tooltip"));
        groupMbeansByType.setSelection(groupMbeansByTypeSelected);

        groupMbeansByNaturalOrder = new Button(jmxTreeGroup, SWT.RADIO);
        groupMbeansByNaturalOrder.setText(Messages
                .getLabelString("group.mbeans.by.natura.order.text"));
        groupMbeansByNaturalOrder.setLayoutData(new GridData(SWT.LEFT,
                SWT.NONE, false, false, 3, 1));
        groupMbeansByNaturalOrder.setToolTipText(Messages
                .getLabelString("group.mbeans.by.natura.order.tooltip"));
        groupMbeansByNaturalOrder.setSelection(groupMbeansByCreationSelected);

        groupMbeansOrderCanonically = new Button(jmxTreeGroup, SWT.RADIO);
        groupMbeansOrderCanonically.setText(Messages
                .getLabelString("group.mbeans.order.canonically.text"));
        groupMbeansOrderCanonically.setLayoutData(new GridData(SWT.LEFT,
                SWT.NONE, false, false, 3, 1));
        groupMbeansOrderCanonically.setToolTipText(Messages
                .getLabelString("group.mbeans.order.canonically.tooltip"));
        groupMbeansOrderCanonically
                .setSelection(groupMbeanscCanonicallySelected);

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
        this.groupMbeansByType
                .setSelection(PreferencesInitializer.GROUP_BY_TYPE_FIRST_DEFAULT_VALUE);
        this.groupMbeansByNaturalOrder
                .setSelection(PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER_DEFAULT_VALUE);
        this.groupMbeansOrderCanonically
                .setSelection(PreferencesInitializer.GROUP_CANONICALLY_DEFAULT_VALUE);

    }

    @Override
    public boolean performOk() {
        try {
            Preferences preferences = Activator.getDefault()
                    .getPluginPreferences();
            boolean selectionByType = groupMbeansByType.getSelection();
            boolean selectionByNaturalOrder = this.groupMbeansByNaturalOrder
                    .getSelection();
            boolean selectionCanonicallyOrder = this.groupMbeansOrderCanonically
                    .getSelection();

            if (selectionByType) {
                preferences.setValue(
                        PreferencesInitializer.GROUP_BY_TYPE_FIRST, true);
                preferences.setValue(PreferencesInitializer.GROUP_CANONICALLY,
                        false);
                preferences
                        .setValue(
                                PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER,
                                false);
            } else if (selectionByNaturalOrder) {
                preferences.setValue(
                        PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER, true);
                preferences.setValue(
                        PreferencesInitializer.GROUP_BY_TYPE_FIRST, false);
                preferences.setValue(PreferencesInitializer.GROUP_CANONICALLY,
                        false);

            } else if (selectionCanonicallyOrder) {
                preferences.setValue(PreferencesInitializer.GROUP_CANONICALLY,
                        true);
                preferences.setValue(
                        PreferencesInitializer.GROUP_BY_TYPE_FIRST, false);
                preferences
                        .setValue(
                                PreferencesInitializer.GROUP_BY_CREATIONAL_ORDER,
                                false);
            }

        } catch (Throwable t) {
            log.error("Throwable on ok for JMX Explorer preferences", t);
        }
        return true;
    }

}
