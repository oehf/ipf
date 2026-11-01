/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02OtherIDsScopingOrganization;
import net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02QueryByParameter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.toIi;
import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.toSt;

/**
 * Transformer between objects {@link PdqV3QueryRequest} and {@link PRPAIN201305UV02Type} (PDQv3, ITI-47).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class PdqV3QueryRequestTransformer extends AbstractPatientDemographicsQueryRequestTransformer<PdqV3QueryRequest> {

    @Override
    protected PdqV3QueryRequest newSimpleQuery() {
        return new PdqV3QueryRequest();
    }

    @Override
    protected void toQueryByParameterSpecific(final PdqV3QueryRequest simpleQuery,
                                              final PRPAMT201306UV02QueryByParameter queryByParameter) {
        if (simpleQuery.getInitialQuantity() != null) {
            queryByParameter.setInitialQuantity(new INT(simpleQuery.getInitialQuantity()));
        }
        for (final var oid : simpleQuery.getOtherIDsScopingOrganizationOids()) {
            final var scopingOrganization = new PRPAMT201306UV02OtherIDsScopingOrganization();
            scopingOrganization.setValue(List.of(toIi(oid)));
            scopingOrganization.setSemanticsText(toSt("OtherIDs.scopingOrganization.id"));
            queryByParameter.getParameterList().getOtherIDsScopingOrganization().add(scopingOrganization);
        }
    }

    @Override
    protected void fromQueryByParameterSpecific(final PRPAMT201306UV02QueryByParameter queryByParameter,
                                                final PdqV3QueryRequest simpleQuery) {
        if (queryByParameter.getInitialQuantity() != null) {
            simpleQuery.setInitialQuantity(queryByParameter.getInitialQuantity().getValue());
        }
        if (queryByParameter.getParameterList() != null) {
            queryByParameter.getParameterList().getOtherIDsScopingOrganization().stream()
                    .flatMap(parameter -> parameter.getValue().stream())
                    .map(II::getRoot)
                    .forEach(simpleQuery.getOtherIDsScopingOrganizationOids()::add);
        }
    }
}
