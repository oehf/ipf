package org.openehealth.tutorial

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.Exchange
import org.apache.cxf.message.MessageContentsList
import javax.activation.DataHandler
import javax.activation.DataSource
import org.apache.camel.component.cxf.CxfConstants
import org.openehealth.ipf.platform.camel.lbs.process.ResourceList


public class SampleRouteConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        // When the jetty endpoint receives a message the route checks the header 
        // for the request method.
        // The request method in the header is used to find out if we have a POST 
        // or GET request.
        // Depending on the request, we route the message to a "direct" endpoint.
        builder.from('jetty:http://localhost:8412/imagebin')
                .noStreamCaching()                   // tell Camel to not read the stream in memory
                .choice()   
                .when(builder.header('http.requestMethod').isEqualTo('POST')).to('direct:upload')
                .when(builder.header('http.requestMethod').isEqualTo('GET')).to('direct:download')
                .otherwise().end()
        
        // Deal with uploads
        builder.from('direct:upload')
                .noStreamCaching()                   // tell Camel to not read the stream in memory
                .store().with('resourceHandlers')    // ensure we can upload large files
                .process { Exchange exchange ->
                    // Transform the message into the CXF format
                    def params = new MessageContentsList()                    
                    def resourceList = exchange.in.getBody(ResourceList.class) 
                    params[0] = new DataHandler(resourceList.get(0))
                    exchange.in.setHeader(CxfConstants.OPERATION_NAME, "upload")
                    exchange.in.body = params
                }
                .to('cxf:bean:imageBinServer')       // webservice.upload() call
                .process { Exchange exchange ->
                    // Transform the message back to HTTP
                    def params = exchange.in.getBody(MessageContentsList.class)
                    exchange.in.body = params[0]
                }
        
        // Deal with downloads
        builder.from('direct:download')
                .noStreamCaching()                   // tell Camel to not read the stream in memory
                .process { Exchange exchange ->
                    // Transform the message into the CXF format
                    def params = new MessageContentsList()
                    params[0] = exchange.in.getHeader("handle")
                    exchange.in.setHeader(CxfConstants.OPERATION_NAME, "download")
                    exchange.in.body = params
                }
                .to('cxf:bean:imageBinServer')       // webservice.download() call
                .store().with('resourceHandlers')    // ensure we can download large files
                .process { Exchange exchange ->
                    // Transform the message back to HTTP
                    def params = exchange.in.getBody(MessageContentsList.class)
                    def resourceList = new ResourceList()
                    resourceList.add(params[0].dataSource)
                    exchange.in.body = resourceList
                }
    }    
}
