/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pdqm320;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

/**
 * Parameters resource for ITI-119 Patient Demographics Match Input as defined by the PDQm specification.
 * This Parameters resource contains the input patient demographics for the $match operation,
 * including the resource parameter containing a Patient resource and optional onlyCertainMatches
 * and count parameters.
 *
 * @author Christian Ohr
 * @since 5.2
 */
@ResourceDef(name = "Parameters", profile = PdqmProfile.ITI119_MATCH_INPUT_PARAMETERS_PROFILE)
public class PdqmMatchInputParameters extends Parameters implements Pdqm320 {

    public static final String RESOURCE_PARAM = "resource";
    public static final String ONLY_CERTAIN_MATCHES_PARAM = "onlyCertainMatches";
    public static final String COUNT_PARAM = "count";

    public PdqmMatchInputParameters() {
        super();
        PdqmProfile.ITI119_MATCH_INPUT_PARAMETERS.setProfile(this);
    }

    @Override
    public PdqmMatchInputParameters copy() {
        var dst = new PdqmMatchInputParameters();
        copyValues(dst);
        return dst;
    }

    /**
     * Convenience method to add the resource parameter containing the Patient resource
     *
     * @param patient the Patient resource to match against
     * @return this Parameters instance for method chaining
     */
    public PdqmMatchInputParameters setResourceParameter(PdqmMatchInputPatient patient) {
        addParameter()
            .setName(RESOURCE_PARAM)
            .setResource(patient);
        return this;
    }

    /**
     * Convenience method to set the onlyCertainMatches parameter
     *
     * @param onlyCertainMatches if true, only return matches with high certainty
     * @return this Parameters instance for method chaining
     */
    public PdqmMatchInputParameters setOnlyCertainMatches(boolean onlyCertainMatches) {
        addParameter()
            .setName(ONLY_CERTAIN_MATCHES_PARAM)
            .setValue(new BooleanType(onlyCertainMatches));
        return this;
    }

    /**
     * Convenience method to set the count parameter
     *
     * @param count maximum number of matches to return
     * @return this Parameters instance for method chaining
     */
    public PdqmMatchInputParameters setCount(int count) {
        addParameter()
            .setName(COUNT_PARAM)
            .setValue(new IntegerType(count));
        return this;
    }
}