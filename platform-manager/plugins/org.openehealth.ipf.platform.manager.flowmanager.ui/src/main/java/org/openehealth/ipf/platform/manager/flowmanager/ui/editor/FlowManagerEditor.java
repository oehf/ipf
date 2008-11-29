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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerSearchCriteria;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;
import org.openehealth.ipf.platform.manager.flowmanager.ui.view.SearchView;

/**
 * Provides UI for "editing" flow manager instances. The flowManager editor is
 * the default editor of connections. Registered in the platform as the default
 * editor for .connection extensions.
 * 
 * @see org.openehealth.ipf.platform.manager.connection.ui.editor.ConnectionEditorInput
 * 
 * @author Mitko Kolev
 */
public class FlowManagerEditor extends EditorPart {

    private final Log log = LogFactory.getLog(FlowManagerEditor.class);

    private FlowManagerOptionsMediator optionsMediator;

    private FlowManagerSearchMediator searchMediator;

    private final IFlowManagerApplicationController flowManagerApplicationController;

    /**
     * Constructs a flow Manager editor.
     */
    public FlowManagerEditor() {
        super();
        flowManagerApplicationController = Activator
                .getFlowManagerApplicationController();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createPartControl(Composite parent) {
        final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL
                | SWT.H_SCROLL);
        final Composite group = new Composite(sc, SWT.NONE);
        GridLayout layout = new GridLayout(5, false);
        group.setLayout(layout);

        Group applicationOptionsGroup = new Group(group, SWT.NONE);
        applicationOptionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
                true, false, 4, 1));
        applicationOptionsGroup.setText(Messages
                .getLabelString("manage.flowmanager.group")); //$NON-NLS-1$
        applicationOptionsGroup.setLayout(new GridLayout(1, false));

        optionsMediator = new FlowManagerOptionsMediator(
                applicationOptionsGroup, this.getEditorInput());

        searchMediator = new FlowManagerSearchMediator(applicationOptionsGroup,
                this.getEditorInput());

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        sc.setContent(group);
        sc.setMinSize(group.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        try {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().showView(SearchView.class.getName());
        } catch (PartInitException e) {
            log.error("Cannot show the Flow View", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
     * org.eclipse.ui.IEditorInput)
     */
    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(input.getName());
    }

    @Override
    public void setFocus() {
        // distinguish the current GUI session
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this
                .getEditorInput().getAdapter(IConnectionConfiguration.class);
        flowManagerApplicationController
                .onActiveFlowManagerChanged(connectionConfiguration);
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveOnCloseNeeded() {
        return false;
    }

    /**
     * Sets input only for not null connections!
     */
    @Override
    protected void setInput(IEditorInput input) {
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) input
                .getAdapter(IConnectionConfiguration.class);
        if (connectionConfiguration == null) {
            // do not allow this, do nothing
            return;
        }
        super.setInput(input);

    }

    /**
     * Returns null if the criteria cannot be built.
     * 
     * @return
     */
    public synchronized IFlowManagerSearchCriteria buildSearchCriteria() {
        return searchMediator.buildSearchCriteria();
    }

    @Override
    public void dispose() {
        IConnectionConfiguration connectionConfiguration = (IConnectionConfiguration) this
                .getEditorInput().getAdapter(IConnectionConfiguration.class);
        super.dispose();
        // dispose all mediators
        optionsMediator.dispose();
        searchMediator.dispose();
        this.flowManagerApplicationController
                .onActiveFlowManagerChanged(connectionConfiguration);
        // call the dispose at last
    }

}
