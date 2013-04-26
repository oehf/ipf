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

import static org.apache.camel.Exchange.HTTP_METHOD
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE
import static org.apache.camel.component.http.HttpMethods.DELETE
import static org.apache.camel.component.http.HttpMethods.GET
import static org.apache.camel.component.http.HttpMethods.POST
import static org.apache.commons.httpclient.HttpStatus.SC_METHOD_NOT_ALLOWED
import static org.openehealth.ipf.commons.test.performance.utils.MeasurementHistoryXMLUtils.unmarshall

import org.apache.camel.spring.SpringRouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * A route builder that provides REST style interface for accessing the statistics. 
 * If the route builder is declared in the Spring application context, Camel will apply it 
 * automatically.
 * 
 * @author Mitko Kolev
 */
public class PerformanceRouteBuilder extends SpringRouteBuilder {
    
    private final static String LOCALHOST = '0.0.0.0'
    private final static Logger LOG = LoggerFactory.getLogger(PerformanceRouteBuilder.class.getName())
    
    /**
     * The URI path to access statistcs related resources
     */
    String statisticsPath = 'statistics'
    
    /**
     * The bean name to handle the requests from the Jetty server
     */
    String requestHandlerBean = 'performanceRequestHandler'
    
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
     * The method name of the <code>requestHanlderBean</code> 
     * to handle update requests with measurment history objects
     */    
    String updateMethod = 'onMeasurementHistory'
    
    /**
     * Configures the queue size of the queue, that stores the measurement history it receives.
     * The queue is used to provide asynchronous processing of the measurement history. Note that 
     * if there is no memory for the incoming measurements (the throughput of the cluster 
     * is much higher than the throughput of the performance measurement server), the server 
     * may throw an OutOfMemoryError. In this case, all measurements made by the server must be 
     * considered invalid.
     *  
     * The property is optional.
     * 
     */
    int queueSize = Integer.MAX_VALUE
    
    /**
     * Configures if the performance measurement server should override the 
     * reference date of the measurement history it receives. 
     *
     * Overriding the date removes the limitation that the tested nodes of the IPF cluster 
     * must have the same time. However, when the throughput of the cluster is higher than 
     * the throughput of the performance measurement server, the performance measurement 
     * server will report it's throughtput rather than the cluster throughput. 
     * 
     * This behaviour can be turned off, when a time synchronization server runs on the cluster nodes.
     * 
     * The property is optional. 
     * 
     */
    boolean overrideMeasurementHistoryReferenceDate = true
    
    /**
     * Confiuration for the HTTP client, used by the embedded Jetty server. Split character is '&'
     * The property is only relevant for the performance measurement server. 
     *
     * The property is optional.
     */
    String jettyHttpClientOptions = 'httpClient.idleTimeout=30000&httpClient.maxConnectionsPerAddress=5'
    
    /**
     * The port on which the Jetty server should accept requests.
     *
     * The property is required.
     */
    int httpPort
    
    void configure(){
        if (httpPort <= 0){
            throw new IllegalArgumentException('The  httpPort of PerformanceMeasurementRouteBuilder can not be null!'); 
        }
        from('jetty:http://' + LOCALHOST + ':' + httpPort + '/' + statisticsPath + '?'+ jettyHttpClientOptions )
                .setHeader('requestTime'){ System.currentTimeMillis() }//store the time the message has arrived 
                .choice()
                .when(header(HTTP_METHOD).isEqualTo(POST)).to('direct:updateStatistics')
                .when(header(HTTP_METHOD).isEqualTo(GET)).to('direct:getStatistics')
                .when(header(HTTP_METHOD).isEqualTo(DELETE)).to('direct:resetStatistics')
                .otherwise().to('direct:notAllowedStatisticsOperation')
        from('direct:getStatistics')
                .to('bean:' + requestHandlerBean + '?method=' + reportsMethod )
        from('direct:resetStatistics')
                .to('bean:' + requestHandlerBean + '?method=' + resetMethod )
        from('direct:updateStatistics')
                .to("seda:measurement?size=${queueSize}")
                .transform{'Measurement history request queued'}
        from("seda:measurement?size=${queueSize}")//uses a single consumer
                .setBody {exchange ->
                    def history
                    //get the measurement history from the request body
                    //TODO: extract the content encoding from the Content-Type header
                    String measurementHistory = exchange.in.getBody(String.class)
                    history = unmarshall(measurementHistory)
                    //use the performance measuremetn server date for a reference date of the measurements
                    if (overrideMeasurementHistoryReferenceDate){
                        long time = exchange.in.getHeader('requestTime')
                        history.setReferenceDate(new Date(time))
                    }
                    history
                }
                .to('bean:' + requestHandlerBean + '?method=' +  updateMethod)
        from('direct:notAllowedStatisticsOperation')
                .setHeader(HTTP_RESPONSE_CODE).constant(SC_METHOD_NOT_ALLOWED)
                .transform{'The HTTP method is not allowed!'  }
        LOG.info('The performance measurent statistics are accessible at: http://'+ LOCALHOST +':' + httpPort + '/' + statisticsPath)
        
    }
}
