/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.extend;

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition

/**
 * DSL extensions for XCPD components.
 *
 * @deprecated Please use standard Camel <code>.process()</code> DSL
 * element with one of processors from
 * {@link org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators}
 * as argument.
 *
 * @author Dmytro Rud
 */
@Deprecated
class XcpdModelExtension {
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti55Request  = { -> XcpdExtension.iti55Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti55Response = { -> XcpdExtension.iti55Response(delegate) }
        ValidatorAdapterDefinition.metaClass.iti56Request  = { -> XcpdExtension.iti56Request(delegate) }        
        ValidatorAdapterDefinition.metaClass.iti56Response = { -> XcpdExtension.iti56Response(delegate) }
    }
}
