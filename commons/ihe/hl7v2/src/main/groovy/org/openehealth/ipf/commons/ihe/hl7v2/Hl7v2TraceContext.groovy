/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.EncodingCharacters
import ca.uhn.hl7v2.parser.DefaultEscaping
import ca.uhn.hl7v2.parser.Escaping
import ca.uhn.hl7v2.preparser.PreParser
import org.openehealth.ipf.modules.hl7.dsl.Repeatable

/**
 * Reads and writes trace context carried in a nonstandard HL7v2 segment, by default {@code ZTR}.
 * <p>
 * HL7v2 has no standard mechanism for this, so the layout is IPF's own and only interoperates between
 * IPF instances: field 1 of the segment repeats, and every repetition is a composite of the propagation
 * key and its value, i.e. in encoded form
 * <pre>
 * ZTR|traceparent^00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01
 * </pre>
 * Both directions leave the encoding to HAPI: writing builds real segment fields, so values containing
 * delimiters are escaped, and reading goes through {@link PreParser}, which takes the encoding
 * characters from MSH-1 and MSH-2 of the message at hand.
 * <p>
 * Reading works on the encoded message because that is the only form available when it is needed: the
 * span for an incoming message is created before the consumer interceptor chain unmarshals.
 *
 * @author Christian Ohr
 * @since 6.0
 */
class Hl7v2TraceContext {

    /** Name of the segment carrying the trace context. */
    public static final String SEGMENT_NAME = 'ZTR'

    /**
     * Upper bound for the number of propagation entries read from a message, so that a malformed or
     * malicious message cannot make this loop forever. Propagation formats use a handful of entries.
     */
    private static final int MAX_ENTRIES = 16

    /** {@link PreParser} returns field text as it stands in the message, escapes included. */
    private static final Escaping ESCAPING = new DefaultEscaping()

    private Hl7v2TraceContext() {
    }

    /**
     * Extracts the trace context from an encoded HL7v2 message.
     *
     * @param message an encoded HL7v2 message, may be {@code null}.
     * @return the propagation keys and values found, empty if the message carries none or cannot be
     *      read at all.
     */
    static Map<String, String> read(String message) {
        Map<String, String> result = [:]
        if (!message?.trim()) {
            return result
        }
        // one call for all repetitions: PreParser parses the message per call, and asking repetition by
        // repetition would parse it again every time. MSH-1 and MSH-2 come along for the unescaping.
        def paths = ['MSH-1', 'MSH-2'] + (0..<MAX_ENTRIES).collectMany { entry ->
            ["${SEGMENT_NAME}-1(${entry})-1".toString(), "${SEGMENT_NAME}-1(${entry})-2".toString()]
        }
        try {
            def fields = PreParser.getFields(message, paths as String[])
            def encodingCharacters = new EncodingCharacters(
                    fields[0] ? fields[0].charAt(0) : '|' as char, fields[1])
            for (entry in 0..<MAX_ENTRIES) {
                def key = fields[entry * 2 + 2]
                if (!key?.trim()) {
                    break
                }
                result.put(ESCAPING.unescape(key, encodingCharacters),
                        ESCAPING.unescape(fields[entry * 2 + 3] ?: '', encodingCharacters))
            }
        } catch (HL7Exception ignored) {
            // not something that can be read as an HL7v2 message, hence no trace context in it
        }
        result
    }

    /**
     * Adds the given trace context to a message, as a segment of its own. Nothing happens if there is
     * nothing to add.
     *
     * @param message a parsed message.
     * @param traceContext propagation keys and values.
     */
    static void write(Message message, Map<String, String> traceContext) {
        if (message == null || !traceContext) {
            return
        }
        if (!message.names.contains(SEGMENT_NAME)) {
            message.addNonstandardSegment(SEGMENT_NAME)
        }
        def segment = message.get(SEGMENT_NAME)
        traceContext.each { key, value ->
            def qip = Composite.QIP(message)
            qip[1] = key
            qip[2] = value ?: ''
            nextRepetition(segment[1]).data = qip
        }
    }

    private static def nextRepetition(Repeatable closure) {
        closure(closure().size())
    }
}
