/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.pharm5;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Pharm5SearchParameters}.
 *
 * @author Quentin Ligier
 **/
class Pharm5SearchParametersTest {

    @Test
    void testToParameters() {
        final var fhirContext = FhirContext.forR4();

        final var searchParameters = new Pharm5SearchParameters(
                new TokenParam("urn:oid:1.2.3", "1234"),
                new DateRangeParam(
                        new DateParam(ParamPrefixEnum.GREATERTHAN, "2022-01-01"),
                        new DateParam(ParamPrefixEnum.LESSTHAN, "2022-03-31")
                ),
                new StringParam("Doe"),
                new StringParam("John"),
                new TokenParam("abc"),
                new TokenOrListParam(null, "current"),
                new TokenOrListParam("urn:oid:1.2.9", "setting1", "setting2"),
                new DateRangeParam(
                        new DateParam(ParamPrefixEnum.GREATERTHAN, "2022-02-02"),
                        new DateParam(ParamPrefixEnum.LESSTHAN, "2022-02-04")
                ),
                new TokenOrListParam(null, "fa"),
                new TokenOrListParam(null, "e"),
                new TokenOrListParam(null, "s"),
                new TokenOrListParam(null, "fo"),
                Pharm5Operations.FIND_MEDICATION_ADMINISTRATIONS,
                new SortSpec("date", SortOrderEnum.ASC, new SortSpec("status", SortOrderEnum.DESC)),
                Set.of(new Include("Provenance:target:DocumentReference", true)),
                fhirContext
        );

        assertEquals(
                "patient.identifier=urn%3Aoid%3A1.2.3%7C1234&date=gt2022-01-01&date=lt2022-03-31&author.family=Doe&author.given=John&identifier=abc&status=current&setting=urn%3Aoid%3A1.2.9%7Csetting1%2Curn%3Aoid%3A1.2.9%7Csetting2&period=gt2022-02-02&period=lt2022-02-04&facility=fa&event=e&security-label=s&format=fo&_sort=date%2C-status&_include%3Aiterate=Provenance%3Atarget%3ADocumentReference",
                this.toQueryString(searchParameters.toParameters()));
    }

    private String toQueryString(final Parameters parameters) {
        final var b = new StringBuilder();
        boolean first = true;
        for (final var param : parameters.getParameter()) {
            if (!first) {
                b.append('&');
            }
            b.append(UrlUtil.escapeUrlParam(param.getName()));
            b.append('=');
            b.append(UrlUtil.escapeUrlParam(param.getValue().primitiveValue()));
            first = false;
        }
        return b.toString();
    }
}