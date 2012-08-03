/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.cda

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.eclipse.emf.ecore.xmi.XMLResource
import org.junit.Assert
import org.junit.BeforeClass
import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.eclipse.emf.ecore.resource.URIConverter
import org.eclipse.emf.common.util.URI

/**
 * @author Christian Ohr
 */
public abstract class AbstractCDAR2Test {
    
    private static final Log LOG = LogFactory.getLog(AbstractCDAR2Test.getClass().getName())
    
    static def registered = []

    // Workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=364797
    static {
        URIConverter.URI_MAP.put(
            URI.createURI("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore"),
            URI.createPlatformPluginURI("/org.eclipse.ocl.ecore/model/oclstdlib.ecore", true));
    }
    
    @BeforeClass
    public static void setupBeforeClass() {
        ExpandoMetaClass.enableGlobally()
        new CDAR2ModelExtension().register(registered)
    }
    
    static void shouldFail(Closure c) {
        shouldFail(Exception.class, c)
    }
    
    static void shouldFail(Class<Exception> clazz, Closure c) {
        boolean failed = false
        try {
            c.call()
        } catch (Exception e) {
            if (clazz.isInstance(e)) {
                failed = true
            }
        }
        Assert.assertTrue("Closure should have failed throwing ${clazz.name}", failed)
    }
    
    void showDocument(def document){
        CDAR2Renderer renderer = new CDAR2Renderer()
        def opts = [:]
        opts[XMLResource.OPTION_DECLARE_XML] = true
        opts[XMLResource.OPTION_ENCODING] = 'utf-8'
        LOG.debug(renderer.render(document, opts))
    }
}