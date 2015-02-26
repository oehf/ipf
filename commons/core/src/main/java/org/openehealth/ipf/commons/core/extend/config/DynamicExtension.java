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
package org.openehealth.ipf.commons.core.extend.config;

/**
 * DSL extensions implementing this marker interface can be auto discovered
 * by IPF's extension configuration framework.
 * <p>
 * This is an alternative to the standard Extension Module mechanism provided by Groovy
 * Instead of a module descriptor in META-INF/services you simply define a Spring bean that
 * extends this type and adheres to the structure described in
 * http://docs.codehaus.org/display/GROOVY/Creating+an+extension+module.
 * </p><p>
 * This interface defines method that provide information that the module descriptor
 * would usually deliver. Bean instance of this type are picked up by the
 * {@link org.openehealth.ipf.commons.core.config.SpringConfigurationPostProcessor} when
 * it contains a {@link DynamicExtensionConfigurer} and dynamically registered in
 * Groovy's metaclass/metamethod registry.
 * </p><p>
 * The advantage is that due to late initialization your extensions can be stateful (e.g.
 * providing a configuration object), which is normally only possible by accessing a
 * global registry via {@link org.openehealth.ipf.commons.core.config.ContextFacade#getBean(Class)}.
 * The disadvantage is that you need to define a bunch of Spring beans (including the extension
 * bean) in order to get the machinery working.
 *
 * @see DynamicExtensionConfigurer
 * 
 * @author Christian Ohr
 */
public interface DynamicExtension {

    String getModuleName();
    String getModuleVersion();

    /**
     * @return true if the extensions methods shall have static access from
     * the extended class, false otherwise.
     */
    boolean isStatic();

}