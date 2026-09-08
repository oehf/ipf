/*
 * Copyright 2026 the original author or authors.
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
import org.openehealth.ipf.modules.hl7.kotlin.parser.CustomModelClassFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.SmartInitializingSingleton

/**
 * Collects the [CustomModelClasses] beans of the application context and adds their package
 * definitions to the [CustomModelClassFactory] instances in use. A single bean declaration is
 * enough:
 *
 * ```
 *     <bean class="org.openehealth.ipf.modules.hl7.kotlin.config.CustomModelClassesRegistrar"/>
 * ```
 *
 * If [customModelClassFactory] is not set, all [CustomModelClassFactory] beans of the
 * application context are configured.
 *
 * Collecting happens in [afterSingletonsInstantiated], i.e. before the `ContextRefreshedEvent`
 * and thus well before any HL7v2 message is parsed.
 *
 * This replaces the deprecated [CustomModelClassFactoryConfigurer].
 *
 * @author Christian Ohr
 * @since 6.0
 */
class CustomModelClassesRegistrar : BeanFactoryAware, SmartInitializingSingleton {

    companion object : KLogging() {

        /**
         * Adds the package definitions of the given contribution to the factory and, if requested,
         * to its chain of [CustomModelClassFactory] delegates. Package definitions that the factory
         * already knows about are skipped, so that a contribution reaching a factory both directly
         * and through a deprecated [CustomModelClassFactoryConfigurer] is only added once.
         */
        @JvmStatic
        fun addModels(factory: CustomModelClassFactory?, contribution: CustomModelClasses,
                      recursively: Boolean) {
            val modelClasses = contribution.modelClasses ?: return
            var current: ModelClassFactory? = factory
            while (current is CustomModelClassFactory) {
                addMissingModels(current, modelClasses)
                if (!recursively) break
                current = current.delegate
            }
            logger.debug { "Custom model classes configured: $contribution" }
        }

        private fun addMissingModels(factory: CustomModelClassFactory,
                                     modelClasses: Map<String, Array<String>>) {
            val known = factory.customModelClasses
            val missing = modelClasses
                    .mapValues { (version, packageNames) ->
                        val knownPackageNames = known?.get(version)
                        if (knownPackageNames == null) packageNames
                        else packageNames.filterNot { knownPackageNames.contains(it) }.toTypedArray()
                    }
                    .filterValues { it.isNotEmpty() }
            if (missing.isNotEmpty()) {
                try {
                    factory.addModels(missing)
                } catch (e: UnsupportedOperationException) {
                    // HAPI's addModels() writes straight into the map the factory was constructed with
                    throw IllegalStateException("Cannot add custom model classes to $factory: its " +
                            "custom model class map is immutable. Construct the CustomModelClassFactory " +
                            "with a mutable map, e.g. HashMap(mapOf(...))", e)
                }
            }
        }
    }

    private var beanFactory: BeanFactory? = null

    /**
     * The factory to be configured. If null, all [CustomModelClassFactory] beans of the
     * application context are configured.
     */
    var customModelClassFactory: CustomModelClassFactory? = null

    /**
     * Whether the delegates of a [CustomModelClassFactory] shall be configured as well.
     */
    var configureRecursively = true

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }

    override fun afterSingletonsInstantiated() {
        val bf = beanFactory ?: return
        val contributions = orderedBeans(bf, CustomModelClasses::class.java)
        if (contributions.isEmpty()) return
        val factories = customModelClassFactory
                ?.let { listOf(it) }
                ?: orderedBeans(bf, CustomModelClassFactory::class.java)
        logger.debug {
            "Adding ${contributions.size} custom model class contributions to ${factories.size} model class factories"
        }
        factories.forEach { factory ->
            contributions.forEach { contribution ->
                addModels(factory, contribution, configureRecursively)
            }
        }
    }

    private fun <T : Any> orderedBeans(beanFactory: BeanFactory, type: Class<T>): List<T> =
            beanFactory.getBeanProvider(type).orderedStream().toList()

}
