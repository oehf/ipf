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
package org.openehealth.ipf.tutorials.osgi.service.impl

import javax.annotation.PostConstruct
import org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
/**
 * @author Martin Krasser
 */
public class AdmissionTransmogrifier  implements Transmogrifier {

    static Log LOG = LogFactory.getLog(AdmissionTransmogrifier.class)
     
    String sendingFacility = 'UNK'
     
    Object zap(Object msg, Object[] params) {
        msg.MSH[4] = sendingFacility
        msg.PID[8] = msg.PID[8].mapGender()
        msg
    }
    
    @PostConstruct
    void init() {
        LOG.debug("Use ${sendingFacility} for sending facility")
    }
    
}
