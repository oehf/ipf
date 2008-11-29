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
package org.openehealth.ipf.commons.flow.config;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Martin Krasser
 */
@Entity
@Table(name = "T_APPLICATION_CONFIG", schema = "PLATFORM")
public class ApplicationConfig {

    public static final boolean FLOW_FILTER_ENABLED_DEFAULT = true;
    
    public static final boolean FLOW_CLEANUP_ENABLED_DEFAULT = false;
    
    @Id
    @Column(name="C_APPLICATION")
    private String application;
    
    @Column(name="C_FLOW_FILTER_ENABLED")
    private boolean flowFilterEnabled;

    @Column(name="C_FLOW_CLEANUP_ENABLED")
    private boolean flowCleanupEnabled;

    public ApplicationConfig() {
        flowFilterEnabled = FLOW_FILTER_ENABLED_DEFAULT;
        flowCleanupEnabled = FLOW_CLEANUP_ENABLED_DEFAULT;
    }
    
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public boolean isFlowFilterEnabled() {
        return flowFilterEnabled;
    }

    public void setFlowFilterEnabled(boolean flowFilterEnabled) {
        this.flowFilterEnabled = flowFilterEnabled;
    }

    public boolean isFlowCleanupEnabled() {
        return flowCleanupEnabled;
    }

    public void setFlowCleanupEnabled(boolean flowCleanupEnabled) {
        this.flowCleanupEnabled = flowCleanupEnabled;
    }

}
