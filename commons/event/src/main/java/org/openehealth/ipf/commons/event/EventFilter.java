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

/**
 * Standard interface for event filters
 * @author Jens Riemschneider
 */
public interface EventFilter {
    /**
     * Called by the {@link EventEngine} to test if an event should be passed to 
     * a subscribed handler
     * @param eventObject
     *          the event object representing the event that occurred
     * @return <code>true</code> if the filter accepts the event and allows handling
     *         of it via the subscribed handler
     */
    boolean accepts(EventObject eventObject);
}
