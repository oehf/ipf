/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.tutorials.iheclient;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.StoredQuery;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;

import static org.openehealth.ipf.commons.ihe.hl7v3.PDQV3.Interactions.ITI_47;
import static org.openehealth.ipf.commons.ihe.hl7v3.PIXV3.Interactions.ITI_44_PIX;
import static org.openehealth.ipf.commons.ihe.hl7v3.PIXV3.Interactions.ITI_45;

/**
 * This is a simple IHE client helper class that provides a couple of sender methods
 */
public class IHEWebServiceClient implements CamelContextAware {

    private CamelContext camelContext;
    private final CombinedXmlValidator validator = new CombinedXmlValidator();

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }


    // ===================
    // XDS-related queries
    // ===================

    public QueryResponse iti18StoredQuery(StoredQuery query, String host, int port, String pathAndParameters) throws Exception {
        var storedQuery = new QueryRegistry(query);
        var endpoint = String.format("xds-iti18://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, storedQuery, QueryResponse.class);
    }

    // Use this for RAD-68, too
    public Response iti41ProvideAndRegister(ProvideAndRegisterDocumentSet documentSet, String host, int port, String pathAndParameters) throws Exception {
        var endpoint = String.format("xds-iti41://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, documentSet, Response.class);
    }

    public RetrievedDocumentSet iti43RetrieveDocumentSet(RetrieveDocumentSet retrieve, String host, int port, String pathAndParameters) throws Exception {
        var endpoint = String.format("xds-iti43://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, retrieve, RetrievedDocumentSet.class);
    }


    // =======================
    // PIX/PDQ-related queries
    // incl validation option
    // =======================

    public String iti44PatientFeed(String message, String host, int port, String pathAndParameters, boolean validate) throws Exception {
        if (validate) {
            validator.validate(message, ITI_44_PIX.getRequestValidationProfile());
        }
        var endpoint = String.format("pixv3-iti44://%s:%d/%s", host, port, pathAndParameters);
        var response = send(endpoint, message, String.class);
        if (validate) {
            validator.validate(response, ITI_44_PIX.getResponseValidationProfile());
        }
        return response;
    }

    public String iti45PatientQuery(String message, String host, int port, String pathAndParameters, boolean validate) throws Exception {
        if (validate) {
            validator.validate(message, ITI_45.getRequestValidationProfile());
        }
        var endpoint = String.format("pixv3-iti45://%s:%d/%s", host, port, pathAndParameters);
        var response = send(endpoint, message, String.class);
        if (validate) {
            validator.validate(response, ITI_45.getResponseValidationProfile());
        }
        return response;
    }

    public String iti47PatientDemographicsQuery(String message, String host, int port, String pathAndParameters, boolean validate) throws Exception {
        if (validate) {
            validator.validate(message, ITI_47.getRequestValidationProfile());
        }
        var endpoint = String.format("pdqv3-iti47://%s:%d/%s", host, port, pathAndParameters);
        var response = send(endpoint, message, String.class);
        if (validate) {
            validator.validate(response, ITI_47.getResponseValidationProfile());
        }
        return response;
    }

    // =======
    // Helpers
    // =======

    private <T> T send(String endpoint, Object input, Class<T> outType) throws Exception {
        var result = send(endpoint, input);
        return result.getMessage().getBody(outType);
    }

    private Exchange send(String endpoint, Object body) throws Exception {
        Exchange exchange = new DefaultExchange(getCamelContext());
        exchange.getIn().setBody(body);
        /*
        if (headers != null && !headers.isEmpty()) {
            exchange.getIn().getHeaders().putAll(headers);
        }
        */
        var template = camelContext.createProducerTemplate();
        var result = template.send(endpoint, exchange);
        if (result.getException() != null) {
            throw result.getException();
        }
        return result;
    }

}
