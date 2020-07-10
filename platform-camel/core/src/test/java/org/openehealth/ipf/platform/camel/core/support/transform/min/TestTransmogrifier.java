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
package org.openehealth.ipf.platform.camel.core.support.transform.min;

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.platform.camel.core.support.domain.Cat;
import org.openehealth.ipf.platform.camel.core.support.domain.Dog;

/**
 * @author Martin Krasser
 */
public class TestTransmogrifier implements Transmogrifier<Dog, Cat>{

    public Cat zap(Dog object, Object... params) {
        return new Cat(object.getName() + appendix(params));
    }

    private static String appendix(Object... params) {
        if (params == null) {
            return "";
        }
        var buffer = new StringBuilder();
        for (var param : params) {
            buffer.append(param);
        }
        return buffer.toString();
    }
    
}
