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
package org.openehealth.ipf.commons.map

import org.springframework.beans.factory.InitializingBeanimport org.springframework.core.io.Resource
/**
 * Adds mapping scripts to a reference BidiMappingService instance. 
 * 
 * @author Martin Krasser
 */
class BidiMappingServiceConfigurer implements InitializingBean {

    BidiMappingService mappingService
    
    Resource[] mappingScripts
    
    Resource mappingScript
    
    void afterPropertiesSet() {
        if (mappingScripts) {
            mappingService.addMappingScripts(mappingScripts)
        }
        if (mappingScript) {
            mappingService.addMappingScript(mappingScript)
        }
    }
}
