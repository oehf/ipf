/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.multiplast

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges

class MultiplastRouteBuilder extends SpringRouteBuilder {

    void configure() throws Exception {

        from('direct:start')
            .multiplast(
                this,
                body().tokenize(', '),
                header('recipients').tokenize(';'),
                new MultiplastAggregationStrategy())

        from('mina:tcp://localhost:8000?textline=true&sync=true')
            .process {
                assert it.in.body == 'abc'
                Exchanges.resultMessage(it).body = '123'
            }
            .delay(3000L)
        
        from('mina:tcp://localhost:8001?textline=true&sync=true')
            .process {
                assert it.in.body == 'def'
                Exchanges.resultMessage(it).body = '456'
            }
            .delay(3000L)
        
        from('mina:tcp://localhost:8002?textline=true&sync=true')
            .process {
                assert it.in.body == 'ghi'
                Exchanges.resultMessage(it).body = '789'
            }
            .delay(3000L)
            
    }
}
