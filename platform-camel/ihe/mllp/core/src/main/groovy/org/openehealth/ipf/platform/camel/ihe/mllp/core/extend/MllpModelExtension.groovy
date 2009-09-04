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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.extend

import org.openehealth.ipf.platform.camel.ihe.mllp.core.MessageAdapterValidatorimport org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtilsimport org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.modules.hl7.parser.PipeParser
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.apache.camel.Exchangeimport org.apache.camel.Message

/**
 * DSL extensions for MLLP PIX/PDQ components.
 * @author Dmytro Rud
 */
class MllpModelExtension {

    // This is an interim and monolithic implementation, which 
    // should be completely changed in subsequent versions.

    
    def static final classMap = 
        [
              8 : 'org.openehealth.ipf.platform.camel.ihe.pix.iti8.Iti8Component', 
              9 : 'org.openehealth.ipf.platform.camel.ihe.pix.iti9.Iti9Component', 
             10 : 'org.openehealth.ipf.platform.camel.ihe.pix.iti10.Iti10Component', 
             21 : 'org.openehealth.ipf.platform.camel.ihe.pdq.iti21.Iti21Component', 
             22 : 'org.openehealth.ipf.platform.camel.ihe.pdq.iti22.Iti22Component', 
        ] 
     
    
    static Closure getValidationClosure(parser) {
        { ->
            delegate.setValidator(new MessageAdapterValidator())
            delegate.input { 
                MllpMarshalUtils.extractMessageAdapter(
                    it.in,
                    it.getProperty(Exchange.CHARSET_NAME),
                    parser)
            }
        }
    }

    
    static extensions = {
        [8, 9, 10, 21, 22].each { transaction ->
            try {
                def parser = Class.forName(classMap[transaction]).newInstance().parser
                def closure = getValidationClosure(parser)
                ValidatorAdapterDefinition.metaClass."iti${transaction}Request"  = closure
                ValidatorAdapterDefinition.metaClass."iti${transaction}Response" = closure
            } catch(Exception e) {
                /* 
                 * Actually, in many cases there will be only one transaction 
                 * configured, i.e. there will be only one successful iteration 
                 * of this try-catch block.  All others will end here. 
                 */
            }
        }
    }
}
