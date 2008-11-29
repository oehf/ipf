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
package org.openehealth.ipf.commons.flow.repository;

import java.util.Date;

/**
 * @author Martin Krasser
 */
public class FlowFinderCriteria {

    public static final int DEFAULT_MAX_RESULTS = -1;
    
    private String application;
    private Date from;
    private Date to;
    private int maxResults;

    public FlowFinderCriteria(Date from, Date to, String application) {
        this(from, to, application, DEFAULT_MAX_RESULTS);
    }
    
    public FlowFinderCriteria(Date from, Date to, String application, int maxResults) {
        this.from = from;
        this.to = to;
        this.application = application;
        this.maxResults = maxResults;
    }
    
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
    
    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    
}
