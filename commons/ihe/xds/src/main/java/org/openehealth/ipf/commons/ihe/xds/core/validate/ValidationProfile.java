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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

/**
 * Validation profile allowing the definition of actor specific validation
 * settings.
 * @author Jens Riemschneider
 */
public class ValidationProfile {
    private boolean query;
    private IheProfile iheProfile;
    private Actor actor;
    
    /**
     * Constructs a profile.
     */
    public ValidationProfile() {}


    /**
     * Constructs a profile.
     * @param query
     *          <code>true</code> if checks are done for query transactions.
     * @param iheProfile
     *          IHE profile which rules should be applied.
     * @param actor
     *          defines the actor that validates messages.
     */
    public ValidationProfile(boolean query, IheProfile iheProfile, Actor actor) {
        this.query = query;
        this.iheProfile = iheProfile;
        this.actor = actor;
    }


    /**
     * Copy constructor.
     * @param profile
     *          profile to copy.
     */
    public ValidationProfile(ValidationProfile profile) {
        if (profile != null) {
            query = profile.query;
            iheProfile = profile.iheProfile;
            actor = profile.actor;
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
     * @return IHE Profile which rules should be applied.
     */
    public IheProfile getIheProfile() {
        return iheProfile;
    }

    /**
     * @param iheProfile
     *          IHE Profile which rules should be applied.
     */
    public void setIheProfile(IheProfile iheProfile) {
        this.iheProfile = iheProfile;
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

    public boolean isXdsb() {throw new RuntimeException(); };
}
