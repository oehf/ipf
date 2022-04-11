/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls.handlers;

import lombok.Getter;

/**
 * @author Dmytro Rud
 * @since 4.3
 */
abstract public class ConsumerHandlerBase<RequestType, ResponseType> implements ConsumerHandler<RequestType, ResponseType> {

    @Getter
    private final ConsumerHandler<RequestType, ResponseType> wrappedHandler;

    protected ConsumerHandlerBase(ConsumerHandler<RequestType, ResponseType> wrappedHandler) {
        this.wrappedHandler = wrappedHandler;
    }

    abstract public ResponseType handle(RequestType request);

}
