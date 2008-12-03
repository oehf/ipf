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
package org.openehealth.ipf.platform.manager.flowmanager.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.ConnectionUtils;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.view.FlowContnetViewPart;

/**
 * Utility class which contains common logic to activate the message view.
 * 
 * @author Mitko Kolev
 */
public abstract class ShowContentHandlerAbstract extends AbstractHandler {

    public IConnectionConfiguration getSelectionConnection() {
        ISelectionService selectionService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService();

        ISelection selection = selectionService.getSelection();
        IConnectionConfiguration connection = ConnectionUtils
                .getSingleConnectionConfigurationObject(selection);
        return connection;
    }

    public IFlowInfo getSelectionFlow() {
        ISelectionService selectionService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();
        if (selection.isEmpty()) {
            return null;
        }
        List<IFlowInfo> flowInfos = SelectionUtils.getFlows(selection);
        if (flowInfos.size() != 1)
            return null;
        return flowInfos.get(0);

    }

    public void activateMessageView() {
        IWorkbenchPage page = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
        IViewPart part = page.findView(FlowContnetViewPart.class.getName());
        if (part == null) {
            try {
                page.showView(FlowContnetViewPart.class.getName());
            } catch (PartInitException pe) {
                // ignore
            }
        }
        page.activate(part);
    }
}
