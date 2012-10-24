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
package org.openehealth.ipf.commons.map.config;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * This class should be used to define the custom mappings
 * in the spring context definition.
 * 
 * <pre class="code"> 
 *    &lt;!-- either as a list of mapping definitions --&gt;
 *    &lt;bean id="customMapping3" 
 *        class="org.openehealth.ipf.commons.map.config.CustomMappings"&gt;
 *        &lt;property name="mappingScripts"&gt;
 *            &lt;list&gt;
 *                &lt;value&gt;classpath:configurer1.map&lt;/value&gt;
 *                &lt;value&gt;classpath:configurer2.map&lt;/value&gt;
 *            &lt;/list&gt;
 *        &lt;/property&gt;
 *    &lt;/bean&gt;
 *    
 *    &lt;!-- or as a single mapping definition --&gt;
 *    &lt;bean id="customMappingSingle" 
 *        class="org.openehealth.ipf.commons.map.config.CustomMappings"&gt;
 *        &lt;property name="mappingScript" value="classpath:configurer3.map" /&gt;
 *    &lt;/bean&gt;</pre>
 * 
 * @see CustomMappingsConfigurer
 * @author Christian Ohr
 * @author Boris Stanojevic
 * 
 */
public class CustomMappings {

    private Collection<Resource> mappingScripts;

    private Resource mappingScript;

    private static final Logger LOG = LoggerFactory.getLogger(CustomMappings.class);

    public Collection<Resource> getMappingScripts() {
        return mappingScripts;
    }

    public void setMappingScripts(Collection<Resource> mappingScripts) {
        this.mappingScripts = new ArrayList<Resource>(mappingScripts.size());
        for (Resource mappingScript : mappingScripts) {
            if (mappingScript.exists() && mappingScript.isReadable()) {
                this.mappingScripts.add(mappingScript);
            } else {
                LOG.warn("Could not read mapping script "
                        + mappingScript.getFilename());
            }
        }
        this.mappingScripts = mappingScripts;
    }

    public Resource getMappingScript() {
        return mappingScript;
    }

    public void setMappingScript(Resource mappingScript) {
        if (mappingScript.exists() && mappingScript.isReadable()) {
            this.mappingScript = mappingScript;
        } else {
            LOG.warn("Could not read mapping script "
                    + mappingScript.getFilename());
        }
    }

}
