/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.mina.mllp;

import static org.apache.commons.lang.Validate.notNull;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;

/**
 * Encapsulation of the delimiters of an MLLP message within a {@link ByteBuffer}.
 * <p>
 * This class can either represent a complete MLLP message or part of the message.
 * In general the complete MLLP message is contained within one or more 
 * {@link ByteBuffer}s. This class always refers to a single {@link ByteBuffer}.
 * <p>
 * {@code MllpMessage} is used when decoding via {@link MllpDecoder}. The
 * class stores state in the {@link IoSession} of the decoder
 * 
 * @author Jens Riemschneider
 */
public class MllpMessagePart {
    private int start;
    private int end;
    private boolean completed;
    private final ByteBuffer buffer;

    /**
     * Extracts a message from the given buffer.
     * <p>
     * This method will use the given buffer and calculate the start and end of an
     * MLLP message. If the buffer only contains part of the message (i.e. no end
     * of message was found) it will report that it needs more data via {@link #isComplete()}.
     * @param state
     *          the state object containing information about the overall extraction of
     *          MLLP messages.
     * @param buffer
     *          the buffer containing (part of) the MLLP message
     * @return the created message
     */
    public static MllpMessagePart extractMessage(MllpMessageExtractionState state, ByteBuffer buffer) {
        MllpMessagePart message = new MllpMessagePart(buffer);
        
        message.checkStart(state);
        message.checkEnd(state, buffer);
        message.checkTermination(state, buffer);
        
        return message;
    }

    /**
     * @return {@code true} if the message was completely read until the end of message 
     *          character (CR)
     */
    public boolean isComplete() { 
        return completed; 
    }
    
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[] {};

    /**
     * @return the data of the message part as a byte array. 
     */
    public byte[] asByteArray() {
        if (end <= start) {
            return EMPTY_BYTE_ARRAY;
        }
        
        int curPosition = buffer.position();
        byte[] byteArray = new byte[end - start + 1];
        buffer.position(start);
        buffer.get(byteArray);
        buffer.position(curPosition);
        return byteArray;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "{%1$s: start=%2$s, end=%3$s, completed=%4$s, buffer=%5$s}",
                getClass().getSimpleName(), start, end, completed, buffer);
    }

    private MllpMessagePart(ByteBuffer buffer) {
        notNull(buffer, "buffer cannot be null");
        
        this.buffer = buffer;
        start = buffer.position();
        end = buffer.position() - 1;
    }
        
    private void checkTermination(MllpMessageExtractionState state, ByteBuffer buffer) {
        if (state.messageWasEnded() && buffer.hasRemaining()) {
            byte data = getData();
            if (data == MllpStoreCodec.LAST_CHARACTER) {
                completed = true;
                state.reset();
            }
            else {
                throw new IllegalStateException("Message is not MLLP compliant. End of message must be followed by CR. Was = " + data);
            }
        }
    }

    private void checkEnd(MllpMessageExtractionState state, ByteBuffer buffer) {
        while (buffer.hasRemaining() && !state.messageWasEnded()) {
            if (getData() == MllpStoreCodec.END_MESSAGE) {
                state.messageEnded();
            }
            else {
                end = buffer.position() - 1;
            }
        }
    }

    private void checkStart(MllpMessageExtractionState state) {
        if (state.messageWasStarted()) {
            return;
        }
        
        byte data = getData();
        if (data != MllpStoreCodec.START_MESSAGE) {
            throw new IllegalStateException("Message is not MLLP compliant. Start of message must be first. Was = " + data);
        }
        state.messageStarted();
        start = buffer.position();
    }
    
    private byte getData() {
        return buffer.get();
    }

    /**
     * State container for message extraction.
     * <p>
     * Contains flags that are needed between multiple calls to {@link MllpMessagePart#extractMessage}.
     */
    public static final class MllpMessageExtractionState {
        private boolean started;
        private boolean ended;

        public boolean messageWasStarted() {
            return started;
        }

        public void reset() {
            started = false;
            ended = false;
        }

        public void messageEnded() {            
            ended = true;
        }

        public boolean messageWasEnded() {
            return ended;
        }

        public void messageStarted() {
            started = true;
        }
    }
}