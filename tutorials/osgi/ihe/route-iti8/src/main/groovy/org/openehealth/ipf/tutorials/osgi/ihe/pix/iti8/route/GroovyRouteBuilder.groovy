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
package org.openehealth.ipf.tutorials.osgi.ihe.pix.iti8.route

import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage


/**
 * @author Dmytro Rud
 */
class GroovyRouteBuilder extends SpringRouteBuilder {

    void configure() throws Exception {

        // normal processing without auditing
        from('xds-iti8://0.0.0.0:8881?audit=false')
            .process {
                resultMessage(it).body = MessageUtils.ack(it.in.body.target)
            }

        // normal processing with auditing
        from('pix-iti8://0.0.0.0:8882')
            .process {
                resultMessage(it).body = MessageUtils.ack(it.in.body.target)
            }
         
        // normal processing with support for incomplete auditing
        from('xds-iti8://0.0.0.0:8883?allowIncompleteAudit=true')
            .process {
                resultMessage(it).body = MessageUtils.ack(it.in.body.target)
            }

    }
}