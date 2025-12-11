/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti80

import org.apache.camel.builder.RouteBuilder
import org.apache.cxf.headers.Header
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.HeaderUtils

import javax.xml.namespace.QName

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti80RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti80ResponseValidator

class Iti80TestRouteBuilder extends RouteBuilder {

    static final QName HOME_COMMUNITY_ID_HEADER_NAME = new QName('urn:ihe:iti:xdr:2014', 'homeCommunityBlock')

    @Override
    void configure() throws Exception {

        from('xcdr-iti80://xcdr-iti80-service1')
            .process(iti80RequestValidator())
            .process {
                Map<QName, Header> headers = HeaderUtils.getIncomingSoapHeaders(it)
                it.message.body = new Response(headers.containsKey(HOME_COMMUNITY_ID_HEADER_NAME)
                        ? Status.SUCCESS
                        : Status.FAILURE)
            }
            .process(iti80ResponseValidator())

    }

}
