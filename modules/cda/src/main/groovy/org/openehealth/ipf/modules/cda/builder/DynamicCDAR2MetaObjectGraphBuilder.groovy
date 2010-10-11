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
package org.openehealth.ipf.modules.cda.builder

import groovytools.builder.*
import org.codehaus.groovy.runtime.InvokerHelper
import groovytools.builder.SchemaNotFoundException


/**
 * @author Christian Ohr
 */
public class DynamicCDAR2MetaObjectGraphBuilder extends CDAR2MetaObjectGraphBuilder{

     public DynamicCDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder,
             SchemaNode defaultSchema, Factory defaultFactory) {
         super(metaBuilder, defaultSchema, defaultFactory, null);
     }

     public DynamicCDAR2MetaObjectGraphBuilder(MetaBuilder metaBuilder,
             SchemaNode defaultSchema, Factory defaultFactory,
             Closure objectVisitor) {
         super(metaBuilder, defaultSchema, defaultFactory, objectVisitor);
     }     

     
     public java.lang.Object invokeMethod(java.lang.String methodName, java.lang.Object args) {
         if ('make'.equals(methodName)) {
                 InvokerHelper.invokeMethod(getMetaBuilder(), 'build', args)
         } else {
             synchronized(true){
                 super.invokeMethod(methodName, args)
             }
         }
     }
/*
     public Object invokeMethod(String methodName, Object args) {
         try {
             super.invokeMethod(methodName, args)
         } catch (SchemaNotFoundException e) {
             def closure = new MetaBuilderClosure(this, methodName, args)
             InvokerHelper.invokeMethod(getMetaBuilder(), 'build', [closure] as Object[])                  
         }
     }
*/     
}
