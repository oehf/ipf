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
package org.openehealth.ipf.platform.camel.ihe.xds.core.extend

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition

/**
 * @author Jens Riemschneider
 */
class XDSModelExtension {
     
    static extensions = {        
        ValidatorAdapterDefinition.metaClass.iti14Request = { -> XDSExtension.iti14Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti14Response = { -> XDSExtension.iti14Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti15Request = { -> XDSExtension.iti15Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti15Response = { -> XDSExtension.iti15Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti16Request = { -> XDSExtension.iti16Request(delegate) } 
        ValidatorAdapterDefinition.metaClass.iti16Response = { -> XDSExtension.iti16Response(delegate) }    

        ValidatorAdapterDefinition.metaClass.iti41Request = { -> XDSExtension.iti41Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti41Response = { -> XDSExtension.iti41Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti42Request = { -> XDSExtension.iti42Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti42Response = { -> XDSExtension.iti42Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti43Request = { -> XDSExtension.iti43Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti43Response = { -> XDSExtension.iti43Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti18Request = { -> XDSExtension.iti18Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti18Response = { -> XDSExtension.iti18Response(delegate) }
    }
}
