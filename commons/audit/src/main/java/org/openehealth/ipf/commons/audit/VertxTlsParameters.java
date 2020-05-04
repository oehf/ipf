/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit;

import io.vertx.core.net.NetClientOptions;

/**
 * Extension interface for {@link TlsParameters} for Vert.x based
 * ATNA clients.
 */
public interface VertxTlsParameters extends TlsParameters {

    static VertxTlsParameters getDefault() {
        return new CustomTlsParameters();
    }

    /**
     * Sets the Vert.x {@link NetClientOptions} according to te defined Tls Parameters
     *
     * @param options Vert.x {@link NetClientOptions}
     */
    void initNetClientOptions(NetClientOptions options);
}
