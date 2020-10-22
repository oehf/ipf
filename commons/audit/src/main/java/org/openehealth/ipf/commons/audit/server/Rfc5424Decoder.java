/*
 * Copyright 2020 the original author or authors.
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

import com.github.palindromicity.syslog.NilPolicy;
import com.github.palindromicity.syslog.SyslogParser;
import com.github.palindromicity.syslog.SyslogParserBuilder;
import com.github.palindromicity.syslog.dsl.ParseException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a Syslog String into a Map of elements as described by RFC 5424. It also
 * adds remote host information if available. If the Syslog string could not be parsed,
 * the raw data together with the exception is preserved.
 *
 * @author Christian Ohr
 * @since 4.0
 */
class Rfc5424Decoder extends MessageToMessageDecoder<ByteBuf> {

    private static final SyslogParser syslogParser = new SyslogParserBuilder()
            .withNilPolicy(NilPolicy.OMIT)
            .build();

    public static final String SYSLOG_RAW_MESSAGE = "syslog.raw.message";
    public static final String SYSLOG_EXCEPTION = "syslog.exception";
    public static final String SYSLOG_REMOTE_HOST = "syslog.remote.host";
    public static final String SYSLOG_REMOTE_PORT = "syslog.remote.port";
    public static final String SYSLOG_REMOTE_IP = "syslog.remote.ip";

    static Map<String, Object> parseByteBuf(ByteBuf byteBuf) {
        var bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        var message = new String(bytes, StandardCharsets.UTF_8);
        try {
            // Get rid of the pesky BOM character
            return syslogParser.parseLine(message.replace("\uFEFF", ""));
        } catch (ParseException e) {
            Map<String, Object> map = new HashMap<>();
            map.put(SYSLOG_RAW_MESSAGE, bytes);
            map.put(SYSLOG_EXCEPTION, e);
            return map;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        Map<String, Object> map = parseByteBuf(msg);
        SocketAddress address = ctx.channel().remoteAddress();
        if (address instanceof InetSocketAddress) {
            Map<String, Object> enriched = new HashMap<>(map);
            var inetSocketAddress = (InetSocketAddress) address;
            enriched.put(SYSLOG_REMOTE_HOST, inetSocketAddress.getHostName());
            enriched.put(SYSLOG_REMOTE_PORT, inetSocketAddress.getPort());
            enriched.put(SYSLOG_REMOTE_IP, inetSocketAddress.getAddress().getHostAddress());
            out.add(Collections.unmodifiableMap(enriched));
        } else {
            out.add(map);
        }
    }
}
