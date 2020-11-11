
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.OptionalInt;

import static java.util.OptionalInt.empty;

/**
 * Simple decoder that extracts the syslog frame as described in RFC 5425.
 * If the actual syslog frame is 476 bytes long, RFC 5425 encodes it with
 * a "476" string, followed by a space, followed by 476 bytes.
 *
 * <pre>
 *     476 BBBBB...BBBBB
 * </pre>
 *
 * @author Christian Ohr
 * @since 4.0
 */
class Rfc5425Decoder extends ByteToMessageDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(Rfc5425Decoder.class);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private OptionalInt frameLength = empty();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (frameLength.isEmpty()) {
            frameLength = getFrameLength(in);
            frameLength.ifPresentOrElse(
                    i -> in.skipBytes(1),   // found frame length, skip space
                    in::resetReaderIndex);  // nothing found
        }
        // Expected frame length detected and buffer contains enough data, so read in the frame
        frameLength.ifPresent(length -> {
            if (in.readableBytes() >= length) {
                frameLength = empty();
                out.add(in.readBytes(length));
            }
        });
    }

    private OptionalInt getFrameLength(ByteBuf in) {
        // Find index of the first space after the length field
        var index = in.indexOf(0, in.readableBytes(), (byte) 32);
        if (index < 0) return empty();

        // Read part until the first space and convert it into a number
        var byteBuf = in.readBytes(index);
        var number = byteBuf.readCharSequence(byteBuf.readableBytes(), StandardCharsets.US_ASCII);
        byteBuf.release();
        try {
            return OptionalInt.of(Integer.parseInt(number.toString()));
        } catch (NumberFormatException ignored) {
            return empty();
        }

    }


}
