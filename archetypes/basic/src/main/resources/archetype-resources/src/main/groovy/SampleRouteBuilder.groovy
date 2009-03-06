#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.apache.camel.spring.SpringRouteBuilder

class SampleRouteBuilder extends SpringRouteBuilder {

    void configure() {
        from('direct:input1').transmogrify { it * 2 }
        from('direct:input2').reverse()
    }
    
}
