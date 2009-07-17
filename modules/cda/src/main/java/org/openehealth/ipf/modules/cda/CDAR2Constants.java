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
package org.openehealth.ipf.modules.cda;

/**
 * @author Christian Ohr
 */
public interface CDAR2Constants {

    // W3C XML Schema

    static final String CDAR2_SCHEMA = "schema/cdar2/infrastructure/cda/CDA.xsd";

    static final String HITSP_24_SCHEMA = "schema/itspc32_v2.4_20090414/infrastructure/cda/C32_CDA.xsd";

    // Schematron

    static final String CCD_SCHEMATRON_RULES = "schematron/ccd/ccd.sch";

    static final String HITSP_24_SCHEMATRON_RULES = "schematron/hitspc32_v2.4_20090414/HITSP_C32.sch";

    static final String CDA4CDT_SCHEMATRON_RULES = "schematron/cda4cdt_20090206/HandP.sch";

    static final String IHE_BPPC_SCHEMATRON_RULES = "schematron/cihe_bppc_20090518/BPPC.sch";

    // ...
}
