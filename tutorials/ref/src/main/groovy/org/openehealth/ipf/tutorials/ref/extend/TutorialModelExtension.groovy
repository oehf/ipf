/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.tutorials.ref.extend

import org.apache.camel.model.language.ExpressionDefinition

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE
import static org.openehealth.ipf.tutorials.ref.util.Expressions.filenameExpression

import org.apache.camel.model.ProcessorDefinition

/**
 * @author Martin Krasser
 */
public class TutorialModelExtension {

        // ------------------------------------------------------------
        //  Flow management extensions
        // ------------------------------------------------------------
        
        public static ProcessorDefinition initFlow (ProcessorDefinition delegate,
                                                    String identifier,
                                                    String errorUri){
            delegate.initFlow(identifier)
                .replayErrorHandler(errorUri)
                .application('tutorial')
                .inType(String.class)
                .outType(String.class)
                .outConversion(false)
        }
        
        // ------------------------------------------------------------
        //  File endpoint extensions
        // ------------------------------------------------------------

        public static ProcessorDefinition toFile (ProcessorDefinition delegate,
                                                  String dir,
                                                  String filePrefix,
                                                  String fileExtension){
            def header = 'CamelFileName'
            def expression = filenameExpression(filePrefix, fileExtension) 
            delegate.setHeader(header, expression).to('file:' + dir);
        }
        
        // ------------------------------------------------------------
        //  HTTP endpoint extensions
        // ------------------------------------------------------------
        
        public static ExpressionDefinition responseCode (ProcessorDefinition delegate){
            delegate.setHeader(HTTP_RESPONSE_CODE)
        }

}
