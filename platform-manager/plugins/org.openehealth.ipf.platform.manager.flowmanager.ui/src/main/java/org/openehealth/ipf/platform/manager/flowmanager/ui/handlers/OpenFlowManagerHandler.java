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
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.editor.ConnectionEditorInput;
import org.openehealth.ipf.platform.manager.connection.ui.utils.ConnectionUtils;
import org.openehealth.ipf.platform.manager.flowmanager.ui.editor.FlowManagerEditor;

/**
 * Starts a new UI Job. Works only in the context of an FlowManager editor.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class OpenFlowManagerHandler extends AbstractHandler {

    /**
     * The constructor.
     */
    public OpenFlowManagerHandler() {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        List<IConnectionConfiguration> connectionConfigurations = ConnectionUtils
                .getConnectionConfigurations(selection);
        for (int t = 0; t < connectionConfigurations.size(); t++) {

            final IConnectionConfiguration connectionConfiguration = connectionConfigurations
                    .get(t);

            ConnectionEditorInput input = new ConnectionEditorInput(
                    connectionConfiguration);
            try {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().openEditor(input,
                                FlowManagerEditor.class.getName());
            } catch (PartInitException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
