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
 * Thrown if the event engine was unable to find a channel that accepts the
 * given delivery options of an event object
 * @author Jens Riemschneider
 */
public class UnsupportedDeliveryModeException extends RuntimeException {
    /** Serial Version UID */
    private static final long serialVersionUID = 2469121107461231348L;

    /**
     * Constructs the exception
     * @param mode
     *          the delivery mode causing the problem
     */
    public UnsupportedDeliveryModeException(DeliveryMode mode) {
        super("Failed to find channel supporting delivery mode: " + mode);
    }
}
