/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.dsl

import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.GenericParser
import ca.uhn.hl7v2.parser.Parser

/**
 *
 */
class TestUtils {

    // -----------------------------------------------------------------
    //  Factory methods using default parser
    // -----------------------------------------------------------------

    static Message load(String resource) {
        make(TestUtils.class.classLoader.getResource(resource)?.text)
    }

    static Message load(String resource, String charset) {
        make(TestUtils.class.classLoader.getResource(resource)?.getText(charset))
    }

    static Message make(InputStream stream) {
        return make(stream.text)
    }

    static Message make(InputStream stream, String charset) {
        return make(stream.getText(charset))
    }

    static Message make(String message) {
        make(new GenericParser(), message)
    }

    // -----------------------------------------------------------------
    //  Factory methods using custom parser
    // -----------------------------------------------------------------

    static Message load(Parser parser, String resource) {
        make(parser, TestUtils.class.classLoader.getResource(resource)?.text)
    }

    static Message load(Parser parser, String resource, String charset) {
        make(parser, TestUtils.class.classLoader.getResource(resource)?.getText(charset))
    }

    static Message make(Parser parser, InputStream stream) {
        return make(parser, stream.text)
    }

    static Message make(Parser parser, InputStream stream, String charset) {
        return make(parser, stream.getText(charset))
    }

    static Message make(Parser parser, String message) {
        if (!message) {
            return null
        }
        parser.parse(message)
    }
}
