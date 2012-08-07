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

import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;

import java.util.*;

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;

/**
 * Validation profile for XDS-like transactions.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 * @author Michael Ottati
 */
public class ValidationProfile {

    public static enum InteractionProfile {
        XDS_A(ITI_14, ITI_15, ITI_16),
        XDS_B(ITI_18, ITI_41, ITI_42, ITI_43, ITI_51, ITI_61, RAD_69),
        XCA(ITI_38, ITI_39, RAD_75),
        XCF(ITI_63),
        Continua_HRN(IpfInteractionId.Continua_HRN);

        private List<InteractionId> ids;

        InteractionProfile(InteractionId... ids) {
            this.ids = Arrays.asList(ids);
        }

        public List<InteractionId> getIds() {
            return ids;
        }
    }

    private InteractionId interactionId;


    /**
     * Constructor.
     * @param interactionId
     *          ID of the eHealth transaction.
     */
    public ValidationProfile(InteractionId interactionId) {
        this.interactionId = interactionId;
    }


    /**
     * @return <code>true</code> if checks are done for query transactions.
     */
    public boolean isQuery() {
        return ((interactionId == ITI_16) ||
                (interactionId == ITI_18) ||
                (interactionId == ITI_38) ||
                (interactionId == ITI_51) ||
                (interactionId == ITI_63));
    }


    /**
     * @return ID of the eHealth transaction.
     */
    public InteractionId getInteractionId() {
        return interactionId;
    }


    /**
     * @return ID of interaction profile the transaction belongs to.
     */
    public InteractionProfile getProfile() {
        for (InteractionProfile profile : InteractionProfile.values()) {
            if (profile.getIds().contains(getInteractionId())) {
                return profile;
            }
        }
        throw new IllegalArgumentException("Unknown interaction ID: " + interactionId);
    }


    /**
     * @return <code>true</code> when the transaction uses ebXML 3.0.
     */
    public boolean isEbXml30Based() {
        InteractionProfile profile = getProfile();
        return ((profile == InteractionProfile.XDS_B) ||
                (profile == InteractionProfile.XCA) ||
                (profile == InteractionProfile.Continua_HRN) ||
                (profile == InteractionProfile.XCF));
    }

}
