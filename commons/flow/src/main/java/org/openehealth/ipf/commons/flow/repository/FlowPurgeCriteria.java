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
package org.openehealth.ipf.commons.flow.repository;

import java.util.Date;

/**
 * Criteria for flow purge operations.
 * 
 * @author Martin Krasser
 */
public class FlowPurgeCriteria {

    public static final int DEFAULT_MAX_PURGE_COUNT = -1;

    public static enum PurgeMode {

        /**
         * Purge all flows.
         */
        ALL,
        
        /**
         * Purge clean flows only.
         */
        CLEAN
        
    }
    
    private PurgeMode purgeMode;

    private Date timeLimit;
    
    private String application;

    private int maxPurgeCount;
    
    /**
     * Creeates a new {@link FlowPurgeCriteria} object. Does not limit the
     * number of flow to be purged.
     * 
     * @param purgeMode
     *            purge mode.
     * @param timeLimit
     *            flow older than this time limit will be purged.
     * @param application
     *            application name.
     */
    public FlowPurgeCriteria(PurgeMode purgeMode, Date timeLimit, String application) {
        this(purgeMode, timeLimit, application, DEFAULT_MAX_PURGE_COUNT);
    }

    /**
     * Creeates a new {@link FlowPurgeCriteria} object.
     * 
     * @param purgeMode
     *            purge mode.
     * @param timeLimit
     *            flow older than this time limit will be purged.
     * @param application
     *            application name.
     * @param purgeCount
     *            maximum number of flows to purge or -1 to purge all flows that
     *            match the other criteria.
     */
    public FlowPurgeCriteria(PurgeMode purgeMode, Date timeLimit, String application, int purgeCount) {
        this.purgeMode = purgeMode;
        this.timeLimit = timeLimit;
        this.application = application;
        this.maxPurgeCount = purgeCount;
    }
    
    public PurgeMode getPurgeMode() {
        return purgeMode;
    }
    
    public Date getTimeLimit() {
        return timeLimit;
    }
    
    public String getApplication() {
        return application;
    }
    
    public int getMaxPurgeCount() {
        return maxPurgeCount;
    }
    
}
