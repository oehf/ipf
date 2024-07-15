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

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.model.Message
import io.micrometer.tracing.Span
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.propagation.Propagator

/**
 * Helper class that injects and extracts tracing information from HL7 messages
 * and creates a new span on producer and consumer side.
 *
 * @author Christian Ohr
 */
class MessageTracer {

    private static final String HL7_SENDING_APPLICATION = "MSH-3"
    private static final String HL7_SENDING_FACILITY = "MSH-4"
    private static final String HL7_MESSAGE_TYPE = "MSH-9-1"
    private static final String HL7_TRIGGER_EVENT = "MSH-9-2"
    private static final String HL7_PROCESSING_ID = "MSH-11"

    private final Tracer tracer
    private final boolean removeSegment
    private final String segmentName
    private final Propagator propagator;
    private final Propagator.Setter<Message> setter
    private final Propagator.Getter<Message> getter

    /**
     * @param tracer Tracer instance, e.g. BraveTracer
     * @param propagator Propagator instance, e.g. BravePropagator
     * @param segmentName name of the segment with the propagated tracing information (default: ZTR)
     * @param removeSegment whether the segment with the propagated tracing information is removed (default: true)
     */
    MessageTracer(Tracer tracer, Propagator propagator, String segmentName = 'ZTR', boolean removeSegment = true) {
        this.tracer = tracer
        this.removeSegment = removeSegment
        this.segmentName = segmentName
        this.setter = new Hl7MessageSetter(segmentName)
        this.getter = new Hl7MessageGetter(segmentName)
        this.propagator = propagator;
    }

    void sendMessage(Message msg, String name, Handler sender) {
        def span = startSpan(tracer.spanBuilder(), Span.Kind.CLIENT, name, msg)
        msg.addNonstandardSegment(segmentName)
        try (def ws = this.tracer.withSpan(span)) {
            propagator.inject(span.context(), msg, setter)
            sender.accept(msg, span)
        } catch (Throwable t) {
            span.error(t)
            throw t
        } finally {
            span.end()
        }
    }

    void receiveMessage(Message msg, String name, Handler receiver) {
        def span = startSpan(propagator.extract(msg, getter), Span.Kind.SERVER, name, msg)
        try (def ws = tracer.withSpan(span)) {
            if (removeSegment && msg instanceof AbstractMessage) {
                try {
                    msg.removeRepetition(segmentName, 0)
                } catch (HL7Exception ignored) {
                }
            }
            receiver.accept(msg, span)
        } catch (Throwable t) {
            span.error(t)
            throw t
        } finally {
            span.end()
        }
    }

    private static Span startSpan(Span.Builder spanBuilder, Span.Kind kind, String name, Message msg) {
        spanBuilder
            .name(name)
            .kind(kind)
            .tag(HL7_SENDING_APPLICATION, msg.MSH[3]?.value ?: '')
            .tag(HL7_SENDING_FACILITY, msg.MSH[4]?.value ?: '')
            .tag(HL7_MESSAGE_TYPE, msg.MSH[9][1]?.value ?: '')
            .tag(HL7_TRIGGER_EVENT, msg.MSH[9][2]?.value ?: '')
            .tag(HL7_PROCESSING_ID, msg.MSH[11]?.value ?: '')
            .start()
    }

}
