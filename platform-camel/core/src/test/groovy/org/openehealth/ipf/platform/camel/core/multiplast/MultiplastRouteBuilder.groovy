/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.multiplast

import org.apache.camel.builder.RouteBuilder

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MultiplastRouteBuilder extends RouteBuilder {

    void configure() throws Exception {

        ThreadPoolExecutor privilegedThreadPool = new ThreadPoolExecutor(3,
                3, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory())

        from('direct:start')
                .multiplast(
                        this,
                        body().tokenize(', '),
                        header('recipients').tokenize(';'),
                        new MultiplastAggregationStrategy(),
                        privilegedThreadPool)

        from('netty:tcp://localhost:10000?textline=true&sync=true')
                .validate().constant('abc')
                .transform().constant('123')

        from('netty:tcp://localhost:10001?textline=true&sync=true')
                .validate().constant('def')
                .transform().constant('456')

        from('netty:tcp://localhost:10002?textline=true&sync=true')
                .validate().constant('ghi')
                .transform().constant('789')
    }


}
