package org.openehealth.ipf.commons.ihe.fhir.iti119;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;

public abstract class AdditionalResourceMetadataKeyEnum {

    public static final ResourceMetadataKeyEnum<BundleEntryTransactionMethodEnum> SEARCH_SCORE =
        new ResourceMetadataKeyEnum<>("SEARCH_SCORE", DecimalDt.class) {};

}
