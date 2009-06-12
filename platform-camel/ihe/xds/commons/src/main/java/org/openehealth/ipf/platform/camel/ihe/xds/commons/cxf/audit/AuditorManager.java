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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit;

import org.openhealthtools.ihe.atna.auditor.XDSConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRegistryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRepositoryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSSourceAuditor;

/**
 * Access synchronizer for OHT XDS ATNA Auditor singletons.
 */
public class AuditorManager {
    private static final Object sync = new Object();
    
    public static XDSRegistryAuditor getRegistryAuditor() {
        synchronized (sync) {
            return XDSRegistryAuditor.getAuditor();
        }
    }
    
    public static XDSRepositoryAuditor getRepositoryAuditor() {
        synchronized (sync) {
            return XDSRepositoryAuditor.getAuditor();
        }
    }

    public static XDSConsumerAuditor getConsumerAuditor() {
        synchronized (sync) {
            return XDSConsumerAuditor.getAuditor();
        }
    }
    
    public static XDSSourceAuditor getSourceAuditor() {
        synchronized (sync) {
            return XDSSourceAuditor.getAuditor();
        }
    }
}
