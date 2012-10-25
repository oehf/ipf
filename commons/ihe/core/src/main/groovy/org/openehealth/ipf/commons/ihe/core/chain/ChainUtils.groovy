/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.core.chain

import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * @author Dmytro Rud
 */
class ChainUtils {
    private static final transient Logger LOG = LoggerFactory.getLogger(ChainUtils.class)


    /**
     * Extends an initial chain with elements from a custom collection.
     *
     * @param initial
     *      initial chain, may be empty, but not <code>null</code>.
     * @param custom
     *      collection of objects to be added to the initial chain,
     *      may be empty, but not <code>null</code>.
     * @return
     *      merged chain.
     * @throws ChainException.
     *      when chain extension fails, e.g. when cyclic dependencies are discovered.
     */
    static <T extends Chainable> List<T> createChain(List<T> initial, Collection<T> custom) {
        ArrayList<T> chain = new ArrayList<T>(initial)
        ArrayList<T> unprocessed = new ArrayList<T>(custom)

        ArrayList<String> chainIds = chain*.id

        while (unprocessed) {
            boolean successful = false

            Iterator<T> iter = unprocessed.iterator()
            while (iter.hasNext()) {
                final T c = iter.next()
                final String cid = c.id

                // check whether element with this ID is already in the chain
                if (cid in chainIds) {
                    LOG.debug("Element '{}' is already in the chain, ignore it", cid)
                    iter.remove()
                    continue
                }

                // check whether this element depends on some other unprocessed ones
                def unprocessedDependencies = (unprocessed - c).findAll { other ->
                    ((other.id in c.before) && ! (cid in other.after)) ||
                    ((other.id in c.after) && ! (cid in other.before))
                }

                if (unprocessedDependencies) {
                    LOG.debug("Element '{}' depends on '{}'", cid, unprocessedDependencies*.id.join(' '))
                    continue
                }

                // Look where to insert this element.  When neither "before" nor "after"
                // are known -- insert at the end, i.e. directly before the standard Camel
                // consumer -- this corresponds to the old behaviour.
                int position = chain.size()

                List<Integer> beforeIndices = c.before.collect { chainIds.indexOf(it) }.findAll { it >= 0 }
                List<Integer>  afterIndices =  c.after.collect { chainIds.indexOf(it) }.findAll { it >= 0 }

                int minBeforePosition
                int maxAfterPosition

                if (beforeIndices) {
                    minBeforePosition = beforeIndices.min()
                    position = minBeforePosition
                }

                if (afterIndices) {
                    maxAfterPosition = afterIndices.max()
                    position = maxAfterPosition + 1
                }

                if (beforeIndices && afterIndices && (minBeforePosition <= maxAfterPosition)) {
                    throw new ChainException(new StringBuilder()
                            .append('Cannot place \'')
                            .append(cid)
                            .append('\' between \'')
                            .append(chainIds[maxAfterPosition])
                            .append('\' and  \'')
                            .append(chainIds[minBeforePosition])
                            .append('\' in \'')
                            .append(chainIds.join(' '))
                            .append('\'')
                            .toString())
                }

                chain.add(position, c)
                chainIds.add(position, cid)
                iter.remove()
                LOG.debug("Inserted element '{}' at position {}", cid, position)
                successful = true
            }

            if (successful) {
                LOG.debug("Iteration result: {} elements in the chain, {} elements left", chain.size(), unprocessed.size())
            } else {
                throw new ChainException("Cannot build a chain, probably there is a dependency loop in '${unprocessed*.id.join(' ')}'")
            }
        }

        return chain
    }

}
