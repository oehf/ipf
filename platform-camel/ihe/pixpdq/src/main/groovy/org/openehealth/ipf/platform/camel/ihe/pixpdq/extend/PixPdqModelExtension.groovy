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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.extend

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition


/**
 * DSL extensions for MLLP PIX/PDQ components.
 * @author Dmytro Rud
 */
class PixPdqModelExtension {
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti8Request   = { -> PixPdqExtension.iti8Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti8Response  = { -> PixPdqExtension.iti8Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti9Request   = { -> PixPdqExtension.iti9Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti9Response  = { -> PixPdqExtension.iti9Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti10Request  = { -> PixPdqExtension.iti10Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti10Response = { -> PixPdqExtension.iti10Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti21Request  = { -> PixPdqExtension.iti21Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti21Response = { -> PixPdqExtension.iti21Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti22Request  = { -> PixPdqExtension.iti22Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti22Response = { -> PixPdqExtension.iti22Response(delegate) }        
    }
}
