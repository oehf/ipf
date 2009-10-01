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
package org.openehealth.ipf.osgi.extender.spring;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.extend.DefaultActivator;
import org.openehealth.ipf.commons.core.extend.ExtensionActivator;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.osgi.extender.OsgiBeanFactoryPostProcessor;

/**
 * @author Martin Krasser
 */
public class ExtensionPostProcessor implements OsgiBeanFactoryPostProcessor {

    private static final Log LOG = LogFactory.getLog(ExtensionPostProcessor.class);
    
    private ExtensionActivator extensionActivator;
    
    public ExtensionPostProcessor() {
        extensionActivator = new DefaultActivator();
    }
    
    @Override
    public void postProcessBeanFactory(BundleContext bundleContext, ConfigurableListableBeanFactory beanFactory) {
        activateExtensionBeans(ExtensionBeans.loadAll(bundleContext.getBundle(), beanFactory));
    }

    private void activateExtensionBeans(List<Object> beans) {
        for (Object bean : beans) {
            try {
                extensionActivator.activate(bean);
                LOG.info("Activated extension bean " + bean);
            } catch (Exception e) {
                LOG.error("Couldn't activate extension bean " + bean, e);
            }
        }
    }
    
}
