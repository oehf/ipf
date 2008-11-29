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
package org.openehealth.ipf.platform.manager.flowmanager;

import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Event to indicate FlowManager related change of state.
 * <p>
 * 
 * @see ConnectionEvent
 * 
 * @author Mitko Kolev
 */
public class FlowManagerEvent extends ConnectionEvent {

    public static final int FLOWMANAGER_UNREGISTERED = 0x10000000;

    public static final int FLOWMANAGER_REGISTERED = 0x20000000;

    public static final int FLOWS_RECEIVED = 0x40000000;

    public static final int FLOWMANAGER_ATTRIBUTE_CHANGED = 0x80000000;

    public static final int FLOWMANAGER_VIEW_CHANGE = 0xf0000000;

    public static final int FLOWMANAGER_APPLICATION_CHANGED = 0x81000000;

    public static final int FLOWMANAGER_MESSAGE_RECEIVED = 0x82000000;

    /**
     * Creates a FlowManagerEvent.
     * 
     * @param connectionConfiguration
     *            the connection to which the event belongs (the connection
     *            source)
     * @param type
     *            the type of the event
     * @param value
     *            an object associated with the event (dynamic).
     */
    public FlowManagerEvent(IConnectionConfiguration connectionConfiguration,
            int type, Object value) {
        super(connectionConfiguration, type, value);
    }

    /**
     * Creates a FlowManagerEvent.
     * 
     * @param connectionConfiguration
     *            onnection the connection to which the event belongs (the
     *            connection source)
     * @param type
     *            the type of the event
     */
    public FlowManagerEvent(IConnectionConfiguration connectionConfiguration,
            int type) {
        super(connectionConfiguration, type);
    }

}
