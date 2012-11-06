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
package org.openehealth.ipf.commons.core.extend;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Martin Krasser
 */
@Deprecated
public class DefaultActivator implements ExtensionActivator {

    private final List<ConditionalActivator> activators;
    
    public DefaultActivator() {
        activators = new ArrayList<ConditionalActivator>();
        activators.add(new ExtensionBlockActivator());
        activators.add(new ExtensionMethodActivator());
    }
    
    @Override
    public void activate(Class<?> extensionClass) {
        boolean supported = false;
        for (ConditionalActivator activator : activators) {
            if (activator.supports(extensionClass)) {
                activator.activate(extensionClass);
                supported = true;
            }
        }
        if (!supported) {
            throw new ExtensionActivationException("Class " + extensionClass + 
                    " doesn't contain valid extension definitions");
        }
    }

    @Override
    public void activate(Object extensionObject) {
        boolean supported = false;
        for (ConditionalActivator activator : activators) {
            if (activator.supports(extensionObject)) {
                activator.activate(extensionObject);
            }
            supported = true;
        }
        if (!supported) {
            throw new ExtensionActivationException("Object " + extensionObject + 
                    " doesn't contain valid extension definitions");
        }
    }

}
