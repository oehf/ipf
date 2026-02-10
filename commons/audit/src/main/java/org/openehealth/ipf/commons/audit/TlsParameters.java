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

/**
 * Abstraction for setting TLS parameters to be used by ATNA sender implementations.
 * Use {@link DefaultAuditContext#setTlsParameters(org.openehealth.ipf.commons.core.ssl.TlsParameters)} (TlsParameters)} to configure the
 * audit context.
 * <p>
 * The default uses the default as defined by javax.net.ssl.* system properties.
 *
 * @author Christian Ohr
 *
 * @deprecated use {@link org.openehealth.ipf.commons.core.ssl.TlsParameters}
 */
@Deprecated(forRemoval = true)
public interface TlsParameters extends org.openehealth.ipf.commons.core.ssl.TlsParameters {

}
