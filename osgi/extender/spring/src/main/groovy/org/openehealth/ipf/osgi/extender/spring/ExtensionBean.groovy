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
package org.openehealth.ipf.osgi.extender.spring

import org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactory
import org.openehealth.ipf.osgi.commons.bundle.BundleHeaders

import org.osgi.framework.Bundle
import org.springframework.beans.factory.BeanFactory

/**
 * @author Martin Krasser
 */
public class ExtensionBean {

     static Log LOG = LogFactory.getLog(ExtensionBean.class)
    
     BeanFactory  factory

     String name   
     
     void activate() {
         def bean = factory.getBean(name)
         if (!bean) {
             LOG.warn("Extension bean ${this} not defined")
         }
         bean.extensions.call()
         LOG.info("Extension bean ${this} activated")
     }

     String toString() {
         name
     }
     
     static List<ExtensionBean> defineAll(Bundle bundle, BeanFactory beanFactory) {
         BundleHeaders.extensionBeans(bundle).collect {
             new ExtensionBean(name:it, factory:beanFactory)
         }
     }
     
     static void activateAll(Bundle bundle, BeanFactory beanFactory) {
         defineAll(bundle, beanFactory).each { it.activate() }
     }
     
}
