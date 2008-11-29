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
package org.openehealth.ipf.platform.camel.core.extend

/**
 * Activates route model extensions that have been injected as 
 * routeModelExtensions list. Each element in the list is an object that
 * defines an instance closure named 'extensions'. For example:
 * 
 * <pre>
 * class MyExtension {
 *     def extensions = {
 *         ProcessorType.metaClass.myCoolNewDslElement = { String customArg ->
 *             // Do something with customArg and ProcessorType (delegate)
 *             // ...
 *             return delegate 
 *         }
 *     }
 * }
 * </pre>
 * 
 * This extension can then be used within route definitions like
 *
 * <pre>
 * class MyConfig implements RouteBuilderConfig {
 *     void apply(RouteBuilder routeBuilder) {
 *         builder
 *             .from('direct:input')
 *             .myCoolNewDslElement('blah')
 *             .to('direct:output')
 *     }
 * }
 * </pre>
 * 
 * @author Martin Krasser
 */
class DefaultModelExtender implements RouteModelExtender {
    
    List routeModelExtensions
    
    static {
        ExpandoMetaClass.enableGlobally()
    }
    
    void activate() {
        routeModelExtensions.each { extension ->
            extension.extensions.call()
        }
    }
    
}