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
package org.openehealth.ipf.platform.manager.connection.ui.handlers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.connection.ui.utils.ConnectionUtils;
import org.openehealth.ipf.platform.manager.connection.ui.wizards.NewConnectionWizard;

/**
 * Basic PROPERTIES action of a connection.
 * 
 * @see IConnectionConfiguration
 * 
 * @author Mitko Kolev
 */
public class PropertiesHandler extends AbstractHandler {

    private static Log log = LogFactory.getLog(PropertiesHandler.class);

    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
            // do nothing
            return null;
        }
        log.info("Opening the new Connection wizard");
        IWorkbenchPage page = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return null;
        }
        IWorkbenchPart part = page.getActivePart();
        if (part == null) {
            log.error("Active part is null!");
            return null;
        }
        try {
            ISelection selection = HandlerUtil.getCurrentSelection(arg0);
            if (selection.isEmpty())
                return null;
            List<IConnectionConfiguration> connectionConfigurations = ConnectionUtils
                    .getConnectionConfigurations(selection);
            if (connectionConfigurations.size() == 1) {
                NewConnectionWizard wizard = new NewConnectionWizard(
                        connectionConfigurations.get(0));
                wizard.init(PlatformUI.getWorkbench(), null);
                // Instantiates the wizard container with the wizard and opens
                // it
                WizardDialog dialog = new WizardDialog(part.getSite()
                        .getShell(), wizard);
                dialog.create();
                dialog.open();
            }
        } catch (Throwable t) {
            log.error("Error opening the new connection wizard", t);
        }
        return null;
    }

}
