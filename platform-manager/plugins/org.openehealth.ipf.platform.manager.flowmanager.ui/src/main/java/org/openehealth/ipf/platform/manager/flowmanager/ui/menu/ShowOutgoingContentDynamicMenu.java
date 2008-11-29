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
package org.openehealth.ipf.platform.manager.flowmanager.ui.menu;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.ConnectionUtils;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.handlers.SelectionUtils;
import org.openehealth.ipf.platform.manager.flowmanager.ui.handlers.ShowOutgoingContentHandler;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * Populates dynamically the parts sub-menu 
 * 
 * @author Mitko Kolev
 */
public class ShowOutgoingContentDynamicMenu extends ContributionItem {

    private final String FLOW_PART_NAME = Messages
            .getLabelString("flow.info.outgoing");
    private final String FLOW_PATH_NAME = Messages
            .getLabelString("flow.part.path");

    public ShowOutgoingContentDynamicMenu() {

    }

    public ShowOutgoingContentDynamicMenu(String id) {
        super(id);
    }

    protected IContributionItem[] getContributionItems() {
        // Here's where you would dynamically generate your list
        IContributionItem[] list = new IContributionItem[1];
        ISelectionService selectionService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();
        if (selection.isEmpty()) {
            return new IContributionItem[] {};
        }
        List<IFlowInfo> flowInfos = SelectionUtils.getFlows(selection);
        if (flowInfos.size() != 1) {
            return new IContributionItem[] {};
        }
        return list;
    }

    @Override
    public void fill(Menu menu, int index) {
        ISelectionService selectionService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService();
        MenuItem partsMi = new MenuItem(menu, SWT.CASCADE, index);
        partsMi.setText(FLOW_PART_NAME);
        Menu partsMenu = new Menu(menu);
        partsMi.setMenu(partsMenu);
        
        //add the dynamic sub menu
        ISelection selection = selectionService.getSelection();
        if (selection.isEmpty()) {
            partsMi.setEnabled(false);
            return;
        }
        List<IFlowInfo> flowInfos = SelectionUtils.getFlows(selection);
        if (flowInfos.size() != 1) {
            partsMi.setEnabled(false);
            return;
        }
        IConnectionConfiguration connection = ConnectionUtils
                .getSingleConnectionConfigurationObject(selection);
        if (connection == null) {
            return;
        }
        IFlowInfo info = flowInfos.get(0);
        List<IFlowPartInfo> parts = info.getPartInfos();

        
        for (int t = 0; t < parts.size(); t++) {
            final MenuItem mi = new MenuItem(partsMenu, SWT.PUSH, t);
            IFlowPartInfo partInfo = parts.get(t);
            mi.addSelectionListener(new ShowFlowPartSelectionListener(info,
                    partInfo, connection));
            mi.setText(FLOW_PATH_NAME + " " + partInfo.getPath());
        }
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    class ShowFlowPartSelectionListener implements SelectionListener {
        private final ShowOutgoingContentHandler handler;

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org
         * .eclipse.swt.events.SelectionEvent)
         */
        public ShowFlowPartSelectionListener(IFlowInfo flowInfo,
                IFlowPartInfo flowPartInfo, IConnectionConfiguration connection) {
            handler = new ShowOutgoingContentHandler(connection, flowInfo,
                    flowPartInfo);
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse
         * .swt.events.SelectionEvent)
         */
        @Override
        public void widgetSelected(SelectionEvent e) {
            handler.executeShowPartMessageText();
        }

    }
}
