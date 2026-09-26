
/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.TooLongFrameException;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Simple decoder that extracts the syslog frame as described in RFC 5425.
 * If the actual syslog frame is 476 bytes long, RFC 5425 encodes it with
 * a "476" string, followed by a space, followed by 476 bytes.
 *
 * <pre>
 *     476 BBBBB...BBBBB
 * </pre>
 *
 * Frames longer than the maximum frame length are rejected, so that a client cannot make the server
 * buffer arbitrary amounts of data. A malformed or too long frame header cannot be recovered from, as
 * the start of the next frame is unknown, so the connection is closed.
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class Rfc5425Decoder extends ByteToMessageDecoder {

    /**
     * Default maximum frame length. RFC 5425 requires at least 2048 octets to be supported, but audit
     * messages of large transactions may be considerably longer.
     */
    public static final int DEFAULT_MAX_FRAME_LENGTH = 1024 * 1024;

    private final int maxFrameLength;
    private final int maxLengthDigits;

    // length of the frame whose header has been read, or -1 while waiting for the next header
    private int frameLength = -1;

    public Rfc5425Decoder() {
        this(DEFAULT_MAX_FRAME_LENGTH);
    }

    /**
     * @param maxFrameLength maximum length of a syslog frame, in bytes
     * @since 6.0
     */
    public Rfc5425Decoder(int maxFrameLength) {
        if (maxFrameLength <= 0) {
            throw new IllegalArgumentException("maxFrameLength must be positive, but was " + maxFrameLength);
        }
        this.maxFrameLength = maxFrameLength;
        this.maxLengthDigits = String.valueOf(maxFrameLength).length();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (frameLength < 0) {
            var start = in.readerIndex();
            // The header is MSG-LEN SP, i.e. at most maxLengthDigits digits followed by a space
            var space = in.indexOf(start, Math.min(in.writerIndex(), start + maxLengthDigits + 1), (byte) ' ');
            if (space < 0) {
                if (in.readableBytes() > maxLengthDigits) {
                    fail(ctx, in, new TooLongFrameException("Syslog frame header exceeds " + maxLengthDigits + " digits"));
                }
                return; // wait for the rest of the header
            }
            var length = parseFrameLength(in, start, space - start);
            if (length < 0) {
                fail(ctx, in, new CorruptedFrameException("Invalid syslog frame length: "
                        + in.toString(start, space - start, StandardCharsets.US_ASCII)));
                return;
            }
            if (length > maxFrameLength) {
                fail(ctx, in, new TooLongFrameException("Syslog frame length " + length + " exceeds " + maxFrameLength));
                return;
            }
            in.readerIndex(space + 1);
            frameLength = (int) length;
        }
        if (in.readableBytes() >= frameLength) {
            out.add(in.readRetainedSlice(frameLength));
            frameLength = -1;
        }
    }

    /**
     * Parses MSG-LEN = NONZERO-DIGIT *DIGIT
     *
     * @return the frame length, or -1 if the header is not a valid MSG-LEN
     */
    private static long parseFrameLength(ByteBuf in, int start, int digits) {
        if (digits == 0 || in.getByte(start) == '0') {
            return -1;
        }
        long length = 0;
        for (var i = start; i < start + digits; i++) {
            var b = in.getByte(i);
            if (b < '0' || b > '9') {
                return -1;
            }
            length = length * 10 + (b - '0');
        }
        return length;
    }

    private void fail(ChannelHandlerContext ctx, ByteBuf in, RuntimeException e) {
        // the next frame cannot be found anymore, so discard what is there and close the connection
        in.skipBytes(in.readableBytes());
        frameLength = -1;
        ctx.close();
        throw e;
    }
}
