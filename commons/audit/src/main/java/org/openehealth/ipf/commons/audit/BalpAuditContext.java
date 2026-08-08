/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.audit;

/**
 * @deprecated neither of the two properties this used to add is specific to
 * <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a>: an audit repository context path
 * belongs to the transport addressing it, and the token claim paths to the token. Both now sit on
 * {@link AuditContext} itself, so this interface adds nothing. Use {@link AuditContext}.
 *
 * @author Boris Stanojevic
 * @since 4.8
 */
@Deprecated(since = "5.3", forRemoval = true)
public interface BalpAuditContext extends AuditContext {
}
