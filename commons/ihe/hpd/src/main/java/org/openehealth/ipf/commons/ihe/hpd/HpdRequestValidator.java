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
package org.openehealth.ipf.commons.ihe.hpd;

import org.openehealth.ipf.commons.ihe.hpd.stub.ErrorType;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchRequest;

/**
 * @author Dmytro Rud
 */
public class HpdRequestValidator {

    private void check(boolean condition, String message) {
        if (! condition) {
            throw new HpdException(message, ErrorType.MALFORMED_REQUEST);
        }
    }

    public void validateBatchRequest(BatchRequest request) {
        check(request != null, "Message is null");
        check(request.getBatchRequests() != null, "Request list is null");
        check(!request.getBatchRequests().isEmpty(), "Request list is empty");
        check(request.getBatchRequests().stream().allMatch(x -> x instanceof SearchRequest), "Only SearchRequests are supported");
    }
}
