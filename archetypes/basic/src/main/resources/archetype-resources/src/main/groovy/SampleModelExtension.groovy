#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}

import org.apache.camel.model.ProcessorDefinition
class SampleModelExtension {
      
     static ProcessorDefinition reverse(ProcessorDefinition self) {
         self.transmogrify { it.reverse() } 
     }
     
}
