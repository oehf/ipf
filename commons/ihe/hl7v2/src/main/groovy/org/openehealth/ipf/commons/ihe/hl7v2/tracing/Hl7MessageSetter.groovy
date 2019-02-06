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

import brave.propagation.Propagation
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import org.openehealth.ipf.modules.hl7.dsl.Repeatable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Add a key/value QIP repetition to ZTR-1
 *
 * @author Christian Ohr
 */
class Hl7MessageSetter implements Propagation.Setter<Message, String> {

    private static final Logger LOG = LoggerFactory.getLogger(Hl7MessageSetter)

    private final String segmentName

    Hl7MessageSetter(String segmentName = 'ZTR') {
        this.segmentName = segmentName
    }

    private static def nextRepetition(Repeatable closure) {
        return closure(closure().size())
    }

    @Override
    void put(Message msg, String key, String value) {
        if (msg && key) {
            def qip = Composite.QIP(msg) //, [segmentFieldName: key, values: value ?: ''])
            qip[1] = key
            qip[2] = value ?: ''
            def varies = nextRepetition(msg.get(segmentName)[1])
            varies.data = qip

            if (LOG.isDebugEnabled()) {
                LOG.debug("Added trace context with key [{}] and value [{}]", key, value)
            }
        }
    }
}
