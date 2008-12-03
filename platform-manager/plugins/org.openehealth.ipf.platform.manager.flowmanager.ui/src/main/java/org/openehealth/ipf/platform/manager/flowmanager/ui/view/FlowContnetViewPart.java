/*
 * Copyright the original author or authors.
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
package org.openehealth.ipf.platform.manager.flowmanager.ui.view;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.openehealth.ipf.platform.manager.connection.ui.utils.jobs.JobUtils;
import org.openehealth.ipf.platform.manager.flowmanager.FlowManagerEvent;
import org.openehealth.ipf.platform.manager.flowmanager.ui.osgi.Activator;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * @author Mitko Kolev
 */
public class FlowContnetViewPart extends ViewPart implements Observer {

    private static final String NO_TEXT_AVAILABLE = Messages
            .getLabelString("no.rendered.text.availabe");
    private Text activePageText;

    public FlowContnetViewPart() {
        Activator.getFlowManagerApplicationController().addObserver(this);
    }

    @Override
    public void setFocus() {
        // activePageText.setFocus();
    }

    @Override
    public void createPartControl(Composite parent) {
        activePageText = new Text(parent, SWT.H_SCROLL | SWT.V_SCROLL);
    }

    @Override
    public void dispose() {
        Activator.getFlowManagerApplicationController().deleteObserver(this);
        super.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof FlowManagerEvent) {
            FlowManagerEvent event = (FlowManagerEvent) arg;

            if (event.getType() == FlowManagerEvent.FLOWMANAGER_MESSAGE_RECEIVED) {

                UpdateGuiStateRunnable runnable = new UpdateGuiStateRunnable(
                        event.getValue());
                JobUtils.runSafe(runnable);

            }
        }
    }

    final class UpdateGuiStateRunnable implements Runnable {
        private final Object value;

        UpdateGuiStateRunnable(Object value) {
            this.value = value;

        }

        public void run() {
            if (value == null) {
                activePageText.setText(NO_TEXT_AVAILABLE);
            } else {
                activePageText.setText(value.toString());
            }
        }

    }

}
