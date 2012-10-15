/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.core.extend.config;

import groovy.lang.ExpandoMetaClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.config.SpringConfigurer;
import org.openehealth.ipf.commons.core.extend.DefaultActivator;
import org.openehealth.ipf.commons.core.extend.ExtensionActivator;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * Configurer used to autowire all classes implementing
 * the {@link Extension} interface.
 * 
 * @author Boris Stanojevic
 */
public class ExtensionConfigurer extends SpringConfigurer<Extension>{

    private static final Log LOG = LogFactory.getLog(ExtensionConfigurer.class);
    
    private ExtensionActivator extensionActivator;

    static {
        ExpandoMetaClass.enableGlobally();
    }

    public ExtensionConfigurer(){
        this.extensionActivator = new DefaultActivator();
        setOrder(2);
    }

    @Override
    public void configure(Extension configuration) {
        extensionActivator.activate(configuration);
        LOG.debug("Extension configured..." + configuration);
    }

    @Override
    public Collection<Extension> lookup(ListableBeanFactory source) {
        List<Extension> list = new ArrayList<Extension>(
                BeanFactoryUtils.beansOfTypeIncludingAncestors(source,
                        Extension.class).values());
        return list;        
    }

}
