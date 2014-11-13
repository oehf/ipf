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
package org.openehealth.ipf.modules.hl7dsl

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.parser.Parser

/**
 * @author Martin Krasser
 * @deprecated the ipd-modules-hl7dsl module is deprecated
 */
public class MessageAdapters {
	
	static HapiContext defaultContext() {
		return new DefaultHapiContext()
	}
	
	// -----------------------------------------------------------------
	//  Factory methods using default parser
	// -----------------------------------------------------------------
	
	static <T extends AbstractMessage>  MessageAdapter<T> load(String resource) {
		make(MessageAdapter.class.classLoader.getResource(resource)?.text)
	}
	
	static <T extends AbstractMessage>  MessageAdapter<T>  load(String resource, String charset) {
		make(MessageAdapter.class.classLoader.getResource(resource)?.getText(charset))
	}
	
	static <T extends AbstractMessage>  MessageAdapter<T>  make(InputStream stream) {
		return make(stream.text)
	}
	
	static <T extends AbstractMessage>  MessageAdapter<T>  make(InputStream stream, String charset) {
		return make(stream.getText(charset))
	}
	
	static <T extends AbstractMessage>  MessageAdapter<T>  make(String message) {
		make(defaultContext(), message)
	}
	
	// -----------------------------------------------------------------
	//  Factory methods using custom parser
	// -----------------------------------------------------------------

    static <T extends AbstractMessage>  MessageAdapter<T>  load(HapiContext context, String resource) {
        make(context, MessageAdapter.class.classLoader.getResource(resource)?.text)
    }

	static <T extends AbstractMessage>  MessageAdapter<T>  load(Parser parser, String resource) {
		make(parser, MessageAdapter.class.classLoader.getResource(resource)?.text)
	}

    static <T extends AbstractMessage>  MessageAdapter<T>  load(HapiContext context, String resource, String charset) {
        make(context, MessageAdapter.class.classLoader.getResource(resource)?.getText(charset))
    }

	static <T extends AbstractMessage>  MessageAdapter<T>  load(Parser parser, String resource, String charset) {
		make(parser, MessageAdapter.class.classLoader.getResource(resource)?.getText(charset))
	}

    static <T extends AbstractMessage>  MessageAdapter<T>  make(HapiContext context, InputStream stream) {
        return make(context, stream.text)
    }

	static <T extends AbstractMessage>  MessageAdapter<T>  make(Parser parser, InputStream stream) {
		return make(parser, stream.text)
	}

    static <T extends AbstractMessage>  MessageAdapter<T>  make(HapiContext context, InputStream stream, String charset) {
        return make(context, stream.getText(charset))
    }

	static <T extends AbstractMessage>  MessageAdapter<T>  make(Parser parser, InputStream stream, String charset) {
		return make(parser, stream.getText(charset))
	}

    static <T extends AbstractMessage>  MessageAdapter<T>  make(HapiContext context, String message) {
        make(context.genericParser, message);
    }

	static <T extends AbstractMessage>  MessageAdapter<T>  make(Parser parser, String message) {
		if (!message) {
			return null
		}
		new MessageAdapter(parser, parser.parse(message))
	}


}
