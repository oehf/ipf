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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.extend

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.apache.camel.model.ProcessorDefinition
/**
 * DSL extensions for PIXv3/PDQv3 components.
 * @author Dmytro Rud
 */
class Hl7v3ModelExtension {
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti44Request  = { -> Hl7v3Extension.iti44Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti44Response = { -> Hl7v3Extension.iti44Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti45Request  = { -> Hl7v3Extension.iti45Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti45Response = { -> Hl7v3Extension.iti45Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti46Request  = { -> Hl7v3Extension.iti46Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti46Response = { -> Hl7v3Extension.iti46Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti47Request  = { -> Hl7v3Extension.iti47Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti47Response = { -> Hl7v3Extension.iti47Response(delegate) }
        
        ProcessorDefinition.metaClass.translateHl7RequestV3toV2 = { translator -> 
            Hl7v3Extension.translateHl7RequestV3toV2(delegate, translator) }
        ProcessorDefinition.metaClass.translateHl7ResponseV2toV3 = { translator -> 
            Hl7v3Extension.translateHl7ResponseV2toV3(delegate, translator) }
        ProcessorDefinition.metaClass.translateHl7ResponseV2toV3 = { translator, originalRequest -> 
            Hl7v3Extension.translateHl7ResponseV2toV3(delegate, translator, originalRequest) }
        
    }
}
