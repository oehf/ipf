/*
 * Copyright 2008 InterComponentWare AG.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.adapter.FlowInfoAdapter;

/**
 * Utility class for extracting data from the selection.
 * 
 * @author Mitko Kolev
 */
public class SelectionUtils {

    @SuppressWarnings("unchecked")
    public static List<IFlowInfo> getFlows(ISelection selection) {
        List<IFlowInfo> flows = new ArrayList<IFlowInfo>();
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection is = (IStructuredSelection) selection;
            if (is.isEmpty()) {
                return flows;
            }
            Iterator flowsIterator = is.iterator();
            while (flowsIterator.hasNext()) {
                FlowInfoAdapter adapter = (FlowInfoAdapter) flowsIterator
                        .next();
                flows.add(adapter.getFlowInfo());
            }
        }
        return flows;
    }
}