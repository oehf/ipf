/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.audit.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Framing of RFC 5425 syslog messages
 */
public class Rfc5425DecoderTest {

    @Test
    public void testFramesWithLengthFieldsOfDifferentSizeInOneRead() {
        var messages = List.of("abcde", "0123456789ab", "x".repeat(999), "y".repeat(1000), "z".repeat(99), "q");
        var channel = new EmbeddedChannel(new Rfc5425Decoder());
        channel.writeInbound(buffer(String.join("", messages.stream().map(Rfc5425DecoderTest::frame).toList())));
        assertEquals(messages, readFrames(channel));
    }

    @Test
    public void testFramesSplitAcrossReads() {
        var messages = List.of("abcde", "0123456789ab", "x".repeat(1000));
        var data = String.join("", messages.stream().map(Rfc5425DecoderTest::frame).toList());
        var channel = new EmbeddedChannel(new Rfc5425Decoder());
        // one byte at a time
        for (var i = 0; i < data.length(); i++) {
            channel.writeInbound(buffer(data.substring(i, i + 1)));
        }
        assertEquals(messages, readFrames(channel));
    }

    @Test
    public void testFrameExceedingMaxLength() {
        var channel = new EmbeddedChannel(new Rfc5425Decoder(100));
        assertThrows(TooLongFrameException.class, () -> channel.writeInbound(buffer(frame("x".repeat(101)))));
        assertFalse(channel.isOpen());
    }

    @Test
    public void testFrameHeaderExceedingMaxLength() {
        var channel = new EmbeddedChannel(new Rfc5425Decoder(100));
        assertThrows(TooLongFrameException.class, () -> channel.writeInbound(buffer("12345")));
        assertFalse(channel.isOpen());
    }

    @Test
    public void testMalformedFrameLength() {
        for (var header : List.of("abc ", "0 ", "012 ", " ", "-1 ")) {
            var channel = new EmbeddedChannel(new Rfc5425Decoder());
            assertThrows(CorruptedFrameException.class, () -> channel.writeInbound(buffer(header + "xyz")), header);
            assertFalse(channel.isOpen(), header);
        }
    }

    private static String frame(String message) {
        return message.length() + " " + message;
    }

    private static ByteBuf buffer(String s) {
        return Unpooled.copiedBuffer(s, StandardCharsets.US_ASCII);
    }

    private static List<String> readFrames(EmbeddedChannel channel) {
        var frames = new ArrayList<String>();
        ByteBuf frame;
        while ((frame = channel.readInbound()) != null) {
            frames.add(frame.toString(StandardCharsets.US_ASCII));
            frame.release();
        }
        return frames;
    }
}
