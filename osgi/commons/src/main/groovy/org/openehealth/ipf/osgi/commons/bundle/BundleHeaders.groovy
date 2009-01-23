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
package org.openehealth.ipf.osgi.commons.bundle

import org.osgi.framework.Bundle
/**
 * @author Martin Krasser
 */
public class BundleHeaders {

     public static String EXTENSION_CLASSES_HEADER = 'Extension-Classes'
     
     public static String EXTENSION_BEANS_HEADER = 'Extension-Beans'
     
     static List extensionClasses(Bundle bundle) {
         new BundleHeader(bundle:bundle, name:EXTENSION_CLASSES_HEADER).values
     }
    
     static List extensionBeans(Bundle bundle) {
         new BundleHeader(bundle:bundle, name:EXTENSION_BEANS_HEADER).values
     }
    
}
