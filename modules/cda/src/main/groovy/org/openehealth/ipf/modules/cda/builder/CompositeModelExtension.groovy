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

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import groovytools.builder.MetaBuilder
/**
 * ModelExtension that recursively calls other extension classes. Overwrite the
 * modelExtensions() method to include the other extension classes
 * 
 * @author Christian Ohr
 */
abstract class CompositeModelExtension extends BaseModelExtension {

     protected static final Log LOG = LogFactory.getLog(CompositeModelExtension.class)
          
     def extensions = {
          LOG.info("Initializing composite extension ${extensionName()} (${templateId()})")
          int c = 0
          modelExtensions().each {
              LOG.info("Initializing extension ${it.extensionName()} (${it.templateId()})")
              it.builder = builder
              c += it.extensions.call()
          }
          return c
     }
     
     CompositeModelExtension() {         
     }
     
     CompositeModelExtension(MetaBuilder builder) {
         super(builder)
     }
     
     List modelExtensions() {
         []
     }
    
    
 }
