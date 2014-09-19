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
package org.openehealth.ipf.platform.camel.ihe.mllp

import org.apache.camel.component.mina2.Mina2Configuration
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.component.mina2.Mina2Component
import org.openehealth.ipf.platform.camel.ihe.mllp.core.HL7MLLPCodec

final String message =
    'MSH|^~\\&|@1.2.840.114350.1.13.99998.8734||@1.2.840.114350.1.13.99999.4567||20091112115139||ADT^A40|123@52918b99-dc72-4d17-8714-f39b9b6f16e8|P^T|2.3.1\r' +
    'EVN|A40|20091112115139\r' +
    'PID|||120-01^^^&1.2.3.4.5.1000&ISO\r' +
    'MRG|120-02^^^domain1\r' +
    'PV1||O\r'

final encoding = 'UTF-8'

def codec = new HL7MLLPCodec()
codec.setCharset(encoding)

def config = new Mina2Configuration(
        host                : 'localhost',
        port                : 1802,

        protocol            : 'tcp',
        sync                : true,
        codec               : codec,
        encoding            : encoding,
        timeout             : 30000,
        lazySessionCreation : true,
)

def endpoint = new Mina2Component(new DefaultCamelContext()).createEndpoint(config)
def producer = endpoint.createProducer()
def exchange = producer.createExchange()
exchange.in.body = message

try {
    producer.process(exchange)
} catch (Exception e) {
    e.printStackTrace()
}

println exchange.out.body