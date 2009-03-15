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
package org.openehealth.ipf.platform.camel.core.extend

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilder/**
 * Extends a RouteBuilderConfig object with a <code>context</code> property.
 * RouteBuilderConfig implementations can lookup Spring beans from that
 * property. For example
 * 
 * <pre>
 * transmogrify(context.myTransmogrifier)
 * </pre>
 * 
 * looks up a bean named <code>myTransmogrifier</code> from the Spring
 * application context. The transmogrifier object (bean) is then passed
 * as argument to the <code>transmogrify()</code> method.
 * 
 * @author Martin Krasser
 */
class DefaultConfigExtender {

    RouteBuilder routeBuilder
     
    /**
     * Adds a <code>context</code> property to the given <code>config</code>
     * object. 
     */
    void defineContextProperty(RouteBuilderConfig config) {
        def emc = new ExpandoMetaClass(config.class, false)
        def ctx = new Context(applicationContext:routeBuilder.applicationContext)
        
        // Capitalize first letter of context property name
        def prop = routeBuilder.contextPropertyName.replaceAll('^.') { it ? it[0].toUpperCase() : it }
        
        emc."get${prop}" << { -> ctx }
        emc.initialize()
        
        if (config instanceof GroovyObject) {
            config.metaClass = emc
        } else {
            new Proxy().wrap(config).metaClass = emc
        }
    }
    
}

 /**
  * Wrapper for a Spring application context.
  * 
  * @author Martin Krasser
  */
class Context {

    def applicationContext
    
    def get(String bean) {
        applicationContext.getBean(bean)
    }
    
}
