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
package org.openehealth.ipf.platform.manager.flowmanager.ui.editor;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerRepository;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.ChangeFlowManagerEnableCleanupJob;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.ChangeFlowManagerFlowFilteringJob;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.SetFlowManagerApplicationJob;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.SetFlowManagerMaxFlowsJob;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * Mediates the visualization of flow manager options. Registers itself as an
 * observer.
 * 
 * @author Mitko Kolev
 */
public class FlowManagerOptionsMediator implements Observer {

    private Button enableFiltering;

    private Button changeCleanup;

    private Text maxFlowsText;

    private Button applyMaxFlows;

    private Text applicationText;

    private Button applyApplication;

    private final IEditorInput editorInput;

    private boolean partCreated = false;

    public FlowManagerOptionsMediator(Composite parent, IEditorInput input) {
        this.editorInput = input;
        Activator.getFlowManagerApplicationController().addObserver(this);
        createPartControl(parent);
    }

    public void createPartControl(Composite parent) {

        createApplicationText(parent);

        Group application = new Group(parent, SWT.NONE);
        application.setText(Messages
                .getLabelString("manage.options.applciation.management"));
        application.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false,
                5, 1));
        application.setLayout(new GridLayout(5, false));
        createApplicationOptions(application);

        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) editorInput
                .getAdapter(IConnectionConfiguration.class);

        // set the enable / disable state of the components
        IFlowManagerRepository flowManagerRepository = Activator
                .getFlowManagerApplicationController()
                .getFlowManagerRepository();

        partCreated = true;
        if (!flowManagerRepository
                .isFlowManagerRegistered(connectionConfiguration)) {
            setDisabledState();
            return;
        }
        setEnabledState();
        setRepositoryValuesToUI(flowManagerRepository, connectionConfiguration);

    }

    private void createApplicationOptions(Composite parent) {

        Composite options = new Composite(parent, SWT.NONE);
        options.setLayout(new GridLayout(5, false));

        enableFiltering = new Button(options, SWT.CHECK);
        enableFiltering.setToolTipText(Messages
                .getLabelString("manage.options.check.filter.tooltip"));
        enableFiltering.setText(Messages
                .getLabelString("manage.options.check.filter")); //$NON-NLS-1$
        enableFiltering.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true,
                false, 5, 1));
        enableFiltering.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent se) {
                IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                        .getAdapter(IConnectionConfiguration.class);
                new ChangeFlowManagerFlowFilteringJob(Display.getCurrent(),
                        thisConnectionConfiguration).schedule();
            }
        });
        changeCleanup = new Button(options, SWT.CHECK);
        changeCleanup.setLayoutData(new GridData(SWT.NONE, SWT.NONE, true,
                false, 5, 1));
        changeCleanup.setText(Messages
                .getLabelString("manage.options.check.cleanup")); //$NON-NLS-1$
        changeCleanup.setToolTipText(Messages
                .getLabelString("manage.options.check.cleanup.tooltip"));
        changeCleanup.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent se) {
                IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                        .getAdapter(IConnectionConfiguration.class);
                // call the job
                new ChangeFlowManagerEnableCleanupJob(Display.getCurrent(),
                        thisConnectionConfiguration).schedule();

            }
        });
        CLabel label = new CLabel(options, SWT.NONE);
        label.setText(Messages.getLabelString("manage.options.label.maxflows"));
        label
                .setLayoutData(new GridData(SWT.LEFT, SWT.NONE, false, false,
                        1, 1));
        label.setToolTipText(Messages
                .getLabelString("manage.options.maxflows.tooltip"));
        maxFlowsText = new Text(options, SWT.SINGLE | SWT.BORDER);
        GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
        gd.minimumWidth = 150;
        maxFlowsText.setLayoutData(gd);
        maxFlowsText.setTextLimit(10);
        applyMaxFlows = new Button(options, SWT.NONE);

        applyMaxFlows.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false,
                false, 2, 1));
        applyMaxFlows.setText(Messages
                .getLabelString("manage.options.button.apply"));

        applyMaxFlows.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
            }

            @Override
            public void widgetSelected(SelectionEvent event) {
                IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                        .getAdapter(IConnectionConfiguration.class);

                try {
                    String maxFlows = maxFlowsText.getText().trim();
                    // check validity
                    Integer iflows = new Integer(maxFlows);
                    if (iflows <= 0)
                        throw new NumberFormatException();

                    // the editor input object is used to hold the data
                    new SetFlowManagerMaxFlowsJob(Display.getCurrent(),
                            thisConnectionConfiguration, maxFlows).schedule();

                } catch (NumberFormatException nfe) {
                    // restore the value from the repository
                    IFlowManagerRepository flowManagerRepository = Activator
                            .getFlowManagerApplicationController()
                            .getFlowManagerRepository();

                    setRepositoryValuesToUI(flowManagerRepository,
                            thisConnectionConfiguration);
                }
            }

        });
    }

    private void createApplicationText(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(4, false));
        composite.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false,
                5, 1));

        CLabel label = new CLabel(composite, SWT.NONE);
        label.setText(Messages
                .getLabelString("manage.options.label.application"));
        label
                .setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false,
                        1, 1));
        label.setToolTipText(Messages
                .getLabelString("manage.options.label.application.tooltip"));
        applicationText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
        gd.minimumWidth = 150;
        applicationText.setLayoutData(gd);
        applicationText.setTextLimit(20);

        applyApplication = new Button(composite, SWT.NONE);

        applyApplication.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false,
                false, 1, 1));
        applyApplication.setText(Messages
                .getLabelString("manage.options.button.apply"));

        applyApplication.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
            }

            @Override
            public void widgetSelected(SelectionEvent event) {
                String application = applicationText.getText();
                IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                        .getAdapter(IConnectionConfiguration.class);

                if (application != null && !application.trim().equals("")) {
                    new SetFlowManagerApplicationJob(Display.getCurrent(),
                            thisConnectionConfiguration, application)
                            .schedule();
                } else {
                    // restore the value from the repository
                    IFlowManagerRepository flowManagerRepository = Activator
                            .getFlowManagerApplicationController()
                            .getFlowManagerRepository();
                    setRepositoryValuesToUI(flowManagerRepository,
                            thisConnectionConfiguration);
                }
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof FlowManagerEvent) {
            FlowManagerEvent event = (FlowManagerEvent) arg;
            IConnectionConfiguration eventConnectionConfigurationContext = event
                    .getConnectionConfigurationContext();
            if (eventConnectionConfigurationContext == null)
                return;

            IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                    .getAdapter(IConnectionConfiguration.class);

            if (eventConnectionConfigurationContext
                    .equals(thisConnectionConfiguration)) {
                UpdateGuiStateRunnable updateStateRunnable = new UpdateGuiStateRunnable(
                        eventConnectionConfigurationContext, event);
                JobUtils.runSafe(updateStateRunnable);
            }
        }
    }

    final class UpdateGuiStateRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        private final FlowManagerEvent event;

        public UpdateGuiStateRunnable(
                IConnectionConfiguration connectionConfiguration,
                FlowManagerEvent event) {
            this.connectionConfiguration = connectionConfiguration;
            this.event = event;
        }

        @Override
        public void run() {
            IFlowManagerRepository flowManagerRepository = Activator
                    .getFlowManagerApplicationController()
                    .getFlowManagerRepository();

            switch (event.getType()) {
            case FlowManagerEvent.FLOWMANAGER_UNREGISTERED:
                setDisabledState();
                break;
            case FlowManagerEvent.FLOWMANAGER_REGISTERED:
                setEnabledState();
                setRepositoryValuesToUI(flowManagerRepository,
                        connectionConfiguration);
            case FlowManagerEvent.FLOWMANAGER_VIEW_CHANGE:
            case FlowManagerEvent.FLOWS_RECEIVED:
                break;
            case FlowManagerEvent.FLOWMANAGER_APPLICATION_CHANGED:
                // set the properties without the application.
                setRepositoryValuesToUI(flowManagerRepository,
                        connectionConfiguration);
                break;
            case FlowManagerEvent.FLOWMANAGER_ATTRIBUTE_CHANGED:
                setRepositoryValuesToUI(flowManagerRepository,
                        connectionConfiguration);
                break;
            }

        }
    }

    private void setRepositoryValuesToUI(
            IFlowManagerRepository flowManagerRepository,
            IConnectionConfiguration connectionConfiguration) {
        if (!partCreated)
            return;
        enableFiltering.setSelection(flowManagerRepository
                .isEnabledFiltering(connectionConfiguration));
        changeCleanup.setSelection(flowManagerRepository
                .isEnabledCleanup(connectionConfiguration));
        maxFlowsText.setText(flowManagerRepository
                .getMaxFlows(connectionConfiguration) == null ? ""
                : flowManagerRepository.getMaxFlows(connectionConfiguration));
        String application = flowManagerRepository
                .getApplication(connectionConfiguration);
        applicationText.setText(application == null ? "" : application);

    }

    private void setEnabledState() {
        if (!partCreated)
            return;

        applicationText.setEnabled(true);
        maxFlowsText.setEnabled(false);
        enableFiltering.setEnabled(true);
        changeCleanup.setEnabled(true);
        maxFlowsText.setEnabled(true);
        applyMaxFlows.setEnabled(true);
        applicationText.setEnabled(true);
        applyApplication.setEnabled(true);
    }

    private void setDisabledState() {
        if (!partCreated)
            return;
        applicationText.setEnabled(false);
        maxFlowsText.setEnabled(false);
        enableFiltering.setEnabled(false);
        changeCleanup.setEnabled(false);
        maxFlowsText.setEnabled(false);
        applyMaxFlows.setEnabled(false);
        applicationText.setEnabled(false);
        applyApplication.setEnabled(false);
    }

    public void dispose() {
        Activator.getFlowManagerApplicationController().deleteObserver(this);
        partCreated = false;

    }
}
