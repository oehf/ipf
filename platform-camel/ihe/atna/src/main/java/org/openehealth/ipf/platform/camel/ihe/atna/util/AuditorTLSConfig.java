/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.atna.util;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;

/**
 * Helper configuration bean that initializes the underlying ATNA facilities when
 * TLS is used as transport protocol.
 *
 * @deprecated use {@link org.openhealthtools.ihe.atna.auditor.AuditorTLSConfig}
 */
public class AuditorTLSConfig extends org.openhealthtools.ihe.atna.auditor.AuditorTLSConfig{

    public AuditorTLSConfig(AuditorModuleConfig auditorModuleConfig) {
        super(auditorModuleConfig);
    }
}
