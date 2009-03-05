package org.openehealth.tutorial

import org.apache.camel.model.ProcessorType
import org.apache.camel.model.RouteType
import org.openehealth.ipf.platform.camel.lbs.model.NoStreamCachingType
public class SampleModelExtension {

     def extensions = {
         
         ProcessorType.metaClass.reverse = {
             delegate.transmogrify { it.reverse() }
         }
         
         RouteType.metaClass.noStreamCaching = {
             def type = new NoStreamCachingType();
             delegate.addOutput(type)
             type
         }         
     }
}
