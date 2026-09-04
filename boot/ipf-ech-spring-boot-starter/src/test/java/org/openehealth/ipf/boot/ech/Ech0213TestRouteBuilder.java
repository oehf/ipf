/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.boot.ech;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.ech.EchUtils;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Request;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1.Response;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PidsFromUPIType;
import org.springframework.stereotype.Component;

/**
 * An eCH-0213 consumer, i.e. a route as a user of this starter would write it. Whether it can be started
 * at all is what the test is about: it requires the eCH Camel component to be on the classpath, the CXF
 * bus of the Spring Boot application to publish the web service, and the audit context contributed by
 * the ATNA starter to be present.
 */
@Component
public class Ech0213TestRouteBuilder extends RouteBuilder {

    public static final String SERVICE = "ech-0213-service";

    public static final Long VN = 7560000000002L;

    public static final String SPID = "761337612345678908";

    public static final EchUtils ECH_UTILS = new EchUtils(
            "T3-CH-24",
            "Open E-Health Foundation",
            "IPF",
            "6.0",
            true,
            "FR");

    @Override
    public void configure() {
        from("ech-0213:" + SERVICE)
                .process(exchange -> {
                    var request = exchange.getIn().getMandatoryBody(Request.class);

                    var pids = new PidsFromUPIType();
                    pids.setVn(VN);
                    pids.getSPID().add(SPID);

                    var positiveResponse = new Response.PositiveResponse();
                    positiveResponse.setSPIDCategory(EchUtils.getEPR_SPID_ID_CATEGORY());
                    positiveResponse.setPids(pids);

                    var response = new Response();
                    response.setHeader(ECH_UTILS.createHeader("1020", "6", request.getHeader()));
                    response.setPositiveResponse(positiveResponse);
                    exchange.getMessage().setBody(response);
                });
    }
}
