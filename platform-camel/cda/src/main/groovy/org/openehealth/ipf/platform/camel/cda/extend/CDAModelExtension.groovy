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
package org.openehealth.ipf.platform.camel.cda.extend

import org.apache.camel.builder.DataFormatClause
import org.openehealth.ipf.platform.camel.core.model.ValidatorAdapterDefinition

/**
 * @author Christian Ohr
 */
public class CDAModelExtension {

     static extensions = { 

         // ----------------------------------------------------------------
         //  Adapter Extensions for DataFormatClause
         // ----------------------------------------------------------------
         
         DataFormatClause.metaClass.cdar2 = { ->
             CDAExtensionModule.cdar2(delegate)
         }         
     
         // ----------------------------------------------------------------
         //  IPF model class extensions
         // ----------------------------------------------------------------
         
         // W3C Schema validation
         
         ValidatorAdapterDefinition.metaClass.cdar2 = {-> 
             CDAExtensionModule.cdar2(delegate)
         }

         // Schematron validation
         
         ValidatorAdapterDefinition.metaClass.ccd = { Map parameters ->
             CDAExtensionModule.ccd(delegate, parameters)
         }
         
         ValidatorAdapterDefinition.metaClass.ccd = {-> 
             CDAExtensionModule.ccd(delegate)
         }
     }
    
}
