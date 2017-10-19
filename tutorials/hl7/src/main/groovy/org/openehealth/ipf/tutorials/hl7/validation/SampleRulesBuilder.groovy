/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.tutorials.hl7.validation

import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder

/**
 * @author Christian Ohr
 */
class SampleRulesBuilder extends NoValidationBuilder{

    // We define only a subset of the segments defined in the HL7 2.2 spec
    @Override
    protected void configure() {
        super.configure()

        forVersion('2.2')
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
    }
    
}
