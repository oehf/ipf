#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.apache.camel.spring.Main;

public class SampleServer {

    public static void main(String[] args) throws Exception {
        Main.main("-ac", "/context.xml");
    }
    
}
