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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate;

/**
 * Validation profile allowing the definition of actor specific validation
 * settings.
 * @author Jens Riemschneider
 */
public class ValidationProfile {
    private boolean query;
    
    /**
     * Constructs a profile.
     */
    public ValidationProfile() {}

    /**
     * Copy constructor.
     * @param profile
     *          profile to copy.
     */
    public ValidationProfile(ValidationProfile profile) {
        if (profile != null) {
            this.query = profile.query;
        }
    }

    /**
     * @return <code>true</code> if this checks are done for query transactions.
     */
    public boolean isQuery() {
        return query;
    }

    /**
     * @param query
     *          <code>true</code> if checks are done for query transactions. 
     */
    public void setQuery(boolean query) {
        this.query = query;
    }
}
