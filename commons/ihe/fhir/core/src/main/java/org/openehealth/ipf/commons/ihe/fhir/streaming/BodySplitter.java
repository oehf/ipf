/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.streaming;

import ca.uhn.fhir.rest.api.EncodingEnum;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copies a FHIR request body, streaming the document content out of it into a
 * {@link StagedContentRegistry} and replacing it with a short marker.
 *
 * @author Christian Ohr
 * @see JsonBodySplitter
 * @see XmlBodySplitter
 */
interface BodySplitter {

    /**
     * @param body     the request body
     * @param registry registry to stream the document content into
     * @return the request body with the document content replaced by markers
     * @throws MalformedBodyException if the body is not well-formed
     * @throws IOException            on any other read or write error
     */
    byte[] split(InputStream body, StagedContentRegistry registry) throws IOException;

    /**
     * @return the encoding this splitter reads and writes, so that a request it rejects can be answered in the
     *         format it arrived in
     */
    EncodingEnum encoding();

    /**
     * Thrown when the body cannot be parsed far enough to split it. The FHIR parser would have rejected such a
     * body as well, so the request is answered with a 400 rather than being passed on.
     */
    class MalformedBodyException extends IOException {

        MalformedBodyException(String message) {
            super(message);
        }

        MalformedBodyException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
