/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.util.List;
import java.util.Set;

/**
 * Decides which of a block of registry objects a requestor may see.
 * <p>
 * Most registries authorize per document against an inbound SAML or JWT token, and the decision is the
 * expensive part of the transaction: a call out of process, a policy set to evaluate, possibly a consent
 * document to fetch. The whole point of {@link AuthorizedPager} is to take as few of these as possible,
 * which is why this interface is <em>batched</em>: the block is the unit the pager sizes against the
 * observed pass rate, and a per-entry method would hide the round trip that sizing exists to amortise.
 * <p>
 * Typed on {@link XDSMetaClass} rather than on document entries, because a stored query may search for
 * folders or submission sets just as well, and those are as much subject to policy as a document is.
 *
 * @author Christian Ohr
 * @see AuthorizedPager
 */
public interface AuthorizationDecisionPoint {

    /**
     * @param requestor whoever the query arrived on behalf of
     * @param block     the registry objects to decide, in result set order, all of one kind
     * @return the {@link XDSMetaClass#getEntryUuid() entry UUIDs} of those the requestor may see;
     *         everything not in the returned set counts as denied
     */
    Set<String> decide(String requestor, List<? extends XDSMetaClass> block);
}
