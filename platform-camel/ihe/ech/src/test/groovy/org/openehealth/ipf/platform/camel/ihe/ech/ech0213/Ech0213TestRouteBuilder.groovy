/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.ech.ech0213

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.ech.EchUtils
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Request as Request0213
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Response as Response0213
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PidsFromUPIType

class Ech0213TestRouteBuilder extends RouteBuilder {

    static final EchUtils ECH_UTILS = new EchUtils(
        'sedex://T3-CH-24',
        'Open E-Health Foundation',
        'IPF',
        '5.0',
        true,
        'FR'
    )

    @Override
    void configure() throws Exception {

        from('ech-0213:service-1')
            .process(exchange -> {
                Request0213 request = exchange.getIn().getMandatoryBody(Request0213.class)
                Response0213 response = new Response0213(
                    header: ECH_UTILS.createHeader('1020', '6', request.header),
                    positiveResponse: new Response0213.PositiveResponse(
                        SPIDCategory: EchUtils.EPR_SPID_ID_CATEGORY,
                        pids: new PidsFromUPIType(
                            vn: 7560000000002L,
                            spid: [
                                '761337612345678908'
                            ],
                        ),
                    ),
                )
                exchange.message.body = response
            })
    }

}
