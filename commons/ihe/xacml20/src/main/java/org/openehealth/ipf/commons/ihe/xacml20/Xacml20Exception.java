/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.xacml20;

import lombok.Getter;

import java.util.Objects;

public class Xacml20Exception extends Exception {

    @Getter
    private final Xacml20Status status;

    public Xacml20Exception(Xacml20Status status, String message, Throwable cause) {
        super(message, cause);
        this.status = Objects.requireNonNull(status, "Status code shall be provided");
    }

    public Xacml20Exception(Xacml20Status status) {
        this(status, null, null);
    }

    public Xacml20Exception(Xacml20Status status, String message) {
        this(status, message, null);
    }

    public Xacml20Exception(Xacml20Status status, Throwable cause) {
        this(status, cause.getMessage(), cause);
    }

}
