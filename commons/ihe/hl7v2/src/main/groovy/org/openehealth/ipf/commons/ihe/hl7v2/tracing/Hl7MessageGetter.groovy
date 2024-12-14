/*
 * Copyright 2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2.tracing


import ca.uhn.hl7v2.model.AbstractMessage
import io.micrometer.tracing.propagation.Propagator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Reads a value from ZTR[1], given a key
 *
 * @author Christian Ohr
 */
class Hl7MessageGetter implements Propagator.Getter<AbstractMessage> {

    private static final Logger log = LoggerFactory.getLogger(Hl7MessageGetter)
    private final String segmentName

    Hl7MessageGetter(String segmentName = 'ZTR') {
        this.segmentName = segmentName
    }

    @Override
    String get(AbstractMessage msg, String key) {
        def ztr = msg?.find { it.name == segmentName }
        def qips = ztr ? ztr[1] : null
        def qip = qips ? qips().find { q -> q[1].value == key } : null
        String value = qip ? qip[2]?.value : null

        if (log.isDebugEnabled()) {
            log.debug("Extracted trace context with key [{}] and value [{}]", key, value)
        }
        value
    }

}
