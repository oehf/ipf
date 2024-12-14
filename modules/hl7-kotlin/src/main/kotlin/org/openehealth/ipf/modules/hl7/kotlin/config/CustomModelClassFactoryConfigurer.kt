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

import ca.uhn.hl7v2.parser.ModelClassFactory
import io.github.oshai.kotlinlogging.KLogging
import org.openehealth.ipf.commons.core.config.OrderedConfigurer
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.modules.hl7.kotlin.parser.CustomModelClassFactory

/**
 * @author Christian Ohr
 */
class CustomModelClassFactoryConfigurer<R : Registry> : OrderedConfigurer<CustomModelClasses, R>() {

    companion object: KLogging()

    var customModelClassFactory: CustomModelClassFactory? = null
    var configureRecursively = true

    override fun lookup(registry: R): Collection<CustomModelClasses> {
        return registry.beans(CustomModelClasses::class.java).values
    }

    override fun configure(configuration: CustomModelClasses) {
        // update the top ModelClassFactory
        var delegateFactory = configureAndDelegate(customModelClassFactory, configuration)
        // delegate if required
        while (configureRecursively && delegateFactory is CustomModelClassFactory) {
            val currentFactory = delegateFactory
            delegateFactory = configureAndDelegate(currentFactory, configuration)
        }
        logger.debug {"Custom model classes configured: $configuration"}
    }

    private fun configureAndDelegate(factory: CustomModelClassFactory?,
                                     configuration: CustomModelClasses): ModelClassFactory? {
        factory?.addModels(configuration.modelClasses)
        return factory?.delegate
    }


}