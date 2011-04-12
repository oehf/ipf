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
package org.openehealth.ipf.modules.cda.builder.content.header

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.BaseModelExtension

/**
 * Chapter 2.1 Header representation
 * 
 * @author Stefan Ivanov
 */
public class CCDMainActivityExtension extends BaseModelExtension{

     CCDMainActivityExtension() {
         super()
     }
     
     CCDMainActivityExtension(builder) {
         super(builder)
     }

	def register(Collection registered) {
	    
	    super.register(registered)

        // CONF-2: A CCD SHALL contain exactly one ClinicalDocument / documentationOf / serviceEvent.
        // CONF-3: The value for “ClinicalDocument / documentationOf / serviceEvent
        //         / @classCode” SHALL be “PCPR” “Care provision” 2.16.840.1.113883.5.6 ActClass STATIC.
        // CONF-4: ClinicalDocument / documentationOf / serviceEvent SHALL contain
        //         exactly one serviceEvent / effectiveTime / low and exactly one
        //         serviceEvent / effectiveTime / high.
        POCDMT000040ClinicalDocument.metaClass {

            setMainActivity {POCDMT000040ServiceEvent serviceEvent ->
                POCDMT000040DocumentationOf docOf = CDAR2Factory.eINSTANCE.createPOCDMT000040DocumentationOf()
                docOf.serviceEvent = serviceEvent
                delegate.documentationOf.add(docOf)
            }

            getMainActivity { ->
                delegate.documentationOf.find { it.serviceEvent.classCode.name == 'PCPR' } ?.serviceEvent
            }

        }// ccd header extensions

        
    }//ccd extensions 
    
    
    String templateId() {
        extensionName()
    }
    
    String extensionName() {
        'CCD Main Activity'
    }   
}