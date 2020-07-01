/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.extend

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
import org.openehealth.ipf.platform.camel.core.support.transform.ext.StaticTransmogrifier
import org.openehealth.ipf.platform.camel.core.transform.TestTransmogrifier

import javax.xml.transform.dom.DOMResult
import javax.xml.transform.stream.StreamSource

import static org.openehealth.ipf.commons.xml.AbstractCachingXmlProcessor.RESOURCE_LOCATION

/**
 * @author Martin Krasser
 */
class TransmogrifierRouteBuilder extends RouteBuilder {

    void configure() {

        def transmogrifier = new StaticTransmogrifier()
        transmogrifier.from = 'a'
        transmogrifier.to = 'b'

        errorHandler(noErrorHandler())

        from('direct:reply')
                .transmogrify { it + 'xyz' }

        from('direct:forward')
                .transmogrify { it + 'xyz' }
                .to('mock:output')

        from('direct:input1')
                .transmogrify { input ->
                    input.reverse()
                }
                .to('mock:output')

        from('direct:input2')
                .transmogrify { input, headers ->
                    input + '-' + headers.foo
                }
                .to('mock:output')

        from('direct:input3')
                .transmogrify { input ->
                    input.reverse()
                }
                // customize transmogrify input using an Expression instance
                .input(header('foo'))
                .to('mock:output')

        from('direct:input4')
                .transmogrify {input -> input.reverse()}
                // customize transmogrify input using a closure ('foo' header x 2)
                .input {exchange -> exchange.in.headers.foo * 2}
                .to('mock:output')

        from('direct:input5')
                .transmogrify {input, foo -> foo.reverse()}
                // customize transmogrify params using an Expression instance
                .params().header('foo')
                .to('mock:output')

        from('direct:input6')
                .transmogrify {input, foo -> foo.reverse()}
                // customize transmogrify params using an Expression instance
                .params {exchange -> exchange.in.headers.foo }
                .to('mock:output')

        from('direct:input7')
                .transmogrify {input, staticParams ->
                    input + staticParams[0] + staticParams[1] + staticParams[2]
                }
                // customize transmogrify staticParams using a 3-element array
                .staticParams('a', 'b', 'c')
                .to('mock:output')

        from('direct:input8')
                .transmogrify(transmogrifier)
                .to('mock:output')

        from('direct:input9a')
                .transmogrify('sampleTransmogrifier')
                .to('mock:output')

        from('direct:input9b')
                .transmogrify(lookup('sampleTransmogrifier', Transmogrifier.class))
                .to('mock:output')

        from('direct:input10')
                // transmogrify input is value of 'foo' header. Its reversed value
                // is set to the 'bar' header (modifyable second closure argument).
                .transmogrify {foo, headers -> headers.bar = foo.reverse()}
                .input {exchange -> exchange.in.headers.foo}
                // transmogrify input is the previously generated 'bar' header
                // value. transmogrify params is the initial 'foo' header value
                // The 'foo' header value is appended to the 'bar' header value.
                .transmogrify {bar, foo -> bar + foo}
                .input(header('bar'))
                .params().header('foo')
                .to('mock:output')

        from('direct:input11')
                .transmogrify { body, xmlBuilder ->
                    xmlBuilder.result(body)
                    xmlBuilder.result
                }
                .params().builder()
                .to('mock:output')

        from('direct:input12')
                .transmogrify { body, params ->
                    def msgHeaders = params[0]
                    def xmlBuilder = params[1]
                    xmlBuilder.result(body + msgHeaders.foo)
                    xmlBuilder.result
                }
                .params().headersAndBuilder()
                .to('mock:output')

        from('direct:input13')
                .transmogrify('testTransmogrifier')
                .params().builder()
                .to('mock:output')

        from('direct:input14')
                .transmogrify(new TestTransmogrifier())
                .params().builder()
                .to('mock:output')

        // XSLT transmogrification

        from('direct:input15') // using a dedicated XsltTransmogrifier bean
                .convertBodyTo(StreamSource.class)
                .transmogrify('xslt').staticParams('/xslt/createPatient.xslt')
                .to('mock:output')

        from('direct:input16') // using an anonymous XsltTransmogrifier
                .transmogrify().xslt().staticParams('/xslt/createPatient.xslt')
                .to('mock:output')

        from('direct:input17') // using an anonymous XsltTransmogrifier with a different output format
                .transmogrify().xslt(InputStream.class).staticParams('/xslt/createPatient.xslt')
                .to('mock:output')

        from('direct:input18') // passing in Xslt parameters
                .transmogrify().xslt().staticParams('/xslt/createPatient.xslt',
                [processingCodeParam:'D',processingModeCodeParam:'I'])
                .to('mock:output')

        from('direct:input19') // specify stylesheet as named parameter
                .transmogrify().xslt().staticParams([(RESOURCE_LOCATION) : '/xslt/createPatient.xslt',
                    processingCodeParam:'D',
                    processingModeCodeParam:'I'])
                .to('mock:output')

        from('direct:input20') // take stylesheet from message header
                .setHeader('stylesheet', constant('/xslt/createPatient.xslt'))
                .transmogrify().xslt().params().header('stylesheet')
                .to('mock:output')

        from('direct:input21') // take stylesheet and parameters from message headers
                .setHeader(RESOURCE_LOCATION, constant('/xslt/createPatient.xslt'))
                .setHeader('processingCodeParam', constant('D'))
                .setHeader('processingModeCodeParam', constant('I'))
                .transmogrify().xslt()
                .to('mock:output')

        // Schematron transmogrification
        from('direct:input22') // using a dedicated SchematronTransmogrifier bean
                .convertBodyTo(StreamSource.class)
                .transmogrify('schematron').staticParams('/schematron/schematron-test-rules.xml')
                .to('mock:output')

        from('direct:input23') // using an anonymous SchematronTransmogrifier
                .transmogrify().schematron().staticParams('/schematron/schematron-test-rules.xml')
                .to('mock:output')

        // xqj transmogrifier
        from('direct:input24') // using a dedicated XqjTransmogrifier bean
                .convertBodyTo(StreamSource.class)
                .transmogrify('xqj').staticParams('/xquery/extract.xq', [id:'someid'])
                .to('mock:output')

        from('direct:input25') // using an anonymous XqjTransmogrifier
                .setHeader('someid', constant('headerId'))
                .transmogrify().xquery()
                .params{
                    ([
                        '/xquery/extract.xq',
                        [id: it.in.headers.someid]]
                    as Object[])}
                .to('mock:output')

        from('direct:input26') // using an anonymous XqjTransmogrifier with a different output format
                .transmogrify().xquery(InputStream.class).staticParams('/xquery/extract.xq', [id:'someid'])
                .to('mock:output')

        from('direct:input27') // using an anonymous XqjTransmogrifier with a different output format
                .transmogrify().xquery(DOMResult.class).staticParams('/xquery/extract.xq', [id:'someid'])
                .to('mock:output')

        from('direct:input28') // passing classpath parameters
                .transmogrify().xquery()
                .staticParams('/xquery/extract-map-resource.xq',[id:'mapid', mapfile:'xquery/mapping.xml'])
                .to('mock:output')

        from('direct:input29') // passing parameters as map
                .transmogrify().xquery().params{
                    [(RESOURCE_LOCATION) : '/xquery/extract-map-document.xq',
                                map: new StreamSource(getClass().getResourceAsStream('/xquery/mapping.xml')),
                                id:'externalid']}
                .to('mock:output')

        from('direct:input30') // passing classpath parameters
                .setHeader(RESOURCE_LOCATION, constant('/xquery/extract-headers.xq'))
                .setHeader('breadcrumbId', simple('${id}'))
                .transmogrify().xquery()
                .to('mock:output')
    }

    private <T> T lookup(String beanName, Class<T> clazz) {
        return getContext().getRegistry().lookupByNameAndType(beanName, clazz);
    }
}
