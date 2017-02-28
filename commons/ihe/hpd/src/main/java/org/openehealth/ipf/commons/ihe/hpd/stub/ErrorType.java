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
package org.openehealth.ipf.commons.ihe.hpd.stub;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum ErrorType {
    NOT_ATTEMPTED("notAttempted"),
    COUND_NOT_CONNECT("couldNotConnect"),
    CONNECTION_CLOSED("connectionClosed"),
    MALFORMED_REQUEST("malformedRequest"),
    GATEWAY_INTERNAL_ERROR("gatewayInternalError"),
    AUTHENTICATION_FAILED("authenticationFailed"),
    UNRESOLVABLE_URI("unresolvableURI"),
    OTHER("other");

    @Getter private final String code;
}
