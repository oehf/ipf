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
package org.openehealth.ipf.modules.hl7.parser

import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.validation.ValidationContext
import ca.uhn.hl7v2.HL7Exception

import org.openehealth.ipf.modules.hl7.validation.ValidationContextFactory

import java.lang.reflect.Constructor;

/**
 * PipeParser that also allows for using the CustomModelClassFactory in
 * order to support changes or extensions of the default HL7 model.
 * 
 * @author Christian Ohr
 * @author Marek Václávik
 *
 */
public class PipeParser extends ca.uhn.hl7v2.parser.PipeParser {
	
	ModelClassFactory factory
	
	PipeParser() {
		super()
		factory = new CustomModelClassFactory();
		setValidationContext(ValidationContextFactory.DEFAULT_TYPE_RULES)
	}
	
	PipeParser(ModelClassFactory factory) {
		super()
		this.factory = factory
		setValidationContext(ValidationContextFactory.DEFAULT_TYPE_RULES)
	}

	PipeParser(ValidationContext context) {
	    super()
		factory = new CustomModelClassFactory();
		setValidationContext(context)
	}
	
	PipeParser(ModelClassFactory factory, ValidationContext context) {
		super()
		this.factory = factory
		setValidationContext(context)
	}
	
	// Need to overwrite this method to access our own ModelClassFactory
	protected Message instantiateMessage(String name, String version, boolean isExplicit)
	throws HL7Exception {
		Message result;		
		try {
			Class messageClass = factory.getMessageClass(name, version, isExplicit);
			if (!messageClass)
				throw new ClassNotFoundException("Can't find message class '$name'");
			Constructor constructor = messageClass.getConstructor([ModelClassFactory.class] as Class[] );
			result = (Message) constructor.newInstance([this.factory] as Object[]);
		} catch (Exception e) {
			throw new HL7Exception("Couldn't create Message object of type '$name'",
			HL7Exception.UNSUPPORTED_MESSAGE_TYPE, e);
		}
		result.setValidationContext(getValidationContext());
		result;
	}
	
	
}
