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
package org.openehealth.ipf.platform.manager.jmxexplorer;

import javax.management.MBeanFeatureInfo;
import javax.management.ObjectName;

import org.openehealth.ipf.platform.manager.connection.ConnectionEvent;
import org.openehealth.ipf.platform.manager.connection.IConnectionConfiguration;

/**
 * Provides events for the JMX related functionality.
 * 
 * In case of INVOKE_OPERATION_ERROR and CHANGE_ATTRIBUTE_VALUE_ERROR, the value
 * of the event is of type Throwable; In case of INVOKE_OPERATION_RESULT, the
 * value of the event is the result of the operation (Object); In case of
 * ATTRIBUTE_VALUE_CHANGED, the value of the event is the new value of the
 * attribute;
 * 
 * @author Mitko Kolev
 */
public class JMXExplorerEvent extends ConnectionEvent {

    public static final int ATTRIBUTE_VALUE_CHANGED = 0x2000000;

    public static final int CHANGE_ATTRIBUTE_VALUE_ERROR = 0x1000000;

    public static final int INVOKE_OPERATION_ERROR = 0x4000000;

    public static final int INVOKE_OPERATION_RESULT = 0x8000000;

    private final ObjectName objectName;

    private final MBeanFeatureInfo source;

    /**
     * @param connectionConfiguration
     *            The connection context of the event.
     * @param type
     *            The type of the event.
     */
    public JMXExplorerEvent(IConnectionConfiguration connectionConfiguration,
            int type, ObjectName objectName, Object newValue,
            MBeanFeatureInfo source) {
        super(connectionConfiguration, type, newValue);
        this.objectName = objectName;
        this.source = source;
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    /**
     * Returns the source of this event. the source is of type MBeanFeatureInfo
     * 
     * @return the source;
     */
    public MBeanFeatureInfo getSource() {
        return source;
    }

}
