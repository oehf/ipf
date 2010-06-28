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
package org.openehealth.ipf.modules.hl7.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.core.config.SpringConfigurer;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * 
 * Configurer used to configure all {@link CustomModelClasses} 
 * bean occurrences in the spring application context. Provides
 * the logic to add the new package definitions after the
 * existing ones.
 * 
 * @author Boris Stanojevic
 * 
 */
public class CustomModelClassFactoryConfigurer extends SpringConfigurer<CustomModelClasses> {

    private CustomModelClassFactory customModelClassFactory;
    
    private static final Log LOG = LogFactory.getLog(CustomModelClassFactoryConfigurer.class);
    
    @SuppressWarnings("unchecked")
    @Override
    public Collection<CustomModelClasses> lookup(ListableBeanFactory source) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(source,
                CustomModelClasses.class).values();
    }

    @Override
    public void configure(CustomModelClasses configuration) {
        Map<String, String[]> existingModelClasses = customModelClassFactory.getCustomModelClasses();
        
        if (existingModelClasses == null) {
            existingModelClasses = new HashMap<String, String[]>();
            customModelClassFactory.setCustomModelClasses(existingModelClasses);
        }
        for (String version : configuration.getModelClasses().keySet()) {
            if (existingModelClasses.containsKey(version)) {
                // the new packages must be added after the existing ones.
                String[] existingPackageNames = existingModelClasses.get(version);
                String[] newPackageNames = configuration.getModelClasses().get(version);
                String[] allPackageNames = new String[existingPackageNames.length
                        + newPackageNames.length];
                System.arraycopy(existingPackageNames, 0, allPackageNames, 0,
                        existingPackageNames.length);
                System.arraycopy(newPackageNames, 0, allPackageNames,
                        existingPackageNames.length, newPackageNames.length);
                existingModelClasses.put(version, allPackageNames);
            } else {
                existingModelClasses.put(version, configuration
                        .getModelClasses().get(version));
            }
        } 
        LOG.debug("Custom model classes configured: " + configuration);
    }

    public CustomModelClassFactory getCustomModelClassFactory() {
        return customModelClassFactory;
    }

    public void setCustomModelClassFactory(
            CustomModelClassFactory customModelClassFactory) {
        this.customModelClassFactory = customModelClassFactory;
    }

}
