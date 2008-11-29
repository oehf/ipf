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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.openehealth.ipf.platform.manager.connection.ui.wizards.NewConnectionWizard;

/**
 * 
 * Handles the NEW connection command.
 * 
 * @author Mitko Kolev
 */
public class NewConnectionHandler extends AbstractHandler {

    private final static Log log = LogFactory
            .getLog(NewConnectionHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
     * .ExecutionEvent)
     */
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
            NewConnectionWizard wizard = new NewConnectionWizard(null);
            wizard.init(PlatformUI.getWorkbench(), null);
            // Instantiates the wizard container with the wizard and opens it
            WizardDialog dialog = new WizardDialog(part.getSite().getShell(),
                    wizard);
            dialog.create();
            dialog.open();
        } catch (Throwable t) {
            log.error("Error opening the new connection wizard", t);
        }
        return null;
    }

}
