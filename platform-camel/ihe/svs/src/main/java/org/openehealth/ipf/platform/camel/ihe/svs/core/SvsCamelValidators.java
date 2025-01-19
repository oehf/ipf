/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.core;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.svs.core.SvsValidator;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * @author Quentin Ligier
 **/
public class SvsCamelValidators {

    private static final Processor ITI_48_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            var request = exchange.getIn().getMandatoryBody(RetrieveValueSetRequest.class);
            SvsValidator.validateIti48Request(request);
        }
    };

    private static final Processor ITI_48_RESPONSE_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            var response = exchange.getIn().getMandatoryBody(RetrieveValueSetResponse.class);
            SvsValidator.validateIti48Response(response);
        }
    };

    public static Processor iti48RequestValidator() {
        return ITI_48_REQUEST_VALIDATOR;
    }

    public static Processor iti48ResponseValidator() {
        return ITI_48_RESPONSE_VALIDATOR;
    }
}
