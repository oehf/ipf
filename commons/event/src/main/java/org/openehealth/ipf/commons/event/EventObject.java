/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.event;

import static org.apache.commons.lang.Validate.notNull;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of an event object
 * <p>
 * This class can be used as a base class for event implementations.
 * It provides meta data support for events via a simple {@code Map}.
 * <p>
 * Subclasses should be aware that an event object is treated as
 * immutable after {@link EventEngine#publish} was called. This cannot
 * be enforced by the event engine itself. Subclasses should therefore
 * use final immutable members if possible.
 * @author Jens Riemschneider
 */
public class EventObject implements Serializable {
    /** Serial Version UID */
    private static final long serialVersionUID = -7451402744233567966L;

    // This map is treated as unmodifiable after EventEngine.publish() has
    // been called. Ideally it would be created and filled within the constructor.
    // However, the event engine does not construct these objects, it simply
    // publishes them. This means that write access to this map must be possible
    // simply to be able to fill in the meta data in publish().
    private final Map<String, Object> metaDataMap = new HashMap<String, Object>();

    /**
     * Returns a meta data property from this event
     * @param key
     *          the key, specifying the meta data property
     * @return the value of the property
     */
    public Object getMetaData(String key) {
        notNull(key, "key cannot be null");
        return metaDataMap.get(key);
    }

    /**
     * Sets a meta data property for this event.
     * <p>
     * Meta data should only be set before calling {@link EventEngine#publish}.
     * Therefore, this method is package-private. Due to the nature of the
     * Java language this method cannot be hidden from any subclassing
     * event objects. Such classes should not call this method.
     * @param key
     *          the key, specifying the meta data property
     * @param value
     *          the value of the property
     */
    final void setMetaData(String key, Object value) {
        notNull(key, "key cannot be null");
        metaDataMap.put(key, value);
    }

    /**
     * Public meta data keys provided by the IPF infrastructure   
     */
    public enum MetaDataKeys {        
        /** the time stamp of the creation of the event. Stored as a {@link Date} */
        TIME_STAMP("org.openehealth.ipf.commons.event.metadata.timestamp"),
        /** the unique ID of the event. Stored as a {@code String} */
        EVENT_ID("org.openehealth.ipf.commons.event.metadata.eventid"),
        /** the topic that the event is published with */
        TOPIC("org.openehealth.ipf.commons.event.metadata.topic");

        private final String key;

        private MetaDataKeys(String key) {
            this.key = key;
        }

        /**
         * @return the {@code String}-based key of the meta field
         */
        public String getKey() {
            return key;
        }
    }
}
