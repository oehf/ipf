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

import brave.Span
import brave.Tracer
import brave.Tracing
import brave.propagation.Propagation
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.model.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Helper class that injects and extracts tracing information from HL7 messages
 * and creates a new span on producer and consumer side.
 *
 * @author Christian Ohr
 */
class MessageTracer {

    private static final Logger LOG = LoggerFactory.getLogger(MessageTracer.class)

    private static final String HL7_SENDING_APPLICATION = "MSH-3"
    private static final String HL7_SENDING_FACILITY = "MSH-4"
    private static final String HL7_MESSAGE_TYPE = "MSH-9-1"
    private static final String HL7_TRIGGER_EVENT = "MSH-9-2"
    private static final String HL7_PROCESSING_ID = "MSH-11"

    private final Tracing tracing
    private final boolean removeSegment
    private final String segmentName
    private final Propagation.Setter<Message, String> setter
    private final Propagation.Getter<Message, String> getter

    /**
     * @param tracing Tracing instance
     * @param segmentName name of the segment with the propagated tracing information (default: ZTR)
     * @param removeSegment whether the segment with the propagated tracing information is removed (default: true)
     */
    MessageTracer(Tracing tracing, String segmentName = 'ZTR', boolean removeSegment = true) {
        this.tracing = tracing
        this.removeSegment = removeSegment
        this.segmentName = segmentName
        this.setter = new Hl7MessageSetter(segmentName)
        this.getter = new Hl7MessageGetter(segmentName)
    }

    void sendMessage(Message msg, String name, Handler sender) {
        Tracer tracer = tracing.tracer()
        Span span = startSpan(tracer.nextSpan(), Span.Kind.CLIENT, name, msg)
        msg.addNonstandardSegment(segmentName)
        tracing.propagation()
                .injector(setter)
                .inject(span.context(), msg)
        Tracer.SpanInScope ws = tracer.withSpanInScope(span)
        try {
            sender.accept(msg, span)
        } catch (Throwable t) {
            span.error(t)
            throw t
        } finally {
            ws?.close()
            span?.finish()
        }
    }

    void receiveMessage(Message msg, String name, Handler receiver) {
        Tracer tracer = tracing.tracer()
        Span span = startSpan(tracer.nextSpan(
                tracing.propagation()
                        .extractor(getter)
                        .extract(msg)),
                Span.Kind.SERVER, name, msg)
        Tracer.SpanInScope ws = tracer.withSpanInScope(span)
        try {
            if (removeSegment && msg instanceof AbstractMessage) {
                try {
                    msg.removeRepetition(segmentName, 0)
                } catch (HL7Exception ignored) {
                    // TODO LOG something?
                }
            }
            receiver.accept(msg, span)
        } catch (Throwable t) {
            span.error(t)
            throw t
        } finally {
            ws?.close()
            span?.finish()
        }
    }

    private static Span startSpan(Span span, Span.Kind kind, String name, Message msg) {
        span.kind(kind)
                .name(name)
                .tag(HL7_SENDING_APPLICATION, msg.MSH[3]?.value ?: '')
                .tag(HL7_SENDING_FACILITY, msg.MSH[4]?.value ?: '')
                .tag(HL7_MESSAGE_TYPE, msg.MSH[9][1]?.value ?: '')
                .tag(HL7_TRIGGER_EVENT, msg.MSH[9][2]?.value ?: '')
                .tag(HL7_PROCESSING_ID, msg.MSH[11]?.value ?: '')
        // ExtraFieldPropagation.set(span.context(), 'messageId', msg.MSH[11]?.value ?: '')
        span.start()
    }

}
