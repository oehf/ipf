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
package org.openehealth.ipf.modules.hl7.validation.model

import ca.uhn.hl7v2.validation.ValidationException

import static org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


/**
 * @author Christian Ohr
 */
class ClosurePrimitiveTypeRuleTest {
    
    @Test
    void testCorrect(){
        ClosurePrimitiveTypeRule rule = new ClosurePrimitiveTypeRule(ClosurePrimitiveTypeRule.PASS)
        rule.omitLeadingWhitespace = true
        assert 'trimmed  ' == rule.correct('   \ntrimmed  ')
        rule.omitTrailingWhitespace = true
        assert 'trimmed' == rule.correct('   \ntrimmed  ')
       
    }
    
    @Test
    void testTest(){
        ClosurePrimitiveTypeRule rule = new ClosurePrimitiveTypeRule( {
            it.size() >= 10 ?
                    [ new ValidationException("Primitive is longer than 10")] as ValidationException[] :
                    [] as ValidationException[]
        })
        rule.omitLeadingWhitespace = true
        rule.omitTrailingWhitespace = true
        assert rule.test('       \ntrimmed')
        rule.omitLeadingWhitespace = false
        rule.omitTrailingWhitespace = false
        assertFalse rule.test('       \ntrimmed')
    }
    
}
