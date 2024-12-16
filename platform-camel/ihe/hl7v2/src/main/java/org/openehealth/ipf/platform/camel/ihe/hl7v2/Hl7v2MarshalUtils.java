/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.preparser.PreParser;
import org.apache.camel.Message;
import org.apache.camel.WrappedFile;
import org.apache.camel.component.hl7.HL7Charset;
import org.apache.camel.converter.IOConverter;
import org.apache.commons.lang3.ClassUtils;

import java.io.File;
import java.io.InputStream;


/**
 * Various helper methods for data transformation.
 *
 * @author Dmytro Rud
 */
public class Hl7v2MarshalUtils {

    private Hl7v2MarshalUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }



    private static Exception adaptingException(Object body) {
        throw new Hl7v2AdaptingException("Cannot create HL7v2 message from " + ClassUtils.getSimpleName(body, "<null>"));
    }


    private static String readFile(Object file, String charset) throws Exception {
        var bytes = IOConverter.toByteArray((File) file);
        return new String(bytes, charset).replace('\n', '\r');
    }


    /**
     * Determines whether the given object belongs to the predefined
     * set of supported data types.
     *
     * @param body The object to check.
     * @return <code>true</code> when the type of the object is supported
     * by the HL7v2 adapter out-of-the-box, <code>false</code> otherwise.
     */
    public static boolean typeSupported(Object body) {
        final var knownTypes = new Class<?>[]{
                String.class,
                ca.uhn.hl7v2.model.Message.class,
                File.class,
                InputStream.class,
                java.nio.ByteBuffer.class,
                byte[].class,
                WrappedFile.class
        };
        if (body != null) {
            for (var type : knownTypes) {
                try {
                    type.cast(body);
                    return true;
                } catch (ClassCastException cce) {
                    // nop
                }
            }
        }
        return false;
    }

    /**
     * Converts message contents to a {@link String} using the given character set (statically or in MSH-18)
     * and replaces all <tt>'\n'</tt>'s with <tt>'\r'</tt>'s.
     * <p>
     * If requested, segments will be defragmented as well.
     *
     * @param message     Camel message
     * @param charsetName charset name
     * @return string
     */
    public static String convertBodyToString(
            Message message,
            String charsetName,
            boolean defragmentSegments) throws Exception {
        var body = message.getBody();
        if (!typeSupported(body)) {
            throw adaptingException(body);
        }
        String s;
        if (body instanceof String string) {
            s = string;
        } else if (body instanceof ca.uhn.hl7v2.model.Message m) {
            s = m.encode();
        } else if (body instanceof byte[] bytes) {
            s = toString(bytes, charsetName);
        } else if (body instanceof InputStream inputStream) {
            var bytes = IOConverter.toBytes(inputStream);
            s = toString(bytes, charsetName);
        } else if (body instanceof File) {
            s = readFile(body, charsetName);
        } else if (body instanceof WrappedFile<?> wrappedFile) {
            var file = wrappedFile.getFile();
            s = readFile(file, charsetName);
        } else {
            var bytes = message.getBody(byte[].class);
            if (bytes != null) {
                s = toString(bytes, charsetName);
            } else {
                throw adaptingException(body);
            }
        }
        s = s.replace('\n', '\r');
        if (defragmentSegments) {
            s = s.replace("\rADD" + s.charAt(3), "");
        }
        return s;
    }

    /**
     * Converts Camel message to a Byte Array using the given character set (statically or in MSH-18).
     * This is necessary before a HL7 message object or string is remotely transferred to have full
     * control over the encoding.
     *
     * @param message     Camel message
     * @param charsetName charset name
     * @return byte array
     * @throws Exception
     */
    public static byte[] convertMessageToByteArray(
            Message message,
            String charsetName) throws Exception {
        try {
            return convertBodyToByteArray(message.getBody(), charsetName);
        } catch (Hl7v2AdaptingException e) {
            var stream = message.getBody(InputStream.class);
            return IOConverter.toBytes(stream);
        }
    }

    /**
     * Converts HL7 object to a Byte Array using the given character set (statically or in MSH-18).
     * This is necessary before a HL7 message object or string is remotely transferred to have full
     * control over the encoding.
     *
     * @param body HL7 object
     * @param charsetName charset name
     * @return byte array
     */
    public static byte[] convertBodyToByteArray(
            Object body,
            String charsetName) throws Exception {
        if (body instanceof String string) {
            return toByteArray(string, charsetName);
        } else if (body instanceof byte[] bytes) {
            return bytes;
        } else if (body instanceof InputStream inputStream) {
            return IOConverter.toBytes(inputStream);
        } else if (body instanceof ca.uhn.hl7v2.model.Message message) {
            return toByteArray(message.encode(), charsetName);
        } else {
            throw adaptingException(body);
        }
    }

    /**
     * Converts the contents of the given Camel message to a {@link Message}. This is necessary
     * when either a transferred HL7 message is unmarshalled or we need to access message details for
     * further processing in interceptors.
     *
     * @param message            Camel message to be converted.
     * @param defaultCharsetName default character set.
     * @param parser             HL7 parser.
     * @return a {@link Message} or <code>null</code> when it was impossible to get or create one.
     * @throws Exception
     */
    public static ca.uhn.hl7v2.model.Message convertBodyToMessage(
            Message message,
            String defaultCharsetName,
            Parser parser) throws Exception {
        var body = message.getBody();
        ca.uhn.hl7v2.model.Message msg = null;
        if (body instanceof ca.uhn.hl7v2.model.Message m) {
            msg = m;
        } else {
            // process all other types (String, File, InputStream, ByteBuffer, byte[])
            // by means of the standard routine.  An exception here will be o.k.
            var s = convertBodyToString(message, defaultCharsetName, false);
            if (s != null) {
                s = s.replace('\n', '\r');
                msg = parser.parse(s);
            }
        }
        return msg;
    }

    // Guess charset from configuration. If the message contains something different in MSH-18, it will be
    // again converted using this charset.
    private static String toString(byte[] bytes, String defaultCharsetName) throws Exception {
        var guessed = new String(bytes, defaultCharsetName);
        var msh18 = PreParser.getFields(guessed, "MSH-18")[0];
        var hl7Charset = HL7Charset.getHL7Charset(msh18);
        var hl7CharsetName = hl7Charset != null ? hl7Charset.getJavaCharsetName() : defaultCharsetName;
        return hl7CharsetName.equals(defaultCharsetName) ?
                guessed :
                new String(bytes, hl7CharsetName);
    }

    private static byte[] toByteArray(String s, String defaultCharsetName) throws Exception {
        var msh18 = PreParser.getFields(s, "MSH-18")[0];
        var hl7Charset = HL7Charset.getHL7Charset(msh18);
        var hl7CharsetName = hl7Charset != null ? hl7Charset.getJavaCharsetName() : defaultCharsetName;
        return s.getBytes(hl7CharsetName);
    }




}
