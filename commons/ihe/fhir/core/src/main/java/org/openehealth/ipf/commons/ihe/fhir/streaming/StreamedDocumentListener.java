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

/**
 * Notified about every document that {@link Base64SplittingFilter} streamed out of a request body, so that the
 * application can meter the split without the split having to know about any particular metrics library.
 *
 * @author Christian Ohr
 */
@FunctionalInterface
public interface StreamedDocumentListener {

    /**
     * A listener that does nothing.
     */
    StreamedDocumentListener NOOP = size -> {
    };

    /**
     * @param sizeBytes size of the decoded document content
     */
    void documentStreamed(long sizeBytes);
}
