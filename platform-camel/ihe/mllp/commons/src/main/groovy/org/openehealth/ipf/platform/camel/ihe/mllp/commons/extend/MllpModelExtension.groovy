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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons.extend

import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition
import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MessageAdapterValidatorimport org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpMarshalUtilsimport org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.apache.camel.Exchangeimport org.apache.camel.Messageimport org.openehealth.ipf.platform.camel.core.util.Exchanges

/**
 * DSL extensions for MLLP PIX/PDQ components
 * @author Dmytro Rud
 */
class MllpModelExtension {

    static validationClosure = { ->
        delegate.setValidator(new MessageAdapterValidator())
        delegate.input { 
            MllpMarshalUtils.extractMessageAdapter(
                it.in,
                it.getProperty(Exchange.CHARSET_NAME))
        }
    }

    
    static extensions = {
        ValidatorAdapterDefinition.metaClass.iti8Request  = validationClosure 
        ValidatorAdapterDefinition.metaClass.iti8Response = validationClosure
    }
}
