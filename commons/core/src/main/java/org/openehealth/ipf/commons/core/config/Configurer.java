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
package org.openehealth.ipf.commons.core.config;

import java.util.Collection;

/**
 * Base interface to implement for any custom configurers.  
 * 
 * @author Boris Stanojevic
 *
 * @deprecated the whole {@code Configurer} SPI only ever expressed "collect all beans of type
 * {@code T} and push them into some holder", which core Spring expresses as
 * {@code beanFactory.getBeanProvider(T.class).orderedStream()}.
 * Holders now collect their own contributions while the singletons are being initialized instead of being filled from the
 * outside once an application context has been refreshed.
 * See
 * {@code org.openehealth.ipf.commons.spring.map.SpringBidiMappingService},
 * {@code org.openehealth.ipf.commons.spring.core.extend.SpringDynamicExtensionRegistrar},
 * {@code org.openehealth.ipf.modules.hl7.config.CustomModelClassesRegistrar} and
 * {@code org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer}.
 */
@Deprecated(since = "6.0.0", forRemoval = true)
public interface Configurer<T, R extends Registry> {

    /**
     * @param configuration custom configuration object
     * used for specific configuration task
     */
    void configure(T configuration) throws Exception;
    
    Collection<T> lookup(R registry);

}
