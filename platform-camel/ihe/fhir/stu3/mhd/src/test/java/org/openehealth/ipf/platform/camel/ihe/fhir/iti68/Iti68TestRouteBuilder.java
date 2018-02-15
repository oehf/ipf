/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti68;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.support.ExpressionAdapter;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 *
 */
public class Iti68TestRouteBuilder extends RouteBuilder {

    static final String DOCUMENT_UNIQUE_ID = "documentUniqueId";
    static final String HOME_COMMUNITY_ID = "homeCommunityId";
    static final String REPOSITORY_ID = "repositoryId";

    static final byte[] DATA;

    static {
        DATA = new byte[10000];
        Arrays.fill(DATA, (byte) 'X');
    }

    private final boolean returnError;

    public Iti68TestRouteBuilder(boolean returnError) {
        this.returnError = returnError;
    }

    @Override
    public void configure() throws Exception {

        from("direct:input")
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .toF("http4:localhost:%d", StandardTestContainer.DEMO_APP_PORT);

        from("mhd-iti68:download?audit=true")
                .errorHandler(noErrorHandler())
                .transform(new Iti68Responder());
    }


    private class Iti68Responder extends ExpressionAdapter {

        @Override
        public Object evaluate(Exchange exchange) {
            Iti68AuditDataset auditDataset = exchange.getIn().getHeader(AuditInterceptor.AUDIT_DATASET_HEADER, Iti68AuditDataset.class);
            auditDataset.setDocumentUniqueId(DOCUMENT_UNIQUE_ID);
            auditDataset.setHomeCommunityId(HOME_COMMUNITY_ID);
            auditDataset.setRepositoryUniqueId(REPOSITORY_ID);
            if (!returnError) {
                return new ByteArrayInputStream(DATA);
            } else {
                throw new RuntimeException("Something went wrong");
            }
        }
    }

}
