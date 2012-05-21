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
package org.openehealth.ipf.modules.hl7.validation.support

import ca.uhn.hl7v2.validation.ValidationContext
import ca.uhn.hl7v2.parser.PipeParser
import ca.uhn.hl7v2.HL7Exception
import org.openehealth.ipf.commons.core.modules.api.ValidationException

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext

/**
 * @author Christian Ohr
 */
public class HL7ValidatorTest extends GroovyTestCase{
    
    	// A test that builds up rules and applies them to the parser
    	// while it parses a message. It fails because of the nonsense
    	// rule that IDs may not be longer than 1
    	void testValidateWhileParsing(){
    	    ValidationContext context = new DefaultValidationContext().configure()        
                .forVersion().asOf('2.3')
                    .type('DT')
                        .matches(/(\d{4}([01]\d(\d{2})?)?)?/)				// YYYY[MM[DD]]
                        .withReference('Version 2.5 Section 2.A.21')
                    
                .forVersion().before('2.3')
                    .type('DT')
                        .matches(/(\d{4}[01]\d(\d{2})?/)					// YYYYMMDD
                                .withReference('Version 2.5 Section 2.A.21')
          
                .forAllVersions()
                    .type('FT').maxSize(65536)
                    .type('ST').omitLeadingWhitespace()
                    .type('TX').omitTrailingWhitespace()
                    .type('ID').maxSize(1).withDescription('IDs may not be longer than 1')     
                                                                        // NONSENSE. Will throw
                                                                        
                    .type('IS').checkIf { it.value.size() <= 20 }		    // same as maxSize(20)
                    .type('SI').matches(/\d*/)							// non-negative integer
                    .type('NM').matches(/(+|-)?\d*\.?\d*/)				// number
                    .type('TM')
                        .matches(/([012]\d([0-5]\d([0-5]\d(\.\d(\d(\d(\d)?)?)?)?)?)?)?([+-]\d{4})?/)	
                        .withReference('Version 2.5 Section 2.16.79')   // HH[MM[SS[.S[S[S[S]]]]]][+/-ZZZZ]
                .context
                
            def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
            def parser = new PipeParser()
            parser.validationContext = context
                    
            try {
                def msg = parser.parse(msgText)
                fail("The message should fail")
            } catch (Exception e) {
                assert e instanceof HL7Exception
                assert e.message.contains('IDs may not be longer than 1')
                assert e.message.contains('MSH (rep 0) Field #8')
            }    
    }
    	
    void testValidateMessageByValidatorSuccess() {
        ValidationContext context = new DefaultValidationContext()
    	context.configure()        
    	    .forVersion('2.2')
    	            .message('ADT', 'A01').abstractSyntax(
    	                    'MSH',
    	                    'EVN',
    	                    'PID',
    	                    [  {  'NK1'  }  ],
    	                    'PV1',
    	                    [  {  INSURANCE(
                              'IN1',
                              [  'IN2'  ] ,
                              [  'IN3'  ]
    	                    )}]
    	            )
            
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def parser = new PipeParser()
                
        def msg = parser.parse(msgText)
        new HL7Validator().validate(msg, context)
    	    
    }

    void testValidateMessageByValidatorFail() {
        ValidationContext context = new DefaultValidationContext()
    	context.configure()        
    	    .forVersion('2.2')
    	            .message('ADT', 'A01').abstractSyntax(
    	                    'MSH',
    	                    'EVN',
    	                    'PID',
    	                    // [  {  'NK1'  }  ],  no NK1 expected
    	                    'PV1',
    	                    [  {  INSURANCE(
                              'IN1',
                              [  'IN2'  ] ,
                              [  'IN3'  ]
    	                    )}]
    	            )
            
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def parser = new PipeParser()
                
        try {
            def msg = parser.parse(msgText)
            new HL7Validator().validate(msg, context)
            fail("The message should fail")
        } catch (Exception e) {
            assert e instanceof ValidationException
            assert e.message.contains('Invalid message')
            assert e.cause.cause.message.contains('The structure NK1 appears in the message but not in the profile')
        }    
    	    
    	}
}
