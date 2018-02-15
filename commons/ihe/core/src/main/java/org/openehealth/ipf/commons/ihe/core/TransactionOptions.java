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

package org.openehealth.ipf.commons.ihe.core;

import java.io.Serializable;
import java.util.List;

/**
 * Some IHE transactions define options that influence the interface(s) being exposed by their interactions, e.g.:
 * <ul>
 *     <li>PAM defines options for ITI-30 and ITI-31 that determines the acceptable HL7 trigger events</li>
 *     <li>QEDm defines options that determine what FHIR resource types needs to be provided</li>
 * </ul>
 */
public interface TransactionOptions<T> extends Serializable {

    List<T> getSupportedThings();

}
