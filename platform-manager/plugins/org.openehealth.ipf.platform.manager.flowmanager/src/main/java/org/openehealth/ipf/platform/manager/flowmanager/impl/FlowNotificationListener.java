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
package org.openehealth.ipf.platform.manager.flowmanager.impl;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowManagerApplicationController;

/**
 * The controller is the notification callback.
 * <p>
 * 
 * @author Mitko Kolev
 */
public class FlowNotificationListener implements NotificationListener {

    private final IConnectionConfiguration connectionConfiguration;

    private final Log log = LogFactory.getLog(FlowNotificationListener.class);

    private final IFlowManagerApplicationController flowManagerApplicationController;

    public FlowNotificationListener(
            IFlowManagerApplicationController controller,
            IConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
        this.flowManagerApplicationController = controller;
    }

    /**
     * @see javax.management.NotificationListener#handleNotification(javax.management.Notification,
     *      java.lang.Object)
     */
    @Override
    public synchronized void handleNotification(Notification notification,
            Object handback) {
        if (notification instanceof AttributeChangeNotification) {
            AttributeChangeNotification acn = (AttributeChangeNotification) notification;
            // log.debug("\tAttributeName: " + acn.getAttributeName());
            // log.debug("\tAttributeType: " + acn.getAttributeType());
            // log.debug("\tNewValue: " + acn.getNewValue());
            // log.debug("\tOldValue: " + acn.getOldValue());
            flowManagerApplicationController.onAttributeChangeNotification(
                    connectionConfiguration, acn.getAttributeName(), acn
                            .getNewValue(), acn.getOldValue());
            log.debug("Connection " + connectionConfiguration.getName()
                    + " received JMX event for atribute "
                    + acn.getAttributeName());

        }

    }
}
