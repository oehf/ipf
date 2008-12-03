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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.jobs.GetIncomingContentJob;

/**
 * Shows the content of the outgoing message.
 * 
 * @author Mitko Kolev
 */
public class ShowIncomingContentHandler extends ShowContentHandlerAbstract {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IFlowInfo flowInfo = super.getSelectionFlow();
        if (flowInfo == null)
            return null;
        IConnectionConfiguration connection = super.getSelectionConnection();
        if (connection == null)
            return null;

        activateMessageView();
        GetIncomingContentJob job = new GetIncomingContentJob(Display
                .getCurrent(), connection, flowInfo);
        job.schedule();
        return null;
    }

}
