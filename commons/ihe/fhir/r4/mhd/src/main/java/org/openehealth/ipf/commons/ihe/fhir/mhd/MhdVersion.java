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

/**
 * The versions of the MHD supplement IPF knows about. Note that this is a version of the
 * <em>profiles</em>, not of the IPF model classes: the resources built by those conform to more than
 * one MHD version, e.g. they carry both the {@code Identifier.use} of MHD 4.2.3 and the
 * {@code Identifier.type} of MHD 4.2.4 (CP-ITI-1328-01).
 *
 * @see MhdValidator#packagePathFor(MhdVersion)
 */
public enum MhdVersion {

    v320, v423, v424
}
