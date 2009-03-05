/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.model;

import java.util.ArrayList;
import org.apache.camel.Processor;
import org.apache.camel.model.OutputType;
import org.apache.camel.model.RouteType;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.processor.interceptor.StreamCaching;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * Processor type that disables stream caching
 * <p>
 * This type is used as a work around for a Camel bug (CAMEL-1413). Beginning
 * with Camel 1.6, stream caching is turned on by default. This will read the
 * entire stream into memory to allow error handling to read the stream again if
 * an exception occurred and the execution of the route is retried.
 * <p>
 * Unfortunately the documented ways to disable stream caching do not work.
 * Therefore, it cannot be disabled when working with the LBS.
 * <p>
 * This type ensures that the {@link RouteType#noStreamCaching()} works as
 * intended, i.e. stream caching can be disabled in the route. This only works
 * for Groovy routes because {@link RouteType#noStreamCaching()} is replaced by
 * creating this type.
 * <p>
 * This workaround should be removed as soon as the above bug is fixed in Camel.
 * 
 * @author Jens Riemschneider
 */
public class NoStreamCachingType extends OutputType<NoStreamCachingType> {
    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        disableStreamCaching(routeContext);
        DelegateProcessor dummy = new DelegateProcessor();
        dummy.setProcessor(routeContext.createProcessor(this));
        return dummy;
    }

    @Override
    protected Processor wrapInErrorHandler(RouteContext routeContext, Processor target) throws Exception {
        return target;
    }

    /**
     * Creates a collection that does not accept StreamCaching strategies and
     * puts it into the {@link RouteContext}
     * <p>
     * If subsequent steps try to add {@link StreamCaching} they will fail to do
     * so silently. This prevents stream caching from working in the route
     * <p>
     * The method preserves any existing strategies (other than
     * {@link StreamCaching})
     * 
     * @param routeContext
     *            the route context to change
     */
    private void disableStreamCaching(RouteContext routeContext) {
        ArrayList<InterceptStrategy> noStreamCachingList = new NoStreamCachingList();
        noStreamCachingList.addAll(routeContext.getInterceptStrategies());
        routeContext.setInterceptStrategies(noStreamCachingList);
    }

    private final class NoStreamCachingList extends ArrayList<InterceptStrategy> {
        /** Serial Version UID */
        private static final long serialVersionUID = 2278715114758913838L;

        @Override
        public boolean add(InterceptStrategy strategy) {
            if (strategy instanceof StreamCaching) {
                return false;
            }
            return super.add(strategy);
        }
    }
}
