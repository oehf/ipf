/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.hpd.HpdValidator;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.chpidd.DownloadResponse;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;

/**
 * @author Dmytro Rud
 */
public class HpdCamelValidators {

    private static final Processor ITI_58_REQUEST_VALIDATOR = exchange -> {
        BatchRequest request = exchange.getIn().getMandatoryBody(BatchRequest.class);
        new HpdValidator().validateBatchRequest(request);
    };

    private static final Processor ITI_58_RESPONSE_VALIDATOR = exchange -> {
        BatchResponse response = exchange.getIn().getMandatoryBody(BatchResponse.class);
        new HpdValidator().validateBatchResponse(response);
    };

    private static final Processor ITI_59_REQUEST_VALIDATOR = exchange -> {
        // TODO
    };

    private static final Processor ITI_59_RESPONSE_VALIDATOR = exchange -> {
        // TODO
    };

    private static final Processor CH_PIDD_REQUEST_VALIDATOR = exchange -> {
        DownloadRequest request = exchange.getIn().getMandatoryBody(DownloadRequest.class);
        new HpdValidator().validateDownloadRequest(request);
    };

    private static final Processor CH_PIDD_RESPONSE_VALIDATOR = exchange -> {
        DownloadResponse response = exchange.getIn().getMandatoryBody(DownloadResponse.class);
        new HpdValidator().validateDownloadResponse(response);
    };

    public static Processor iti58RequestValidator() {
        return ITI_58_REQUEST_VALIDATOR;
    }

    public static Processor iti58ResponseValidator() {
        return ITI_58_RESPONSE_VALIDATOR;
    }

    public static Processor iti59RequestValidator() {
        return ITI_59_REQUEST_VALIDATOR;
    }

    public static Processor iti59ResponseValidator() {
        return ITI_59_RESPONSE_VALIDATOR;
    }

    public static Processor chPiddRequestValidator() {
        return CH_PIDD_REQUEST_VALIDATOR;
    }

    public static Processor chPiddResponseValidator() {
        return CH_PIDD_RESPONSE_VALIDATOR;
    }

}
