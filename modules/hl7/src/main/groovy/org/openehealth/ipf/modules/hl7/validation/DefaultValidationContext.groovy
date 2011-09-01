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
package org.openehealth.ipf.modules.hl7.validation

import org.openehealth.ipf.modules.hl7.validation.builder.RuleBuilder
import org.openehealth.ipf.modules.hl7.validation.model.CompositeTypeRule
import org.openehealth.ipf.modules.hl7.validation.model.MissingMessageRule
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.validation.EncodingRule
import ca.uhn.hl7v2.validation.MessageRule
import ca.uhn.hl7v2.validation.PrimitiveTypeRule
import ca.uhn.hl7v2.validation.Rule
import ca.uhn.hl7v2.validation.ValidationContext

/**
 * IPF implementation of a HAPI validation context. It's strongly recommended
 * to use the {@link RuleBuilder} to assemble the rules that are checked during
 * validation by calling {@link DefaultValidationContext#configure()}
 * A ValidationContext can either be injected into a HAPI Parser instance (is this case the
 * message is validated during parsing) or be used in seperate validation steps, e.g.
 * using HAPI's MessageValidator. 
 * <p>
 * Existing DefaultValidationContext instances can be extended by simply adding more rules. If several
 * rules match the input data, all rules are tested in the order of their insertion. Example:
 * <pre>
 * ValidationContext context = ValidationContextFactory.DEFAULT_TYPE_RULES
 * context.configure()
 *    .forVersion(...)
 *    ...
 * </pre>
 * You can also combine an existing ValidationContext with additional validation rules:
 * <pre>
 * ValidationContext existingContext = ....
 * ValidationContext context = new DefaultValidationContext()
 * context.addContext(existingContext)
 *    .configure()
 *    	.forVersion(...)
 *      ...
 * </pre>
 * 
 * @author Christian Ohr
 */
public class DefaultValidationContext implements ValidationContext, Serializable {
	    
	private Map ruleMap = MessageUtils.HL7V2_VERSIONS.collectEntries { version -> [version, [:]] }

	private def nestedValidationContexts = []
	boolean requireMessageRule = false
	
	public DefaultValidationContext() {	    
	}
	
	/**
	 * @see ca.uhn.hl7v2.validation.ValidationContext#getPrimitiveRules(java.lang.String, java.lang.String, ca.uhn.hl7v2.model.Primitive)
	 */
	public PrimitiveTypeRule[] getPrimitiveRules(String version, String typeName, Primitive type){
	    def rules = []
		def matchingRules = ruleMap[version]["_P_$typeName"]
	    if (matchingRules) {
	        rules.addAll(matchingRules)
	    }
		for (ValidationContext context : nestedValidationContexts) {
		    matchingRules = context.getPrimitiveRules(version, typeName, type).toList()
		    if (matchingRules) {
		        rules.addAll(matchingRules)
		    }
		}
		rules as PrimitiveTypeRule[]
	}
	
	/**
	 * @see ca.uhn.hl7v2.validation.ValidationContext#getMessageRules(java.lang.String, java.lang.String, java.lang.String)
	 */
	public MessageRule[] getMessageRules(String version, String messageType, String triggerEvent){
	    def rules = []
		def matchingRules = ruleMap[version]["_M_$messageType^$triggerEvent"]
	    if (matchingRules) {
	        rules.addAll(matchingRules)
	    }
	    matchingRules = ruleMap[version]["_M_$messageType^*"]
	    if (matchingRules) {
	        rules.addAll(matchingRules)
	    }
		for (ValidationContext context : nestedValidationContexts) {
		    matchingRules = context.getMessageRules(version, messageType, triggerEvent).toList()
		    if (matchingRules) {
		        rules.addAll(matchingRules)
		    }
		}
		// If no message rule matched but we require one, add the MissingMessageRule
		if (rules.size() == 0 && requireMessageRule) {
			rules.add(new MissingMessageRule())
		}
		rules as MessageRule[]
	}
    
   public <T extends Composite> CompositeTypeRule<T> [] getCompositeTypeRules(String version, String messageType, String triggerEvent, Class<T> clazz){
       def rules = []
       def matchingRules = ruleMap[version]["_C_$messageType^$triggerEvent"]
       if (matchingRules) {
           rules.addAll(matchingRules)
       }
       matchingRules = ruleMap[version]["_C_$messageType^*"]
       if (matchingRules) {
           rules.addAll(matchingRules)
       }
       for (ValidationContext context : nestedValidationContexts) {
           if (context instanceof DefaultValidationContext){
               matchingRules = ((DefaultValidationContext)context).getCompositeTypeRules(version, messageType, triggerEvent, clazz).toList()
               if (matchingRules) {
                   rules.addAll(matchingRules)
               }
           }
       }
       //filter only the rules that apply for clazz
       def result = [];
       for(rule in rules){
           if(rule.appliesFor(clazz)){
               result.add(rule)
           }
       }
       result as CompositeTypeRule<T>[]
   }
   
   
	/**
	 * @see ca.uhn.hl7v2.validation.ValidationContext#getEncodingRules(java.lang.String, java.lang.String)
	 */
	public EncodingRule[] getEncodingRules(String version, String encoding){
	    def rules = []
		def matchingRules = ruleMap[version]["_E_$encoding"]
	    if (matchingRules) {
	        rules.addAll(matchingRules)
	    }
		for (ValidationContext context : nestedValidationContexts) {
		    matchingRules = context.getEncodingRules(version, encoding).toList()
		    if (matchingRules) {
		        rules.addAll(matchingRules)
		    }
		}
		rules as EncodingRule[]
	}
	
	/**
	 * Adds a rule validating a Primitive to the registry
	 */
	public void addPrimitiveRule(String version, String typeName, PrimitiveTypeRule rule) {
		addToRules(version, "_P_$typeName", rule)
	}
    
    /**
    * Adds a rule validating a Composite to the registry
    */
    public void addCompositeTypeRule(String version, String messageType, String triggerEvent, CompositeTypeRule rule) {
        addToRules(version, "_C_$messageType^$triggerEvent", rule)
    }

    /**
	 * Adds a rule validating a Message to the registry
	 */
	public void addMessageRule(String version, String messageType, String triggerEvent, MessageRule rule) {
		addToRules(version, "_M_$messageType^$triggerEvent", rule)
	}
	
	/**
	 * Adds a rule validating a encoded message to the registry
	 */
	public void addEncodingRule(String version, String encoding, EncodingRule rule) {
		addToRules(version, "_E_$encoding", rule)
	}
	
	/**
	 * Launches the RuleBuilder in order to add validation rules
	 */
	public RuleBuilder configure() {
		new RuleBuilder(this)
	}
	
	/**
	 * Adds an existing ValidationContext
	 */	
	public DefaultValidationContext addContext(ValidationContext context) {
	    if (context) {
	        nestedValidationContexts.add(context)
	    }
	    this
	}
	
	protected void addToRules(String version, String scope, Rule rule) {
		version.tokenize().each { v -> addRule(v, scope, rule) }
	}
	
	protected void addRule(String version, String scope, Object rule) {
		if (ruleMap[version].containsKey(scope)) {
			ruleMap[version][scope].add(rule)
		} else {
			ruleMap[version].put(scope, [rule])
		}
	}
}
