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
    
    static final String IHE_LAB_SCHEMA = "schema/ihe_lab/infrastructure/cda/LabCDA.xsd";

    /** @deprecated use HITSP_32_2_4_SCHEMA **/
    static final String HITSP_24_SCHEMA = "schema/hitspc32_v2.4_20090414/infrastructure/cda/C32_CDA.xsd";

    static final String HITSP_32_2_4_SCHEMA = "schema/hitspc32_v2.4_20090414/infrastructure/cda/C32_CDA.xsd";
    static final String HITSP_32_2_5_SCHEMA = "schema/hitspc32_v2.5_20101007/infrastructure/cda/C32_CDA.xsd";

    static final String CCDA_SCHEMA = "schema/ccda/infrastructure/cda/CDA_SDTC.xsd";

    // Schematron

    static final String CCD_SCHEMATRON_RULES = "schematron/ccd/ccd.sch";
    
    static final String CDA_PHMR_SCHEMATRON_RULES = "schematron/cda_phmr/PHMR.sch";

    /** @deprecated use HITSP_32_2_4_SCHEMATRON_RULES **/
    static final String HITSP_24_SCHEMATRON_RULES = "schematron/hitsp32_v2.4_20090414/HITSP_C32.sch";

    static final String HITSP_32_2_4_SCHEMATRON_RULES = "schematron/hitsp32_v2.4_20090414/HITSP_C32.sch";
    static final String HITSP_32_2_5_SCHEMATRON_RULES = "schematron/hitspc32_v2.5_20101007/HITSP_C32.sch";

    static final String HITSP_37_SCHEMATRON_RULES = "schematron/hitspc37_20080211/HITSP_C37.sch";

    static final String CDA4CDT_SCHEMATRON_RULES = "schematron/cda4cdt_20090206/HandP.sch";

    static final String IHE_BPPC_SCHEMATRON_RULES = "schematron/ihe_bppc_20090518/BPPC.sch";
    
    static final String IHE_LAB_SCHEMATRON_RULES = "schematron/ihe_lab_v21_20080803/IHE_LAB.sch";

    static final String IHE_LAB_20_SCHEMATRON_RULES = "schematron/ihe_lab_v20_20070816/IHE_LAB.sch";

    static final String IHE_LAB_21_SCHEMATRON_RULES = "schematron/ihe_lab_v21_20080803/IHE_LAB.sch";

    static final String IHE_PCC_ANTEPARTUM_EDUCATION_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/ape.sch";

    static final String IHE_PCC_ANTEPARTUM_H_AND_P_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/aphp.sch";

    static final String IHE_PCC_ANTEPARTUM_SUMMARY_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/aps.sch";

    static final String IHE_PCC_ED_COMPOSITE_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/edComposite.sch";

    static final String IHE_PCC_ED_NURSING_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/edNursingNote.sch";

    static final String IHE_PCC_ED_PHYSICIAN_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/edPhysicianNote.sch";

    static final String IHE_PCC_ED_REFERRAL_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/edReferral.sch";

    static final String IHE_PCC_ED_TRIAGE_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/edTriageNote.sch";

    static final String IHE_PCC_FSA_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/fsa.sch";

    static final String IHE_PCC_IMMUNIZATION_DETAIL_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/immunizationDetail.sch";

    static final String IHE_PCC_XDSMS_DISCHARGE_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/xdsMsDischarge.sch";

    static final String IHE_PCC_XDSMS_REFERRAL_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/xdsMsReferral.sch";

    static final String IHE_PCC_XPHR_EXTRACT_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/xphrExtract.sch";

    static final String IHE_PCC_XPHR_UPDATE_SCHEMATRON_RULES = "schematron/ihe_pcc_20081223/xphrUpdate.sch";

    static final String IHE_QRPH_CRD_SCHEMATRON_RULES = "schematron/ihe_qrph_20090206/crd.sch";

    static final String CCDA_SCHEMATRON_RULES = "schematron/ccda/Consolidation.sch";

}
