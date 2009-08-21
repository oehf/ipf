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
import ca.uhn.hl7v2.validation.impl.ConformanceProfileRule
import ca.uhn.hl7v2.validation.ValidationContext
import org.openehealth.ipf.modules.hl7.validation.model.ClosureMessageRule
import ca.uhn.hl7v2.validation.Rule
/**
 * @author Christian Ohr
 */
public class MessageRuleBuilder extends VersionBuilder{
	
	String messageType
	def triggerEvent
	
	MessageRuleBuilder() {	    
	}
	
	MessageRuleBuilder(String version, ValidationContext context, String messageType, triggerEvent) {
		super(version, context)
		this.messageType = messageType
		this.triggerEvent = triggerEvent
	}
	
	RuleBuilder checkIf(Closure c) {
		if (!rule) {
			rule = new ClosureMessageRule(c)
			addMessageRule(rule)
		} else {
			rule.testClosure = c
		}
		this
	}
	
	/**
	 * Adds an existing HAPI {@link ConformanceProfileRule} to the set of rules.
	 *
	 * @param profileID the profile ID or null, if the ID shall
	 * be taken from MSH-22.
	 */
	RuleBuilder conformsToProfile(String profileID) {
	     addMessageRule(new ConformanceProfileRule(profileID))
	     this
	}
	 
	/**
	 * Adds an check for a HL7 Abstract Syntax of a message. For details of the format,
	 * see {@link AbstractSyntaxRuleBuilder}.
	 */
	RuleBuilder abstractSyntax(Object... args) {
	    new AbstractSyntaxRuleBuilder(version, context, messageType, triggerEvent, args)
	}
	 
	protected void addMessageRule(Rule rule) {
	    if (triggerEvent instanceof Collection) {
	        triggerEvent.each {
	            context.addMessageRule(version, messageType, it, rule)
	        }
	    } else {
	        triggerEvent.tokenize().each {
	            context.addMessageRule(version, messageType, it, rule)
	        }
	    }
	}
	
}
