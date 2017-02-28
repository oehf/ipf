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
import org.openehealth.ipf.commons.ihe.hpd.HpdRequestValidator;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;

/**
 * @author Dmytro Rud
 */
public class HpdCamelValidators {

    private static final Processor ITI_58_REQUEST_VALIDATOR = exchange -> {
        BatchRequest request = exchange.getIn().getMandatoryBody(BatchRequest.class);
        new HpdRequestValidator().validateBatchRequest(request);
    };

    private static final Processor ITI_58_RESPONSE_VALIDATOR = exchange -> {
        // TODO
    };

    private static final Processor ITI_59_REQUEST_VALIDATOR = exchange -> {
        // TODO
    };

    private static final Processor ITI_59_RESPONSE_VALIDATOR = exchange -> {
        // TODO
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

}
