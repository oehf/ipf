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

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContextimport groovy.util.GroovyTestCase

/**
 * @author Christian Ohr
 */
public class PrimitiveRuleBuilderTest extends GroovyTestCase {
	
	void testCheckIf() {
		def builder = new PrimitiveRuleBuilder('2.4', new DefaultValidationContext(), 'ST').checkIf { 'blarg' }
		assert builder.version == '2.4'
		assert builder.typeName == 'ST'
		assert builder.rule
		assert builder.rule.testClosure.call() == 'blarg'
	}     
	
	void testOmitLeadingWhitespace() {
		def builder = new PrimitiveRuleBuilder('2.4', new DefaultValidationContext(), 'ST').omitLeadingWhitespace()
		assert builder.version == '2.4'
		assert builder.typeName == 'ST'
		assert builder.rule
		assert builder.rule.omitLeadingWhitespace
	}     
	
	void testMaxSize() {
		def builder = new PrimitiveRuleBuilder('2.4', new DefaultValidationContext(), 'ST').maxSize(10)
		assert builder.version == '2.4'
		assert builder.typeName == 'ST'
		assert builder.rule
		assert builder.rule.testClosure.call('12345')
		assertFalse builder.rule.testClosure.call('12345678901')
	} 
	
	void testNotEmpty() {
		def builder = new PrimitiveRuleBuilder('2.4', new DefaultValidationContext(), 'ST').notEmpty()
		assert builder.version == '2.4'
		assert builder.typeName == 'ST'
		assert builder.rule
		assert builder.rule.testClosure.call('12345')
		assertFalse builder.rule.testClosure.call('')
	} 
	
	void testMatches() {
		def builder = new PrimitiveRuleBuilder('2.4', new DefaultValidationContext(), 'ST').matches(/[abc]123/)
		assert builder.version == '2.4'
		assert builder.typeName == 'ST'
		assert builder.rule
		assert builder.rule.testClosure.call('a123')
		assertFalse builder.rule.testClosure.call('d123')
	}
}

