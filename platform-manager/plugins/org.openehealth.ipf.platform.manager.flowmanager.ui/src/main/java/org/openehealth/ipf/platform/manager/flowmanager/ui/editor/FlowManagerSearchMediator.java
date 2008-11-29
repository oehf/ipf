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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.handlers.IHandlerService;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerRepository;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerSearchCriteria;
import org.openehealth.ipf.platform.manager.flowmanager.impl.FlowManagerSearchCriteriaImpl;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * Mediates the flow search functionality, which is part of the editor.
 * Registers itself as an observer.
 * 
 * @author Mitko Kolev
 */
public class FlowManagerSearchMediator implements Observer {

    private Button checkFrom;

    private Button checkTo;

    private Button uak;

    private Button nak;

    private Button doNotRestrict;

    private DateTime fromDate;

    private DateTime fromTime;

    private DateTime toDate;

    private DateTime toTime;

    private Button searchButton;

    private ImageDescriptor searchImageDescriptor;

    private Image searchImage;

    private final IEditorInput editorInput;

    private static final String UAK_SELECTED_KEY = "uak.selected";

    private static final String NAK_SELECTED_KEY = "nak.selected";

    private static final String DO_NOT_RESTRICT_SEARCH_KEY = "donotrestrict.selected";

    private static final String FROM_AFTER_TO_KEY = "fromdate.after.todate";

    private static final String FROM_AFTER_CURRENTDATE_KEY = "fromdate.after.currentdate";

    private static final String ERROR_BUILDING_SEARCH_CRITERIA_KEY = "error.search.criteria.title";

    private static final String EDITOR_SEARCH_OPTIONS_KEY = "search";

    private boolean nakSelectionState = false;

    private boolean uakSelectionState = false;

    private boolean doNotRestrictState = true;

    private final IFlowManagerRepository contorller;

    public FlowManagerSearchMediator(Composite parent, IEditorInput input) {
        IFlowManagerApplicationController flowManagerApplicationController = Activator
                .getFlowManagerApplicationController();
        contorller = flowManagerApplicationController
                .getFlowManagerRepository();
        this.editorInput = input;
        createQueryPart(parent);
        flowManagerApplicationController.addObserver(this);
        readAndSetState();
    }

    private void createQueryPart(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(5, false));
        group
                .setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 5,
                        1));
        group.setText(Messages.getLabelString("manage.search.group")); //$NON-NLS-1$

        GridData gridDataCheckRestrict = new GridData();
        gridDataCheckRestrict.horizontalSpan = 5;
        gridDataCheckRestrict.horizontalAlignment = GridData.FILL;
        doNotRestrict = new Button(group, SWT.RADIO);
        nak = new Button(group, SWT.RADIO);
        uak = new Button(group, SWT.RADIO);

        uak.setLayoutData(gridDataCheckRestrict);
        nak.setLayoutData(gridDataCheckRestrict);
        doNotRestrict.setLayoutData(gridDataCheckRestrict);
        doNotRestrict.setSelection(doNotRestrictState);

        nak.setText(Messages.getLabelString("manage.results.error"));
        uak.setText(Messages.getLabelString("manage.results.uak"));
        doNotRestrict.setText(Messages
                .getLabelString("manage.results.norestrict"));
        nak
                .setToolTipText(Messages
                        .getLabelString("manage.search.nak.tooltip"));
        uak
                .setToolTipText(Messages
                        .getLabelString("manage.search.uak.tooltip"));

        nak.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button b = (Button) e.getSource();
                nakSelectionState = b.getSelection();
                if (b.getSelection()) {
                    nakSelectionState = true;
                    uakSelectionState = false;
                    doNotRestrictState = false;
                }
            }
        });
        doNotRestrict.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button b = (Button) e.getSource();
                if (b.getSelection()) {
                    nakSelectionState = false;
                    uakSelectionState = false;
                    doNotRestrictState = true;
                }
            }
        });
        uak.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                Button b = (Button) e.getSource();
                if (b.getSelection()) {
                    nakSelectionState = false;
                    uakSelectionState = true;
                    doNotRestrictState = false;
                }
            }
        });

        new Label(group, SWT.NONE).setText(Messages
                .getLabelString("manage.search.label.from")); //$NON-NLS-1$
        fromDate = new DateTime(group, SWT.DATE);
        fromTime = new DateTime(group, SWT.TIME);
        checkFrom = new Button(group, SWT.CHECK);
        new Label(group, SWT.NONE); // empty

        new Label(group, SWT.NONE).setText(Messages
                .getLabelString("manage.search.label.to")); //$NON-NLS-1$
        toDate = new DateTime(group, SWT.DATE);
        toTime = new DateTime(group, SWT.TIME);
        checkTo = new Button(group, SWT.CHECK);
        searchButton = new Button(group, SWT.PUSH);

        checkFrom.setText(Messages.getLabelString("manage.search.check.lower")); //$NON-NLS-1$
        checkTo.setText(Messages.getLabelString("manage.search.check.upper")); //$NON-NLS-1$

        fromDate.setDay(fromDate.getDay() - 1);
        fromDate.setEnabled(false);
        fromTime.setEnabled(false);

        toDate.setEnabled(false);
        toTime.setEnabled(false);

        searchButton.setText(Messages.getLabelString("manage.search.button")); //$NON-NLS-1$
        Display display = Display.getCurrent();
        if (display == null || display.isDisposed()) {
            searchImageDescriptor = null;
            searchImage = null;

        } else {
            searchImageDescriptor = Activator
                    .getImageDescriptor("icons/find.png");
            searchImage = searchImageDescriptor.createImage();
        }
        searchButton.setImage(searchImage);
        searchButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent event) {
                IHandlerService service = (IHandlerService) PlatformUI
                        .getWorkbench().getService(IHandlerService.class);

                try {
                    service
                            .executeCommand(
                                    "org.openehealth.ipf.platform.manager.flowmanager.ui.search",
                                    null);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NotDefinedException e) {
                    e.printStackTrace();
                } catch (NotEnabledException e) {
                    e.printStackTrace();
                } catch (NotHandledException e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });

        checkFrom.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                fromDate.setEnabled(selected);
                fromTime.setEnabled(selected);

            }
        });

        checkTo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                boolean selected = ((Button) e.widget).getSelection();
                toDate.setEnabled(selected);
                toTime.setEnabled(selected);
            }
        });
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) editorInput
                .getAdapter(IConnectionConfiguration.class);
        // set the enable / disable state of the components
        updateStateOfGuiComponents(connectionConfiguration);

    }

    public Date getFromDate() {
        if (!checkFrom.getSelection()) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(fromDate.getYear(), fromDate.getMonth(),
                fromDate.getDay(), fromTime.getHours(), fromTime.getMinutes(),
                fromTime.getSeconds());
        return calendar.getTime();
    }

    public Date getToDate() {
        if (!checkTo.getSelection()) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(toDate.getYear(), toDate.getMonth(), toDate.getDay(),
                toTime.getHours(), toTime.getMinutes(), toTime.getSeconds());
        return calendar.getTime();
    }

    public void dispose() {
        // first remove it
        Activator.getFlowManagerApplicationController().deleteObserver(this);
        this.saveState();
        if (searchImageDescriptor != null)
            if (searchImage != null)
                searchImageDescriptor.destroyResource(searchImage);
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
            IConnectionConfiguration connectionConfigurationContext = event
                    .getConnectionConfigurationContext();
            if (connectionConfigurationContext == null)
                return;

            IConnectionConfiguration thisConnectionConfiguration = (IConnectionConfiguration) editorInput
                    .getAdapter(IConnectionConfiguration.class);
            if (connectionConfigurationContext
                    .equals(thisConnectionConfiguration)) {
                // update only then the state!

                UpdateGuiStateRunnable updateStateRunnable = new UpdateGuiStateRunnable(
                        connectionConfigurationContext);
                JobUtils.runSafe(updateStateRunnable);
            }
        }
    }

    public synchronized IFlowManagerSearchCriteria buildSearchCriteria() {

        Date from = getFromDate();
        Date to = getToDate();
        if (from != null) {
            if (from.after(new Date(System.currentTimeMillis()))) {
                showCriteriaInvalidNotificationWarning(FROM_AFTER_CURRENTDATE_KEY);
                return null;
            }
        }
        if (from != null && to != null) {
            if (from.after(to)) {
                showCriteriaInvalidNotificationWarning(FROM_AFTER_TO_KEY);
                return null;
            }
        }
        boolean isRestrictedToActiveFlows = uak.getSelection();
        boolean isRestricteToError = nak.getSelection();
        FlowManagerSearchCriteriaImpl searchCriteria = new FlowManagerSearchCriteriaImpl(
                from, to, isRestrictedToActiveFlows, isRestricteToError);
        return searchCriteria;
    }

    private void showCriteriaInvalidNotificationWarning(String resourceKey) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();

        MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        box.setMessage(Messages.getLabelString(resourceKey));
        box
                .setText(Messages
                        .getLabelString(ERROR_BUILDING_SEARCH_CRITERIA_KEY));
        box.open();
    }

    final class UpdateGuiStateRunnable implements Runnable {

        private final IConnectionConfiguration connectionConfiguration;

        public UpdateGuiStateRunnable(
                IConnectionConfiguration connectionConfiguration) {
            this.connectionConfiguration = connectionConfiguration;
        }

        @Override
        public void run() {
            updateStateOfGuiComponents(connectionConfiguration);

        }
    }

    private void updateStateOfGuiComponents(
            IConnectionConfiguration connectionConfiguration) {

        if (!contorller.isFlowManagerRegistered(connectionConfiguration)) {
            this.searchButton.setEnabled(false);
        } else {
            this.searchButton.setEnabled(true);
        }
    }

    private File getStateFile() {
        IPath path = Activator.getDefault().getStateLocation();
        if (path == null) {
            return null;
        }
        path = path.append("FlowManagerEditor.xml");
        return path.toFile();
    }

    private void saveMementoToFile(XMLMemento memento) {
        File stateFile = getStateFile();
        if (stateFile != null) {
            try {
                FileOutputStream stream = new FileOutputStream(stateFile);
                OutputStreamWriter writer = new OutputStreamWriter(stream,
                        "utf-8"); //$NON-NLS-1$
                memento.save(writer);
                writer.close();
            } catch (IOException ioe) {
                stateFile.delete();
            }
        }
    }

    private synchronized void readAndSetState() {
        try {
            XMLMemento memento = XMLMemento.createReadRoot(new BufferedReader(
                    new FileReader(getStateFile())));
            IMemento thisMemento = memento.getChild(EDITOR_SEARCH_OPTIONS_KEY);
            if (thisMemento != null) {
                readComponentState(thisMemento);
            }
        } catch (WorkbenchException we) {
            // ignoreit
        } catch (FileNotFoundException fnfe) {
            // ignore
        }
    }

    protected synchronized void saveState() {
        XMLMemento memento = XMLMemento
                .createWriteRoot(EDITOR_SEARCH_OPTIONS_KEY);
        IMemento child = memento.createChild(EDITOR_SEARCH_OPTIONS_KEY);
        writeComponentState(child);
        saveMementoToFile(memento);
    }

    /**
     * reads the button state from a memento
     * 
     * @param memento
     */
    private void readComponentState(IMemento memento) {
        try {
            int nakSelected = memento.getInteger(NAK_SELECTED_KEY);
            if (nakSelected == 1) {
                nak.setSelection(true);
                nakSelectionState = true;
            } else {
                nak.setSelection(false);
                nakSelectionState = false;
            }

            int uakSelected = memento.getInteger(UAK_SELECTED_KEY);
            if (uakSelected == 1) {
                uak.setSelection(true);
                uakSelectionState = true;
            } else {
                uak.setSelection(false);
                uakSelectionState = false;
            }
            int doNotRestrictSelected = memento
                    .getInteger(DO_NOT_RESTRICT_SEARCH_KEY);
            if (doNotRestrictSelected == 1) {
                doNotRestrict.setSelection(true);
                doNotRestrictState = true;
            } else {
                doNotRestrict.setSelection(false);
                doNotRestrictState = false;
            }
        } catch (Exception e) {
            // if the memento is not ok
            nak.setSelection(false);
            nakSelectionState = false;

            uak.setSelection(false);
            uakSelectionState = false;

            doNotRestrict.setSelection(true);
            doNotRestrictState = true;
        }
    }

    private void writeComponentState(IMemento memento) {
        if (nakSelectionState == true) {
            memento.putInteger(NAK_SELECTED_KEY, 1);
        } else {
            memento.putInteger(NAK_SELECTED_KEY, 0);
        }
        if (uakSelectionState == true) {
            memento.putInteger(UAK_SELECTED_KEY, 1);
        } else {
            memento.putInteger(UAK_SELECTED_KEY, 0);
        }
        if (doNotRestrictState == true) {
            memento.putInteger(DO_NOT_RESTRICT_SEARCH_KEY, 1);
        } else {
            memento.putInteger(DO_NOT_RESTRICT_SEARCH_KEY, 0);
        }
    }

}
