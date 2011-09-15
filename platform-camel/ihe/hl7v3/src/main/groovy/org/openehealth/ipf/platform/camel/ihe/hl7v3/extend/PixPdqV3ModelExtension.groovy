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
 * <p>
 * Note that validation-related DSL extensions are deprecated.
 * Please use standard Camel <code>.process()</code> DSL
 * element with one of processors from
 * {@link org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators}
 * as argument.
 *
 * @author Dmytro Rud
 */
class PixPdqV3ModelExtension {
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti44Request  = { -> PixPdqV3Extension.iti44Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti44Response = { -> PixPdqV3Extension.iti44Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti45Request  = { -> PixPdqV3Extension.iti45Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti45Response = { -> PixPdqV3Extension.iti45Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti46Request  = { -> PixPdqV3Extension.iti46Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti46Response = { -> PixPdqV3Extension.iti46Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti47Request  = { -> PixPdqV3Extension.iti47Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti47Response = { -> PixPdqV3Extension.iti47Response(delegate) }
        
        ProcessorDefinition.metaClass.translateHL7v3toHL7v2 = { translator -> 
                    PixPdqV3Extension.translateHL7v3toHL7v2(delegate, translator) }
        ProcessorDefinition.metaClass.translateHL7v2toHL7v3 = { translator -> 
                    PixPdqV3Extension.translateHL7v2toHL7v3(delegate, translator) }
        
    }
}
