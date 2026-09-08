/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin.config

import org.openehealth.ipf.commons.core.config.OrderedConfigurer
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.modules.hl7.kotlin.parser.CustomModelClassFactory

/**
 * @author Christian Ohr
 *
 * @deprecated declare a [CustomModelClassesRegistrar] instead, which collects the
 * [CustomModelClasses] beans of the application context itself and therefore needs neither this
 * configurer nor a `SpringConfigurationPostProcessor`:
 * ```
 *     <bean class="org.openehealth.ipf.modules.hl7.kotlin.config.CustomModelClassesRegistrar"/>
 * ```
 * Both mechanisms may be active at the same time: [CustomModelClassesRegistrar.addModels] skips
 * package definitions the factory already knows about.
 */
@Deprecated("Declare a CustomModelClassesRegistrar bean instead",
        ReplaceWith("CustomModelClassesRegistrar"))
class CustomModelClassFactoryConfigurer<R : Registry> : OrderedConfigurer<CustomModelClasses, R>() {

    var customModelClassFactory: CustomModelClassFactory? = null
    var configureRecursively = true

    override fun lookup(registry: R): Collection<CustomModelClasses> {
        return registry.beans(CustomModelClasses::class.java).values
    }

    override fun configure(configuration: CustomModelClasses) {
        CustomModelClassesRegistrar.addModels(customModelClassFactory, configuration, configureRecursively)
    }

}