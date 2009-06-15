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
package org.openehealth.ipf.platform.camel.test.performance.route

import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import static org.apache.camel.component.http.HttpMethods.HTTP_METHOD
import static org.apache.camel.component.http.HttpMethods.GET
import static org.apache.camel.component.http.HttpMethods.DELETE
import static org.apache.camel.component.http.HttpMethods.POST
import static org.apache.camel.component.http.HttpProducer.HTTP_RESPONSE_CODE
import static org.apache.commons.httpclient.HttpStatus.SC_METHOD_NOT_ALLOWED

import static org.apache.commons.io.IOUtils.closeQuietly
import static org.apache.commons.io.IOUtils.toByteArray
import static org.openehealth.ipf.commons.core.io.IOUtils.deserialize
/**
 * A route builder that provides REST style interface for accessing the statistics. 
 * If the route builder is declared in the Spring application context, Camel will apply it 
 * automatically.
 * 
 * @author Mitko Kolev
 */
public class PerformanceMeasurementRouteBuilder extends SpringRouteBuilder {
    
    private final static String LOCALHOST = '0.0.0.0'
    private final static Log LOG = LogFactory.getLog(PerformanceMeasurementRouteBuilder.class.getName())
    
    /**
     * The port on which the server should be accessed
     */
    int httpPort
    /**
     * The URI path to access the statistcs resource
     */
    String statisticsPath = 'statistics'
    
    /**
     * The bean name to handle the requests from the Jetty server
     */
    String requestHanlderBean = 'performanceRequestHandler'
    
    /**
     * The method name of the <code>requestHanlderBean</code> 
     * to handle requests to view the statistcal reports
     */
    String reportsMethod = 'onRenderHTMLStatisticalReports'
    /**
     * The method name of the <code>requestHanlderBean</code> 
     * to handle requests to reset the statistcs data
     */
    String resetMethod = 'onResetStatistics'
    
    /**
     * Confiuration for the HTTP client, used by the embedded Jetty server 
     */
    String jettyHttpClientOptions = 'httpClient.idleTimeout=30000'
    
    void configure(){
        if (httpPort == 0){
            throw new IllegalArgumentException('The  httpPort of PerformanceMeasurementRouteBuilder can not be null!'); 
        }
        if (statisticsPath == null){
            throw new IllegalArgumentException('The  statisticsPath of PerformanceMeasurementRouteBuilder can not be null!'); 
        }
        from('jetty:http://' + LOCALHOST + ':' + httpPort + '/' + statisticsPath + '?'+ jettyHttpClientOptions )
                .choice()
                .when(header(HTTP_METHOD).isEqualTo(GET)).to('direct:getStatistics')
                .when(header(HTTP_METHOD).isEqualTo(DELETE)).to('direct:resetStatistics')
                .when(header(HTTP_METHOD).isEqualTo(POST)).to('direct:updateStatistics')
                .otherwise().to('direct:notAllowedStatisticsOperation')
        from('direct:getStatistics')
                .to('bean:' + requestHanlderBean + '?method=' + reportsMethod )
        from('direct:resetStatistics')
                .to('bean:' + requestHanlderBean + '?method=' + resetMethod )
        from('direct:updateStatistics')
                .to('seda:measurement')
                .transform{'Measurement history request queued'}
        from('seda:measurement')//TODO from camel 1.6.1 provide setting for concurrentConsumers
                .setBody {exchange ->
                    def history
                    InputStream stream = exchange.in.body
                    try{
                        byte [] body = toByteArray(stream)
                        history = deserialize(body)
                    }finally{
                        closeQuietly(stream)
                    }
                    history
                }
                .to('bean:performanceRequestHandler?method=onMeasurementHistory')
        from('direct:notAllowedStatisticsOperation')
                .setHeader(HTTP_RESPONSE_CODE).constant(SC_METHOD_NOT_ALLOWED)
                .transform{'The HTTP method is not allowed!'  }
        LOG.info('To view the statistics go to: http://'+ LOCALHOST +':' + httpPort + '/' + statisticsPath)
        
    }
}
