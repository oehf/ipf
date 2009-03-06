#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.apache.camel.model.ProcessorType
class SampleModelExtension {

     static extensions = {
         
         ProcessorType.metaClass.reverse = {
             delegate.transmogrify { it.reverse() }
         }
         
     }
     
}
