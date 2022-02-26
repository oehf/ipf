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
package org.openehealth.ipf.commons.ihe.hpd.controls;

/**
 * Abstract server-side handler.
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public interface Handler<RequestType, ResponseType> {

    /**
     * @return Handler which is one step nearer to the Camel route.
     */
    Handler<RequestType, ResponseType> getWrappedHandler();

    /**
     * @return batch response for the given batch requests.
     */
    ResponseType handle(RequestType request);

}
