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
package org.openehealth.ipf.commons.ihe.atna;

import org.openhealthtools.ihe.atna.auditor.PDQConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXManagerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRegistryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSRepositoryAuditor;
import org.openhealthtools.ihe.atna.auditor.XDSSourceAuditor;

/**
 * Access synchronizer for OHT XDS ATNA Auditor singletons.
 */
public class AuditorManager {
    private static final Object sync = new Object();
    
    private AuditorManager() {
        throw new IllegalStateException("Static helper class cannot be instantiated");
    }
    
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
            XDSConsumerAuditor auditor = XDSConsumerAuditor.getAuditor();
            
            // for ITI-16 and ITI-17
            AuditorModuleConfig config = auditor.getConfig(); 
            if(config.getSystemUserId() == null) {
                try {
                    config.setSystemUserId(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    config.setSystemUserId("unknown");
                }
            }
            return auditor; 
        }
    }
    
    public static XDSSourceAuditor getSourceAuditor() {
        synchronized (sync) {
            return XDSSourceAuditor.getAuditor();
        }
    }
    
    public static PIXManagerAuditor getPIXManagerAuditor() {
        synchronized (sync) {
            return PIXManagerAuditor.getAuditor();
        }
    }
    
    public static PIXSourceAuditor getPIXSourceAuditor() {
        synchronized (sync) {
            return PIXSourceAuditor.getAuditor();
        }
    }
    
    public static PIXConsumerAuditor getPIXConsumerAuditor() {
        synchronized (sync) {
            return PIXConsumerAuditor.getAuditor();
        }
    }

    public static PDQConsumerAuditor getPDQConsumerAuditor() {
        synchronized (sync) {
            return PDQConsumerAuditor.getAuditor();
        }
    }
}
