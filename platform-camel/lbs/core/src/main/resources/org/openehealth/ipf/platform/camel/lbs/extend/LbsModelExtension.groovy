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
package org.openehealth.ipf.platform.camel.lbs.extend

import org.openehealth.ipf.platform.camel.lbs.model.FetchProcessorType
import org.openehealth.ipf.platform.camel.lbs.model.StoreProcessorType
import org.openehealth.ipf.platform.camel.core.closures.DelegatingExpression
import org.apache.camel.Expression

import org.apache.camel.model.ProcessorType
import org.apache.camel.model.RouteType
import org.openehealth.ipf.platform.camel.lbs.model.NoStreamCachingType

/**
 * @author Jens Riemschneider
 */
class LbsModelExtension {

    def extensions = {
            
        ProcessorType.metaClass.store = {
            def answer = new StoreProcessorType()
            delegate.addOutput(answer)
            answer
        }        

        ProcessorType.metaClass.fetch = {
            def answer = new FetchProcessorType()
            delegate.addOutput(answer)
            answer
        }        

        RouteType.metaClass.noStreamCaching = {
            def type = new NoStreamCachingType();
            delegate.addOutput(type)
            type
        }         
    }    
}
