/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2;

/**
 * @since 3.1
 */
public interface Constants {

    /**
     * Name of the prameter where a copy of the original request
     * message (as a {@link ca.uhn.hl7v2.model.Message} instance) will be saved.
     */
    String ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME = "ipf.hl7v2.OriginalMessageAdapter";

    /**
     * Name of the parameter where a copy of the original request
     * message (as a {@link String} instance) will be saved.
     */
    String ORIGINAL_MESSAGE_STRING_HEADER_NAME  = "ipf.hl7v2.OriginalMessageString";
}
