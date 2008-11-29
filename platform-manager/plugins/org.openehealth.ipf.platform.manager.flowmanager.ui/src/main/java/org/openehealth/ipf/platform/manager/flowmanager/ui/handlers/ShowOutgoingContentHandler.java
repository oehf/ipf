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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.GetOutgoingContentJob;

/**
 * Handler to show the FlowPart Message text of a flow Part. Since the parts a
 * dynamic, this handler must be used only programatically.
 * 
 * @author Mitko Kolev
 */
public class ShowOutgoingContentHandler extends ShowContentHandlerAbstract {

    public final static int FLOW_PART_INDEX_DEFAULT = 0;

    private final IFlowInfo flowInfo;

    private final IFlowPartInfo flowPartInfo;

    private final IConnectionConfiguration connection;

    public ShowOutgoingContentHandler(IConnectionConfiguration connection,
            IFlowInfo flowInfo, IFlowPartInfo flowPartInfo) {
        this.flowPartInfo = flowPartInfo;
        this.flowInfo = flowInfo;
        this.connection = connection;

    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        throw new ExecutionException(
                "Use the handler only programatically with executeShowPartMessageText method!");
    }

    /**
     * Activates the message view. Starts a new job.
     */
    public void executeShowPartMessageText() {
        activateMessageView();
        // does not use the selection flowInfo object. The selection flow info
        // object is given in the contsturctor.
        GetOutgoingContentJob job = new GetOutgoingContentJob(Display
                .getCurrent(), connection, flowInfo, flowPartInfo);
        job.schedule();
    }

}
