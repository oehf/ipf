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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.spi.RouteContext;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.adapter.TransmogrifierAdapter;

/**
 * @author Martin Krasser
 */
public class TransmogrifierAdapterType extends ProcessorAdapterType {

    private Transmogrifier transmogrifier;
    
    private String transmogrifierBean;
    
    public TransmogrifierAdapterType(Transmogrifier transmogrifier) {
        this.transmogrifier = transmogrifier;
        this.params().headers();
    }

    public TransmogrifierAdapterType(String transmogrifierBean) {
        this.transmogrifierBean = transmogrifierBean;
        this.params().headers();
    }

    @Override
    public String toString() {
        return "TransmogrifierAdapter[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "transmogrifierAdapter";
    }

    @Override
    protected ProcessorAdapter doCreateProcessor(RouteContext routeContext) {
        if (transmogrifierBean != null) {
            transmogrifier = routeContext.lookup(transmogrifierBean, Transmogrifier.class);
        }
        return new TransmogrifierAdapter(transmogrifier);
    }

}
