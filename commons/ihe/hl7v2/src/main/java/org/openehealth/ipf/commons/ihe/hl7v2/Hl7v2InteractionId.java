/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2;

import org.openehealth.ipf.commons.ihe.core.InteractionId;

/**
 * HL7v2 Transaction ID
 *
 * @author Christian Ohr
 * @since 3.2
 */
public interface Hl7v2InteractionId extends InteractionId {

    Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration();

    NakFactory getNakFactory();

    /**
     * Optional initialization with dynamic TransactionOptions
     * @param options transaction options
     */
    default void init(TransactionOptions... options) {}
}
