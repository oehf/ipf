/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import ca.uhn.hl7v2.model.v25.datatype.CX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * Represents a reference ID.
 * <p>
 * This class is derived from an HL7v2 CX data type ("CXi" in IHE ITI TF-3).
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are
 * removed from the HL7 string.
 *
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "ReferenceId", propOrder = {"id", "assigningAuthority", "idTypeCode"})
public class ReferenceId extends Hl7v2Based<CX> {
    private static final long serialVersionUID = 6615092850652668283L;

    public static final String ID_TYPE_CODE_UNIQUE_ID       = "urn:ihe:iti:xds:2013:uniqueId";
    public static final String ID_TYPE_CODE_ACCESSION       = "urn:ihe:iti:xds:2013:accession";
    public static final String ID_TYPE_CODE_REFERRAL        = "urn:ihe:iti:xds:2013:referral";
    public static final String ID_TYPE_CODE_ORDER           = "urn:ihe:iti:xds:2013:order";
    public static final String ID_TYPE_WORKFLOW_INSTANCE_ID = "urn:ihe:iti:xdw:2013:workflowInstanceId";
    public static final String ID_TYPE_ENCOUNTER_ID         = "urn:ihe:iti:xds:2015:encounterId";
    public static final String ID_TYPE_STUDY_INSTANCE_ID    = "urn:ihe:iti:xds:2016:studyInstanceUID";

    /**
     * Constructs a reference ID.
     */
    public ReferenceId() {
        super(new CX(MESSAGE));
    }


    /**
     * Constructs a reference ID.
     */
    public ReferenceId(CX cx) {
        super(cx);
    }


    /**
     * Constructs a reference ID.
     * @param id
     *          the value of the id (CX.1).
     * @param assigningAuthority
     *          the assigning authority (CX.4).
     * @param idTypeCode
     *          the ID type code (CX.5).
     */
    public ReferenceId(String id, CXiAssigningAuthority assigningAuthority, String idTypeCode) {
        this();
        setId(id);
        setAssigningAuthority(assigningAuthority);
        setIdTypeCode(idTypeCode);
    }

    /**
     * @return the value of the id (CX.1).
     */
    @XmlAttribute
    public String getId() {
        return getHapiObject().getCx1_IDNumber().getValue();
    }

    /**
     * @param id
     *          the value of the id (CX.1).
     */
    public void setId(String id) {
        setValue(getHapiObject().getCx1_IDNumber(), id);
    }

    /**
     * @return the assigning authority (CX.4).
     */
    public CXiAssigningAuthority getAssigningAuthority() {
        CXiAssigningAuthority assigningAuthority = new CXiAssigningAuthority(getHapiObject().getCx4_AssigningAuthority());
        return assigningAuthority.isEmpty() ? null : assigningAuthority;
    }

    /**
     * @param assigningAuthority
     *          the assigning authority (CX.4).
     */
    public void setAssigningAuthority(CXiAssigningAuthority assigningAuthority) {
        setAssigningAuthority(assigningAuthority, getHapiObject().getCx4_AssigningAuthority());
    }

    /**
     * @return ID type code (CX.5).
     */
    @XmlAttribute
    public String getIdTypeCode() {
        return getHapiObject().getCx5_IdentifierTypeCode().getValue();
    }

    /**
     * @param idTypeCode
     *          ID type code (CX.5).
     */
    public void setIdTypeCode(String idTypeCode) {
        setValue(getHapiObject().getCx5_IdentifierTypeCode(), idTypeCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceId that = (ReferenceId) o;
        return Objects.equals(getAssigningAuthority(), that.getAssigningAuthority()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getIdTypeCode(), that.getIdTypeCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIdTypeCode(), getAssigningAuthority());
    }

    @Override
    public String toString() {
        return "ReferenceId(" +
                "id=" + getId() +
                ", assigningAuthority=" + getAssigningAuthority() +
                ", idTypeCode=" + getIdTypeCode() +
                ')';
    }
}
