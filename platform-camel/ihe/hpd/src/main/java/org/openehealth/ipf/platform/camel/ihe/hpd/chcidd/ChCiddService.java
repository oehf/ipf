/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd.chcidd;

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.hpd.chcidd.ChCiddPortType;
import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.chcidd.DownloadResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

@Slf4j
public class ChCiddService extends AbstractWebService implements ChCiddPortType {

    @Override
    public DownloadResponse communityDownloadRequest(DownloadRequest request) {
        var result = process(request);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("{} service failed", getClass().getSimpleName(), exception);
            throw new RuntimeException(exception);
        }
        return result.getMessage().getBody(DownloadResponse.class);
    }

}
