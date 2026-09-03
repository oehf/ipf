/*
 * Copyright 2023 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd;

import ca.uhn.fhir.context.FhirContext;
import org.openehealth.ipf.commons.ihe.fhir.support.BaseValidator;

/**
 * Validator for MHD transactions.
 *
 * @author Christian Ohr
 * @since 4.8
 */
public class MhdValidator extends BaseValidator {

    /** Classpath location of the MHD 4.2.3 implementation guide package. */
    public static final String MHD_423_PACKAGE_PATH = "classpath:META-INF/profiles/v423/ihe.iti.mhd.tgz";

    /** Classpath location of the MHD 4.2.4 implementation guide package. */
    public static final String MHD_424_PACKAGE_PATH = "classpath:META-INF/profiles/v424/ihe.iti.mhd.tgz";

    /**
     * Classpath location of the implementation guide package this transaction validates against by
     * default. Public so that the audit records of the transaction can be validated against the very
     * same profiles, rather than against a second copy of the path.
     */
    public static final String MHD_PACKAGE_PATH = MHD_424_PACKAGE_PATH;

    /**
     * Creates a validator for the default MHD version, i.e. {@link MhdVersion#v424}. The resources built
     * by the IPF model classes satisfy the 4.2.3 profiles as well, because they carry both the
     * {@code Identifier.use} of MHD 4.2.3 and the {@code Identifier.type} of MHD 4.2.4.
     *
     * @param fhirContext FhirContext
     */
    public MhdValidator(FhirContext fhirContext) {
        super(fhirContext, MHD_PACKAGE_PATH);
    }

    /**
     * Creates a validator for a specific MHD version.
     *
     * @param fhirContext FhirContext
     * @param version MHD version to validate against, either {@link MhdVersion#v423} or {@link MhdVersion#v424}
     */
    public MhdValidator(FhirContext fhirContext, MhdVersion version) {
        super(fhirContext, packagePathFor(version));
    }

    /**
     * @param version MHD version
     * @return classpath location of the implementation guide package of that MHD version
     * @throws IllegalArgumentException if no implementation guide package is shipped for that version
     */
    public static String packagePathFor(MhdVersion version) {
        return switch (version) {
            case v423 -> MHD_423_PACKAGE_PATH;
            case v424 -> MHD_424_PACKAGE_PATH;
            default -> throw new IllegalArgumentException("No MHD implementation guide package available for version " + version);
        };
    }

}
