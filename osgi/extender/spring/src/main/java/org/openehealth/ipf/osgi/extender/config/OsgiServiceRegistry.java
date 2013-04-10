/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.osgi.extender.config;

import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.core.config.SpringRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Map;

/**
 * @author Boris Stanojevic
 */
public class OsgiServiceRegistry extends SpringRegistry {

    private final BundleContext bundleContext;

    public OsgiServiceRegistry(BundleContext context){
        bundleContext = context;
        ContextFacade.setRegistry(this);
    }

    @Override
    public Object bean(String name) {
        ServiceReference sr = bundleContext.getServiceReference(name);
        Object bean = bundleContext.getService(sr);
        if (bean != null){
            return bean;
        }
        return super.bean(name);
    }

    @Override
    public <T> T bean(Class<T> requiredType) {
        ServiceReference sr = bundleContext.getServiceReference(requiredType.getCanonicalName());
        T bean = (T) bundleContext.getService(sr);
        if (bean != null){
            return bean;
        }
        return super.bean(requiredType);
    }

    @Override
    public <T> Map<String, T> beans(Class<T> requiredType) {
        return super.beans(requiredType);
    }
}
