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
import org.openehealth.ipf.modules.hl7.validation.model.ClosurePrimitiveTypeRule

import ca.uhn.hl7v2.validation.ValidationContext

/**
 * @author Christian Ohr
 */
public class PrimitiveRuleBuilder extends VersionBuilder{
	
	String typeName
	
	
	PrimitiveRuleBuilder(String version, DefaultValidationContext context, String typeName) {
		super(version, context)
		this.typeName = typeName
	}
	
	/**
	 * Adds a Primitive validation rule using the given Closure
	 */
	RuleBuilder checkIf(Closure c) {
		if (!rule) {
			rule = new ClosurePrimitiveTypeRule(c)
			context.addPrimitiveRule(version, typeName, rule)
		} else {
			rule.testClosure = c
		}
		this
	}
	
	/*
	 * Causes that primitive values are with leading whitespaces removed
	 * before validation is executed. If no other validation rule is added, the
	 * validation always passes.
	 */
	RuleBuilder omitLeadingWhitespace() {
		if (!rule) {
			rule = new ClosurePrimitiveTypeRule(ClosurePrimitiveTypeRule.PASS)
			context.addPrimitiveRule(version, typeName, rule)
		}
		rule.omitLeadingWhitespace = true
		this
	}
	
	/*
	 * Causes that primitive values are with leading whitespaces removed
	 * before validation is executed. If no other validation rule is added, the
	 * validation always passes.
	 */
	RuleBuilder omitTrailingWhitespace() {
		if (!rule) {
			rule = new ClosurePrimitiveTypeRule(ClosurePrimitiveTypeRule.PASS)
			context.addPrimitiveRule(version, typeName, rule)
		}
		rule.omitTrailingWhitespace = true
		this
	}
	
	/*
	 * Adds a rule that restricts the size of a Primitive value. Equivalent with
	 * <pre>
	 * checkIf { it.size() <= max }
	 * </pre>
	 */
	RuleBuilder maxSize(int max) {
		checkIf { it.size() <= max }
	}
	
	
	/*
	 * Adds a rule that restricts the size of a Primitive value. Equivalent with
	 * <pre>
	 * checkIf { it.size() >= range.from && it.size() <= range.to }
	 * </pre>
	 */	
	RuleBuilder getAt(IntRange range) {
		checkIf { it.size() >= range.from && it.size() <= range.to }
	}
	
	/*
	 * Adds a rule that checks a non-null Primitive value. Equivalent with
	 * <pre>
	 * checkIf { it != null && it.size() > 0 }
	 * </pre>
	 */
	RuleBuilder notEmpty() {
		checkIf { it != null && it.size() > 0 }
	}
	
	/*
	 * Adds a rule that restricts Primitive value to a regular expression match. Equivalent with
	 * <pre>
	 * checkIf { it ==~ regex }
	 * </pre>
	 */
	RuleBuilder matches(def regex) {
		checkIf { it ==~ regex }
	}
	
	/*
	 * Adds a rule that restricts Primitive value to be a decimal number
	 * <pre>
	 * checkIf { it.isNumber() }
	 * </pre>
	 */
	RuleBuilder isNumber() {
		checkIf { it.isNumber() }
	}
	
	// TODO add more common validation rules
}
