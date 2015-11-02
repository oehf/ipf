/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.map;

import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 *
 */
public class SpringBidiMappingService extends BidiMappingService {

    // bean configuration support
    public void setMappingScript(Resource resource) {
        try {
            setMappingScript(resource.getURL());
        } catch (IOException e) {
            if (!isIgnoreResourceNotFound())
                throw new IllegalArgumentException(resource.getFilename() + " could not be read", e);
        }
    }

    // bean configuration support
    public void setMappingScripts(Resource[] resources) {
        for (Resource resource : resources) {
            setMappingScript(resource);
        }
    }

    // Read in the mapping definition
    public synchronized void addMappingScript(Resource resource) {
        try {
            addMappingScript(resource.getURL());
        } catch (IOException e) {
            if (!isIgnoreResourceNotFound())
                throw new IllegalArgumentException(resource.getFilename() + " could not be read", e);
        }
    }

    // Read in several mapping definition
    public synchronized void addMappingScripts(Resource[] resources) {
        for (Resource resource : resources) {
            addMappingScript(resource);
        }
    }

}
