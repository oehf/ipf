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
package org.openehealth.ipf.osgi.extender.basic

import static java.lang.reflect.Modifier.STATIC

import org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactory
import org.openehealth.ipf.osgi.commons.bundle.BundleHeaders

import org.osgi.framework.Bundle
/**
 * @author Martin Krasser
 */
public class ExtensionClass {

    static Log LOG = LogFactory.getLog(ExtensionClass.class)
    
     static {
         ExpandoMetaClass.enableGlobally()
     }
     
     Class<?> target  
     
     void activate() {
         def prop = target.metaClass.getMetaProperty('extensions')
         
         if (!prop) {
             LOG.error("class ${target} has no \"extensions\" closure defined")
             return
         }
         
         try {
             if (prop.modifiers & STATIC) {
                 target.extensions.call()
             } else {
                 target.newInstance().extensions.call()
             }
             
         } catch (Exception e) {
             LOG.error("Extension class ${this} could not be activated", e)
         }
         LOG.info("Extension class ${this} activated")
     }

     String toString() {
         target.toString()
     }
     
     static List<ExtensionClass> loadAll(Bundle bundle) {
         BundleHeaders.extensionClasses(bundle).collect {
             new ExtensionClass(target:bundle.loadClass(it))
         }
     }
     
     static void activateAll(Bundle bundle) {
         loadAll(bundle).each { it.activate() }
     }
     
     private static activateExtension
}
