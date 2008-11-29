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
package org.openehealth.ipf.platform.manager.connection.ui.utils.encoding;

import org.apache.commons.codec.binary.Base64;

/**
 * Encodes and decodes 64 strings with the default UTF-8 encoding.
 * 
 * @author Mitko Kolev
 */
public class Base64Utils {

    public Base64Utils() {
    }

    /**
     * Creates a new instance of the class
     * 
     * @param textByteEncoding
     * @return
     */
    public static Base64Utils createInstance() {
        return new Base64Utils();
    }

    /**
     * Encodes the given text in a base-64 byte array.
     * 
     * @param text
     * @return the encoded String
     */
    public static String encode(String text) {
        byte[] encoded = Base64.encodeBase64(text.getBytes());
        return new String(encoded);
    }

    /**
     * @param encodedText
     * @return
     */
    public static String decode(String encodedText) {
        byte[] bytes = Base64.decodeBase64(encodedText.getBytes());
        return new String(bytes);
    }
}
