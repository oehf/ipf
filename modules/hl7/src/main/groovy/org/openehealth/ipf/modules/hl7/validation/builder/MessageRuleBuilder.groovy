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

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7.validation.model.ClosureMessageRule
import org.openehealth.ipf.modules.hl7.validation.model.CompositeTypeRule

import ca.uhn.hl7v2.validation.Rule
import ca.uhn.hl7v2.validation.impl.ConformanceProfileRule

/**
 * @author Christian Ohr
 */
public class MessageRuleBuilder extends VersionBuilder{
	
	String messageType
	def triggerEvent
	
	MessageRuleBuilder() {
        super(null)
	}
	
	MessageRuleBuilder(String version, DefaultValidationContext context, String messageType, triggerEvent) {
		super(version, context)
		this.messageType = messageType
		this.triggerEvent = triggerEvent
	}
    
	MessageRuleBuilder checkIf(Closure c) {
        if (!rule) {
            rule = new ClosureMessageRule(c)
            addMessageRule(rule)
        } else {
            rule.testClosure = c
        }
        this
	}

    PrimitiveRuleBuilder checkPrimitive(String name){
        return new PrimitiveRuleBuilder(version, context, name)
    }
    
    MessageRuleBuilder checkCompositesWith(CompositeTypeRule typeRule){
        context.addCompositeTypeRule(version, messageType, triggerEvent, typeRule)
        return this
    }
    
          
	/**
	 * Adds an existing HAPI {@link ConformanceProfileRule} to the set of rules.
	 *
	 * @param profileID the profile ID or null, if the ID shall
	 * be taken from MSH-22.
	 */
	MessageRuleBuilder conformsToProfile(String profileID) {
	     addMessageRule(new ConformanceProfileRule(profileID))
	     this
	}
	 
	/**
	 * Adds an check for a HL7 Abstract Syntax of a message. For details of the format,
	 * see {@link AbstractSyntaxRuleBuilder}.
	 */
	AbstractSyntaxRuleBuilder abstractSyntax(Object... args) {
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
