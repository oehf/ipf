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

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
public interface ProducerHandler<RequestType, ResponseType> {

    /**
     * @return Handler which is one step nearer to the Camel route.
     */
    ProducerHandler<RequestType, ResponseType> getWrappedHandler();

    /**
     * @return Response to the given request.
     */
    ResponseType handle(Object clientObject, RequestType request) throws Exception;

}
