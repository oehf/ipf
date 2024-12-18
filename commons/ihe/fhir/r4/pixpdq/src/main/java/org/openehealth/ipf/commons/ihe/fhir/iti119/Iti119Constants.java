/*
 * Copyright 2024 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti119;

import org.openehealth.ipf.commons.ihe.fhir.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Christian Ohr
 * @since 5.0
 */
public interface Iti119Constants {

    String RESOURCE = "resource";
    String ONLY_CERTAIN_MATCHES = "onlyCertainMatches";
    String COUNT = "count";

    Set<String> ITI119_PARAMETERS = new HashSet<>(Arrays.asList(
        ONLY_CERTAIN_MATCHES,
        COUNT));

    String PDQM_MATCH_OPERATION_NAME = "$match";
}
