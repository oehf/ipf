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
package org.openehealth.ipf.modules.ccd.builder

import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.section.*

/**
 * Make sure that the CDAModelExtensions are called before
 *
 * @author Christian Ohr
 */
public class CCDExtension extends CompositeModelExtension{
     
    CCDExtension() {
    }
    
    CCDExtension(CCDBuilder builder) {
        super(builder)
    }
    
    List modelExtensions() {
        [
             new CCDPurposeExtension(),
             new CCDPayersExtension()
        ]
    }
    
    String templateId() {
        '2.16.840.1.113883.10.20.1'
    }
    
    String extensionName() {
        "Continuity of Care Document (CCD)"
    }
    
    
    
    
}