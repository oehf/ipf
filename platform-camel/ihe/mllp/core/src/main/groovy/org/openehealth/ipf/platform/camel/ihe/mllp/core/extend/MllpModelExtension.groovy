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

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition


/**
 * DSL extensions for MLLP PIX/PDQ components.
 * @author Dmytro Rud
 */
class MllpModelExtension {
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti8Request = { -> MllpExtension.iti8Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti8Response = { -> MllpExtension.iti8Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti9Request = { -> MllpExtension.iti9Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti9Response = { -> MllpExtension.iti9Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti10Request = { -> MllpExtension.iti10Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti10Response = { -> MllpExtension.iti10Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti21Request = { -> MllpExtension.iti21Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti21Response = { -> MllpExtension.iti21Response(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti22Request = { -> MllpExtension.iti22Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti22Response = { -> MllpExtension.iti22Response(delegate) }        
    }
}
