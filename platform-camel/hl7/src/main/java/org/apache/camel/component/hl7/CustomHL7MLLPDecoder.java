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

package org.apache.camel.component.hl7;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

/**
 * HL7MLLPDecoder that is aware that a HL7 message can span several buffers.
 * In addition, it avoids rescanning packets by keeping state in the IOSession.
 *
 * This decoder addresses a decoding error that might occur when only the end bytes
 * are transferred in a different frame.
 */
class CustomHL7MLLPDecoder extends CumulativeProtocolDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(CustomHL7MLLPDecoder.class);
    private static final String DECODER_STATE = CustomHL7MLLPDecoder.class.getName() + ".STATE";
    private static final String CHARSET_DECODER = CustomHL7MLLPDecoder.class.getName() + ".charsetdecoder";

    private HL7MLLPConfig config;

    CustomHL7MLLPDecoder(HL7MLLPConfig config) {
        this.config = config;
    }


    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        // Get the state of the current message and
        // Skip what we have already scanned before
        DecoderState state = decoderState(session);
        in.position(state.current());

        LOG.debug("Received data, checking from position {} to {}", in.position(), in.limit());

        while (in.hasRemaining()) {
            int previousPosition = in.position();
            byte current = in.get();

            if (current == config.getStartByte()) {
                state.markStart(previousPosition);
            }

            if (state.previous() == config.getEndByte1() && current == config.getEndByte2()) {
                if (state.isStarted()) {
                    int currentPosition = in.position();
                    int currentLimit = in.limit();
                    LOG.debug("Message ends at position {} with length {}", currentPosition, currentPosition - state.start());
                    in.position(state.start());
                    in.limit(currentPosition);
                    LOG.debug("Set start to position {} and limit to {}", in.position(), in.limit());
                    try {
                        out.write(config.isProduceString()
                                ? parseMessageToString(in, charsetDecoder(session))
                                : parseMessageToByteArray(in));
                        return true;
                    } finally {
                        in.position(currentPosition);
                        in.limit(currentLimit);
                        state.reset();
                    }
                } else {
                    LOG.warn("Ignoring message end at position {} until start byte has been seen.", previousPosition);
                }

            }
            // Remember previous byte in state object because the buffer could
            // be theoretically exhausted right between the two end bytes
            state.markPrevious(current);
        }

        // Could not find a complete message in the buffer.
        // Reset to the initial position (just as nothing had been read yet)
        // and return false so that this method is called again with more data.
        LOG.debug("No complete message yet at position {} ", in.position());
        state.markCurrent(in.position());
        in.position(0);
        return false;
    }

    // Make a defensive byte copy (the buffer will be reused)
    // and omit the start and the two end bytes of the MLLP message
    // returning a byte array
    private Object parseMessageToByteArray(IoBuffer buf) throws CharacterCodingException {
        int len = buf.limit() - 3;
        LOG.debug("Making {} bytes", len);
        byte[] dst = new byte[len];
        buf.skip(1); // skip start byte
        buf.get(dst, 0, len);
        buf.skip(2); // skip end bytes

        // Only do this if conversion is enabled
        if (config.isConvertLFtoCR()) {
            LOG.debug("Replacing LF by CR");
            for (int i = 0; i < dst.length; i++) {
                if (dst[i] == (byte) '\n') {
                    dst[i] = (byte) '\r';
                }
            }
        }
        return dst;
    }

    // Make a defensive byte copy (the buffer will be reused)
    // and omit the start and the two end bytes of the MLLP message
    // returning a String
    private Object parseMessageToString(IoBuffer buf, CharsetDecoder decoder) throws CharacterCodingException {
        int len = buf.limit() - 3;
        LOG.debug("Making string of length {} using charset {}", len, decoder.charset());
        buf.skip(1); // skip start byte
        String message = buf.getString(buf.limit() - 3, decoder);
        buf.skip(2); // skip end bytes

        // Only do this if conversion is enabled
        if (config.isConvertLFtoCR()) {
            LOG.debug("Replacing LF by CR");
            message = message.replace('\n', '\r');
        }
        return message;
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        session.removeAttribute(DECODER_STATE);
        session.removeAttribute(CHARSET_DECODER);
    }

    private CharsetDecoder charsetDecoder(IoSession session) {
        synchronized (session) {
            CharsetDecoder decoder = (CharsetDecoder) session.getAttribute(CHARSET_DECODER);
            if (decoder == null) {
                decoder = config.getCharset().newDecoder();
                session.setAttribute(CHARSET_DECODER, decoder);
            }
            return decoder;
        }
    }

    private DecoderState decoderState(IoSession session) {
        synchronized (session) {
            DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE);
            if (decoderState == null) {
                decoderState = new DecoderState();
                session.setAttribute(DECODER_STATE, decoderState);
            }
            return decoderState;
        }
    }

    /**
     * Holds the state of the decoding process
     */
    private static class DecoderState {
        private int startPos = -1;
        private int currentPos;
        private byte previousByte;

        void reset() {
            startPos = -1;
            currentPos = 0;
            previousByte = 0;
        }

        void markStart(int position) {
            if (isStarted()) {
                LOG.warn("Ignoring message start at position {} before previous message has ended.", position);
            } else {
                startPos = position;
                LOG.debug("Message starts at position {}", startPos);
            }
        }

        void markCurrent(int position) {
            currentPos = position;
        }

        void markPrevious(byte previous) {
            previousByte = previous;
        }

        public int start() {
            return startPos;
        }

        public int current() {
            return currentPos;
        }

        public byte previous() {
            return previousByte;
        }

        public boolean isStarted() {
            return startPos >= 0;
        }
    }
}
