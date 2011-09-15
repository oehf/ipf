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

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;

/**
 * Validation profile for XDS-like transactions.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class ValidationProfileImpl implements ValidationProfile {
    private InteractionId interactionId;


    /**
     * Constructor.
     * @param interactionId
     *          ID of the eHealth transaction.
     */
    public ValidationProfileImpl(InteractionId interactionId) {
        this.interactionId = interactionId;
    }


    @Override
    public boolean isQuery() {
        return ((interactionId == ITI_16) ||
                (interactionId == ITI_18) ||
                (interactionId == ITI_38));
    }


    @Override
    public InteractionId getInteractionId() {
        return interactionId;
    }


    @Override
    public InteractionProfile getProfile() {
        if (interactionId == Continua_HRN) {
            return InteractionProfile.Continua_HRN;
        }

        if ((interactionId == ITI_14) ||
            (interactionId == ITI_15) ||
            (interactionId == ITI_16))
        {
            return InteractionProfile.XDS_A;
        }

        if ((interactionId == ITI_38) || (interactionId == ITI_39)) {
            return InteractionProfile.XCA;
        }

        if ((interactionId == ITI_18) ||
            (interactionId == ITI_41) ||
            (interactionId == ITI_42) ||
            (interactionId == ITI_43))
        {
            return InteractionProfile.XDS_B;
        }

        throw new IllegalArgumentException("Unknown interaction ID: " + interactionId);
    }


    @Override
    public boolean isEbXml30Based() {
        InteractionProfile profile = getProfile();
        return ((profile == InteractionProfile.XDS_B) ||
                (profile == InteractionProfile.XCA) ||
                (profile == InteractionProfile.Continua_HRN));
    }
}
