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
package org.openehealth.ipf.osgi.extender.config;

import java.util.Collection;

import org.openehealth.ipf.commons.core.config.Configurer;
import org.openehealth.ipf.commons.core.config.Registry;
import org.osgi.framework.BundleContext;

/**
 * Base class for all custom configurers which have to implement their own
 * strategy for lookup and configure inside of the OSGi environment.
 * 
 * @author Boris Stanojevic
 */
public abstract class OsgiSpringConfigurer<T, R extends Registry> implements
        Configurer<T, R> {

    public abstract Collection<T> lookup(BundleContext bc, R registry);

}
