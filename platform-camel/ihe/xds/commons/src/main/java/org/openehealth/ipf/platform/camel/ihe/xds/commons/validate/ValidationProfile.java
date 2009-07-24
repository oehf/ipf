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
    private boolean xdsb;
    private Actor actor;
    
    /**
     * Constructs a profile.
     */
    public ValidationProfile() {}


    /**
     * Constructs a profile.
     * @param query
     *          <code>true</code> if checks are done for query transactions.
     * @param xdsb
     *          <code>true</code> if XDS.b is validated.
     * @param actor
     *          defines the actor that validates messages.
     */
    public ValidationProfile(boolean query, boolean xdsb, Actor actor) {
        this.query = query;
        this.xdsb = xdsb;
        this.actor = actor;
    }


    /**
     * Copy constructor.
     * @param profile
     *          profile to copy.
     */
    public ValidationProfile(ValidationProfile profile) {
        if (profile != null) {
            this.query = profile.query;
            this.xdsb = profile.xdsb;
            this.actor = profile.actor;
        }
    }

    /**
     * @return <code>true</code> if checks are done for query transactions.
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

    /**
     * @return <code>true</code> if XDS.b is validated.
     */
    public boolean isXdsb() {
        return xdsb;
    }

    /**
     * @param xdsb
     *          <code>true</code> if XDS.b is validated.
     */
    public void setXdsb(boolean xdsb) {
        this.xdsb = xdsb;
    }

    /**
     * @return defines the actor that validates messages.
     */
    public Actor getActor() {
        return actor;
    }

    /**
     * @param actor
     *          defines the actor that validates messages.
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
