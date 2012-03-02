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

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Dmytro Rud
 */
class ChainUtils {
    private static final transient Log LOG = LogFactory.getLog(ChainUtils.class)


    /**
     * Creates a chain from the given lists of standard and custom interceptors.
     * @param initial
     *      list of standard interceptors.
     * @param custom
     *      list of custom interceptors.
     * @return
     *      merged list.
     * @throws ChainException.
     */
    static <T extends Chainable> List<T> createChain(List<T> initial, Collection<T> custom) {
        ArrayList<Chainable> chain = new ArrayList<Chainable>(initial)
        ArrayList<Chainable> unprocessed = new ArrayList<Chainable>(custom)

        ArrayList<String> chainIds = chain*.id

        while (unprocessed) {
            boolean successful = false

            Iterator<Chainable> iter = unprocessed.iterator()
            while (iter.hasNext()) {
                final Chainable c = iter.next()
                final String cid = c.id

                // check whether interceptor with this ID is already in the chain
                if (cid in chainIds) {
                    LOG.debug("Interceptor with ID='${cid}' is already in the chain, ignore it")
                    iter.remove()
                    continue
                }

                // check whether this interceptor depends on some other unprocessed ones
                def unprocessedDependencies = (unprocessed - c).findAll { other ->
                    ((other.id in c.before) && ! (cid in other.after)) ||
                    ((other.id in c.after) && ! (cid in other.before))
                }

                if (unprocessedDependencies) {
                    LOG.debug("Interceptor '${cid}' depends on ${chainString(unprocessedDependencies)}")
                    continue
                }

                // Look where to insert this interceptor.  When neither "before" nor "after"
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
                            .append('\' in ')
                            .append(chainString(chainIds))
                            .toString())
                }

                chain.add(position, c)
                chainIds.add(position, cid)
                iter.remove()
                LOG.debug("Inserted interceptor '${cid}' at position ${position}")
                successful = true
            }

            if (successful) {
                LOG.debug("Iteration result: ${chain.size()} interceptors in the chain, ${unprocessed.size()} interceptors left")
            } else {
                throw new ChainException("Cannot build a chain, probably there is a dependency loop in ${chainString(unprocessed)}")
            }
        }

        return chain
    }


    /**
     * Returns string representation of the given collection
     * of interceptors or interceptor IDs.
     * @param collection
     *      list of interceptors or interceptor IDs.
     * @return
     *      string representation of the given list.
     */
    static String chainString(Collection collection) {
        if (! collection) {
            return ''
        }

        if (collection[0] instanceof Chainable) {
            return chainString(collection*.id)
        }

        return "'${collection.join(' ')}'"
    }

}
