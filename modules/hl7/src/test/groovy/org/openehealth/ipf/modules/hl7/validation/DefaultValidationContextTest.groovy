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

import ca.uhn.hl7v2.validation.EncodingRule
import ca.uhn.hl7v2.validation.MessageRule
import ca.uhn.hl7v2.validation.PrimitiveTypeRule
import ca.uhn.hl7v2.validation.ValidationContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.conf.store.ProfileStoreFactory
import ca.uhn.hl7v2.model.Message
import org.openehealth.ipf.modules.hl7.validation.model.MissingMessageRule

import org.openehealth.ipf.modules.hl7.validation.support.ClassPathProfileStore
import org.openehealth.ipf.modules.hl7.validation.model.ClosurePrimitiveTypeRule
import org.openehealth.ipf.modules.hl7.validation.model.ClosureMessageRule
import org.openehealth.ipf.modules.hl7.validation.model.ClosureEncodingRule
import org.openehealth.ipf.modules.hl7.parser.PipeParser

/**
 * @author Christian Ohr
 */
public class DefaultValidationContextTest extends GroovyTestCase{
   
    def noop = { String -> }
    
	void testGetPrimitiveRules(){
	    DefaultValidationContext r = new DefaultValidationContext();
	    PrimitiveTypeRule rule1 = new ClosurePrimitiveTypeRule(noop)
	    PrimitiveTypeRule rule2 = new ClosurePrimitiveTypeRule(noop)
	    r.addPrimitiveRule('2.5', 'ST', rule1)
	    r.addPrimitiveRule('2.5', 'ST', rule2)
	    assert r.getPrimitiveRules('2.5', 'ST', null)[0] == rule1
	    assert r.getPrimitiveRules('2.5', 'ST', null)[1] == rule2
	    assert r.getPrimitiveRules('2.4', 'ST', null).size() == 0
	}
	
	void testGetMessageRules(){
	    DefaultValidationContext r = new DefaultValidationContext();
	    MessageRule rule1 = new ClosureMessageRule (noop)
	    MessageRule rule2 = new ClosureMessageRule (noop)
	    r.addMessageRule('2.5', 'ADT', 'A01', rule1)
	    r.addMessageRule('2.5', 'ADT', 'A01', rule2)
	    assert r.getMessageRules('2.5', 'ADT', 'A01')[0] == rule1
	    assert r.getMessageRules('2.5', 'ADT', 'A01')[1] == rule2
	    assert r.getMessageRules('2.4', 'ADT', 'A01').size() == 0
	}
	
	void testGetMissingMessageRule(){
	    DefaultValidationContext r = new DefaultValidationContext(requireMessageRule:true);
	    MessageRule rule1 = new ClosureMessageRule(noop)
	    MessageRule rule2 = new ClosureMessageRule(noop)
	    r.addMessageRule('2.5', 'ADT', 'A01', rule1)
	    r.addMessageRule('2.5', 'ADT', 'A01', rule2)
	    assert r.getMessageRules('2.4', 'ADT', 'A01').size() == 1
		assert r.getMessageRules('2.4', 'ADT', 'A01')[0] instanceof MissingMessageRule
	}
	
	
	void testGetEncodingRules(){
	    DefaultValidationContext r = new DefaultValidationContext();
	    EncodingRule rule1 = new ClosureEncodingRule(noop)
	    EncodingRule rule2 = new ClosureEncodingRule(noop)
	    r.addEncodingRule('2.5', 'bla', rule1)
	    r.addEncodingRule('2.5', 'bla', rule2)
	    assert r.getEncodingRules('2.5', 'bla')[0] == rule1
	    assert r.getEncodingRules('2.5', 'bla')[1] == rule2
        assert r.getEncodingRules('2.4', 'bla').size() == 0
	}
	
	// A test that builds up rules and applies them to the parser
	// while it parses a message. It fails because of the nonsense
	// rule that IDs may not be longer than 1
	void testComplete(){
	    ValidationContext context = new DefaultValidationContext().configure()        
            .forVersion().asOf('2.3')
                .primitiveType('DT')
                    .matches(/(\d{4}([01]\d(\d{2})?)?)?/)				// YYYY[MM[DD]]
                    .withReference('Version 2.5 Section 2.A.21')
            .forVersion().before('2.3')
                .primitiveType('DT')
                    .matches(/(\d{4}[01]\d(\d{2})?/)					// YYYYMMDD
                            .withReference('Version 2.5 Section 2.A.21')
      
            .forAllVersions()
                .primitiveType('FT')[1..65535]
                .primitiveType('ST').omitLeadingWhitespace()
                .primitiveType('TX').omitTrailingWhitespace()
                .primitiveType('ID')[1..1].withDescription('IDs may not be longer than 1')     // NONSENSE. Will throw
                .primitiveType('IS').checkIf { it.value.size() <= 20 }		    // same as maxSize(20)
                .primitiveType('SI').matches(/\d*/)							// non-negative integer
                .primitiveType('NM').matches(/(+|-)?\d*\.?\d*/)				// number
                .primitiveType('TM')
                    .matches(/([012]\d([0-5]\d([0-5]\d(\.\d(\d(\d(\d)?)?)?)?)?)?)?([+-]\d{4})?/)	//HH[MM[SS[.S[S[S[S]]]]]][+/-ZZZZ]
                    .withReference('Version 2.5 Section 2.16.79')
            .context
            
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def parser = new PipeParser(context)
        try {
            parser.parse(msgText)
        } catch (Exception e) {
            assert e instanceof HL7Exception
            assert e.message.contains('IDs may not be longer than 1')
            assert e.message.contains('MSH (rep 0) Field #8')
        }

	}

	/*
	void testConformanceProfile() {
	    DefaultValidationContext context = new DefaultValidationContext()
	    ProfileStoreFactory.setStore(new ClassPathProfileStore())
	    context.configure()
	        .forVersion('2.5')
	            .message('QBP', 'Q22').conformsToProfile('IHE-PDQ-QBP-Q22')
	    def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
	    def parser = new PipeParser(context)
	    try {
		    parser.parse(msgText)
		    fail("Should fail due to missing message timestamp")
	    } catch (HL7Exception e) {
	        // ok
	        println e.cause
	    }
	}
*/

    void testWildcardMessageRule() {
        DefaultValidationContext context = new DefaultValidationContext()
        context.configure()        
            .forVersion('2.5')
                .message('ADT', '*')
                    .checkIf { null }
        assert context.getMessageRules('2.5', 'ADT', 'A01').size() == 1
        assert context.getMessageRules('2.5', 'ADT', 'A02').size() == 1
    }

    void testMultiMessageRule1() {
        DefaultValidationContext context = new DefaultValidationContext()
        context.configure()        
            .forVersion('2.5')
                .message('ADT', 'A01 A02')
                    .checkIf { null }
        assert context.getMessageRules('2.5', 'ADT', 'A01').size() == 1
        assert context.getMessageRules('2.5', 'ADT', 'A02').size() == 1
        assert context.getMessageRules('2.5', 'ADT', 'A03').size() == 0
    }

    void testMultiMessageRule2() {
        DefaultValidationContext context = new DefaultValidationContext()
        context.configure()        
            .forVersion('2.5')
                .message('ADT', ['A01', 'A02'])
                    .checkIf { null }
        assert context.getMessageRules('2.5', 'ADT', 'A01').size() == 1
        assert context.getMessageRules('2.5', 'ADT', 'A02').size() == 1
        assert context.getMessageRules('2.5', 'ADT', 'A03').size() == 0
    }
    
	void testValidAbstractSyntax1() {
	    DefaultValidationContext context = new DefaultValidationContext()
	    context.configure()        
	        .forVersion('2.5')
	            .message('ADT', 'A01').abstractSyntax(
	                    'MSH',
	                    [  {  'SFT'  }  ]  ,
	                    'EVN',
	                    'PID',
	                    [  'PD1'  ]  ,
	                    [  {  'ROL'  }  ]  ,
	                    [  {  'NK1'  }  ]  ,
	                    'PV1',
	                    [  'PV2'  ]  ,
	                    [  {  'ROL'  }  ] , 
	                    [  {  'DB1'  }  ] , 
	                    [  {  'OBX'  }  ]  ,
	                    [  {  'AL1'  }  ]  ,
	                    [  {  'DG1'  }  ]  ,
	                    [  'DRG'  ]  ,	                                                  
	                    [{PROCEDURE(  'PR1'  ,
	                       [  {  'ROL'  }  ]  
	                    )}],
	                    [  {  'GT1'  }  ]  ,
	                    [{INSURANCE(  'IN1'  ,
	                       [  'IN2'  ]  ,
	                       [  {  'IN3'  }  ]  ,
	                       [  {  'ROL'  }  ]  
	                    )}],
	                    [  'ACC'  ]  ,
	                    [  'UB1'  ]  ,
	                    [  'UB2'  ]  ,
	                    [  'PDA'  ]  )

	    def msgText = this.class.classLoader.getResource('msg-06.hl7')?.text
	    def parser = new PipeParser(context)
	    parser.parse(msgText)
	}

    void testValidAbstractSyntax2() {
        DefaultValidationContext context = new DefaultValidationContext()
        context.configure()        
            .forVersion('2.5')
                .message('ORU', 'R01').abstractSyntax(
                        'MSH',
                        [  {  'SFT'  }  ],
                        {PATIENT_RESULT(
                                [PATIENT(
                                        'PID',
                                        [  'PD1'  ],  
                                        [  {  'NTE'  }  ],  
                                        [  {  'NK1'  }  ],  
                                        [VISIT(
                                                'PV1',  
                                                [  'PV2'  ]  
                                        )]
                                 )],
                                 {ORDER_OBSERVATION(
                                         [  'ORC'  ],
                                         'OBR',
                                         [{  'NTE'  }],
                                         [{TIMING_QTY(
                                                 'TQ1',
                                                 [{  'TQ2'  }]
                                         )}],
                                         [  'CTD'  ],  
                                         [{OBSERVATION(
                                                'OBX',
                                                [  {  'NTE'  }  ] 
                                         )}],
                                         [{  'FT1'  }],
                                         [{  'CTI'  }],
                                         [{SPECIMEN(
                                                 'SPM',
                                                 [{  'OBX'  }]
                                         )}]
                                  )}
                         )},
                         [ 'DSC' ]
                    )
    
        def msgText = this.class.classLoader.getResource('msg-07.hl7')?.text
        def parser = new PipeParser(context)
        Message m = parser.parse(msgText)
    }

	void testInvalidAbstractSyntax() {
	    DefaultValidationContext context = new DefaultValidationContext()
	    context.configure()
	        .forVersion('2.5')
	            .message('ADT', 'A01').abstractSyntax(
                'MSH',
                'EVN',
                'PID',
                [{ 'NK1' }])

         def msgText = this.class.classLoader.getResource('msg-06.hl7')?.text
         def parser = new PipeParser(context)
	    try {
	        Message m = parser.parse(msgText)
	        fail("Should throw an HL7Exception")
	    } catch (HL7Exception e) {
	        // o.k.
	    }	    
     }

}
