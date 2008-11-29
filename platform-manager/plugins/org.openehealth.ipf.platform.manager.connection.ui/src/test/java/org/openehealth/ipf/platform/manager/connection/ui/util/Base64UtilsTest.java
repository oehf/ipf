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
package org.openehealth.ipf.platform.manager.connection.ui.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.openehealth.ipf.platform.manager.connection.ui.utils.encoding.Base64Utils;

/**
 * @author Mitko Kolev
 */
public class Base64UtilsTest extends TestCase {

    public void testEncodeDecode() {
        String text = "Hello Java World";
        String encoded = Base64Utils.encode(text);
        String decoded = Base64Utils.decode(encoded);
        assertFalse(encoded.equals(text));
        assertTrue(decoded.equals(text));
    }

    public void testEncodeDecode2() throws UnsupportedEncodingException {
        // String text = "asd;ljkas;ldfj;_;:sodfji23994 asdl;fj 0034&&&*(9";
        // String encoded = Base64Utils.encode(text);
        // String decoded = Base64Utils.decode(new String(encoded.getBytes(),
        // "UTF-16"));
        // assertFalse(text.equals(decoded));
    }
}
