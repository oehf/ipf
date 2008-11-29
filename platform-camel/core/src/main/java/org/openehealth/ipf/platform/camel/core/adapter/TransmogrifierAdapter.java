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
package org.openehealth.ipf.platform.camel.core.adapter;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.prepareResult;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;

/**
 * Adapts a {@link Transmogrifier}. 
 * 
 * @author Martin Krasser
 */
public class TransmogrifierAdapter extends ProcessorAdapter {

    private Transmogrifier transmogrifier;

    /**
     * Creates a new {@link TransmogrifierAdapter} and sets the delegate
     * {@link Transmogrifier}.
     * 
     * @param transmogrifier
     *            a transmogrifier.
     */
    public TransmogrifierAdapter(Transmogrifier transmogrifier) {
        this.transmogrifier = transmogrifier;
    }
    
    /**
     * Delegates processing of input data and input parameters to a
     * {@link Transmogrifier} object. The transmogrification result is written
     * to body of the message returned by
     * {@link org.openehealth.ipf.platform.camel.core.util.Exchanges#prepareResult(Exchange)}.
     * 
     * @param exchange
     *            message exchange where to write processing results.
     * @param inputData
     *            input data.
     * @param inputParams
     *            input parameters.
     * @throws Exception
     *             if a processing error occurs.
     */
    @Override
    protected void doProcess(Exchange exchange, Object inputData, 
            Object... inputParams) throws Exception {

        prepareResult(exchange).setBody(transmogrifier.zap(inputData, inputParams));
    }

}
