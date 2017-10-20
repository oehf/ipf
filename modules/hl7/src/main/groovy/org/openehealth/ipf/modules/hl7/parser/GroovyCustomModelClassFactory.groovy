/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.parser

import ca.uhn.hl7v2.ErrorCode
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.parser.ModelClassFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This ModelClassFactory is used in same way as its superclass, however,
 * it looks for .groovy resources in the classpath rather than .class files.
 * It then compiles these resources during loading. If the resource is not
 * found, it delegates to the wrapped ModelClassFactory.
 * <p>
 * GroovyCustomModelClassFactory is particularly useful together with the 
 * IPF extension mechanism by setting 
 * CustomModelClassFactoryConfigurer#customModelClassFactory to an instance of
 * this class. You can also wrap a normal CustomModelClassFactory to have a 
 * hierarchy of custom Groovy model classes, custom Java model classes, and 
 * default model classes to search in.
 *
 * @author Christian Ohr
 *
 */
class GroovyCustomModelClassFactory extends CustomModelClassFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomModelClassFactory.class)

    private GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader())

    boolean cacheSources = true

    GroovyCustomModelClassFactory() {
        super()
    }

    GroovyCustomModelClassFactory(Map<String, String[]> map) {
        this(new CustomModelClassFactory(), map)
    }

    GroovyCustomModelClassFactory(ModelClassFactory defaultFactory) {
        super(defaultFactory, null)
    }

    GroovyCustomModelClassFactory(ModelClassFactory defaultFactory, Map<String, String[]> map) {
        super(defaultFactory, map)
    }

    // Finds appropriate classes to be compiled and loaded for the given structure/type
    protected Class findClass(String subpackage, String name, String version) {
        if (!Version.supportsVersion(version)) {
            throw new HL7Exception("HL7 version $version is not supported",
                    ErrorCode.UNSUPPORTED_VERSION_ID)
        }
        def classLoaded = null
        def fullyQualifiedName = null
        customModelClasses[version]?.find {
            try {
                def path = it.replaceAll('\\.', '/')
                def sep = path.endsWith('/') ? '' : '/'
                fullyQualifiedName = "/${path}${sep}${subpackage}/${name}.groovy"
                String script = getClass().getResourceAsStream(fullyQualifiedName)?.text
                GroovyCodeSource gcs = new GroovyCodeSource(script, fullyQualifiedName, ".")
                classLoaded = loader.parseClass(gcs, cacheSources)
                LOGGER.debug("Found {} in custom HL7 model definitions", fullyQualifiedName)
            } catch (Exception e) {
                LOGGER.debug("Did not find {} in custom HL7 model definitions", fullyQualifiedName)
            }
        }
        return classLoaded
    }
}
