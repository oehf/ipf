#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
import org.apache.camel.builder.RouteBuilderpublic class SampleRouteConfig implements RouteBuilderConfig {

    void apply(RouteBuilder builder) {
        builder.from('direct:input1').transmogrify { it * 2 }
        builder.from('direct:input2').reverse()
    }
    
}
