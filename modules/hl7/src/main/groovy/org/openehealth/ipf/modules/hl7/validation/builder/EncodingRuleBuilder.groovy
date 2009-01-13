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
package org.openehealth.ipf.modules.hl7.validation.builder

import ca.uhn.hl7v2.validation.MessageRule
import ca.uhn.hl7v2.validation.ValidationContext
import org.openehealth.ipf.modules.hl7.validation.model.ClosureEncodingRule

/**
 * @author Christian Ohr
 */
public class EncodingRuleBuilder extends VersionBuilder{
	
	String encoding
	
	EncodingRuleBuilder(String version, ValidationContext context, String encoding) {
		super(version, context)
		this.encoding = encoding
	}
	
	RuleBuilder checkIf(Closure c) {
		if (!rule) {
			rule = new ClosureEncodingRule(c)
			context.addEncodingRule(version, event, rule)
		} else {
			rule.testClosure = c
		}
	}
	
}
