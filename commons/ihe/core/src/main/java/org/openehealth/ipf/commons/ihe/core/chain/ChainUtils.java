/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.core.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christian Ohr
 * @author Dmytro Rud
 */
public class ChainUtils {

    private static final transient Logger LOG = LoggerFactory.getLogger(ChainUtils.class);

    /**
     * Extends an initial chain with elements from a custom collection.
     *
     * @param initial initial chain, may be empty, but not <code>null</code>.
     * @param custom  collection of objects to be added to the initial chain,
     *                may be empty, but not <code>null</code>.
     * @return merged chain.
     * @throws ChainException when chain extension fails, e.g. when cyclic dependencies are discovered.
     */
    public static <T extends Chainable> List<T> createChain(List<T> initial, Collection<T> custom) {
        var chain = new ArrayList<>(initial);
        var unprocessed = new ArrayList<>(custom);

        var chainIds = chain.stream()
                .map(Chainable::getId)
                .collect(Collectors.toList());

        while (!unprocessed.isEmpty()) {
            var successful = false;

            var iter = unprocessed.iterator();
            while (iter.hasNext()) {
                final var c = iter.next();
                final var cid = c.getId();

                // check whether element with this ID is already in the chain
                if (chainIds.contains(cid)) {
                    LOG.debug("Element {} is already in the chain, ignore it", cid);
                    iter.remove();
                    successful = true;
                    continue;
                }

                var unProcessedWithoutC = new ArrayList<>(unprocessed);
                unProcessedWithoutC.remove(c);
                // check whether this element depends on some other unprocessed ones
                var unprocessedDependencies = unProcessedWithoutC.stream()
                        .filter(other ->
                                (c.getBefore().contains(other.getId()) && !(other.getAfter().contains(cid))) ||
                                (c.getAfter().contains(other.getId()) && !(other.getBefore().contains(cid)))
                        )
                        .collect(Collectors.toList());

                if (!unprocessedDependencies.isEmpty()) {
                    LOG.debug("Element {} depends on {}", cid,
                            unprocessedDependencies.stream()
                                    .map(Chainable::getId)
                                    .collect(Collectors.joining(" ")));
                    continue;
                }

                // Look where to insert this element.  When neither "before" nor "after"
                // are known -- insert at the end, i.e. directly before the standard Camel
                // consumer -- this corresponds to the old behaviour.
                var position = chain.size();

                var beforeIndices = c.getBefore().stream()
                        .map(chainIds::indexOf)
                        .filter(value -> value >= 0)
                        .collect(Collectors.toList());

                var afterIndices = c.getAfter().stream()
                        .map(chainIds::indexOf)
                        .filter(value -> value >= 0)
                        .collect(Collectors.toList());

                var minBeforePosition = 0;
                var maxAfterPosition = 0;

                if (!beforeIndices.isEmpty()) {
                    minBeforePosition = beforeIndices.stream().min(Integer::compare).get();
                    position = minBeforePosition;
                }

                if (!afterIndices.isEmpty()) {
                    maxAfterPosition = afterIndices.stream().max(Integer::compare).get();
                    position = maxAfterPosition + 1;
                }

                if (!beforeIndices.isEmpty() && !afterIndices.isEmpty() && (minBeforePosition <= maxAfterPosition)) {
                    throw new ChainException(String.format(
                            "Cannot place %s between %s and %s in %s",
                            cid,
                            chainIds.get(maxAfterPosition),
                            chainIds.get(minBeforePosition),
                            String.join(" ", chainIds)));
                }

                chain.add(position, c);
                chainIds.add(position, cid);
                iter.remove();
                LOG.debug("Inserted element {} at position {}", cid, position);
                successful = true;
            }

            if (successful) {
                LOG.debug("Iteration result: {} elements in the chain, {} elements left", chain.size(), unprocessed.size());
            } else {
                throw new ChainException("Cannot build a chain, probably there is a dependency loop");
            }
        }

        return chain;
    }
}
