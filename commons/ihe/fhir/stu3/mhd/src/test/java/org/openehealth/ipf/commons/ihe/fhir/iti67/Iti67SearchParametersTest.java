/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.iti67;

import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Ohr
 */
public class Iti67SearchParametersTest {

    @Test
    public void setAuthor() {
        var searchParameters = Iti67SearchParameters.builder().build();

        var param = new ReferenceAndListParam()
                .addAnd(new ReferenceOrListParam()
                        .addOr(new ReferenceParam(Practitioner.SP_FAMILY, "family")))
                .addAnd(new ReferenceOrListParam()
                        .addOr(new ReferenceParam(Practitioner.SP_GIVEN, "given")));
        searchParameters.setAuthor(param);
        assertEquals("family", searchParameters.getAuthorFamilyName().getValue());
        assertEquals("given", searchParameters.getAuthorGivenName().getValue());
    }
}
