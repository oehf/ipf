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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.AssigningAuthorityAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
@XmlType(name = "ReferenceId", propOrder = {"idTypeCode", "homeCommunityId"})
public class ReferenceId extends Identifiable {
    private static final long serialVersionUID = 6615092850652668283L;

    public static final String ID_TYPE_CODE_UNIQUE_ID = "urn:ihe:iti:xds:2013:uniqueId";
    public static final String ID_TYPE_CODE_ACCESSION = "urn:ihe:iti:xds:2013:accession";
    public static final String ID_TYPE_CODE_REFERRAL  = "urn:ihe:iti:xds:2013:referral";
    public static final String ID_TYPE_CODE_ORDER     = "urn:ihe:iti:xds:2013:order";
    public static final String ID_TYPE_WORKFLOW_ID    = "urn:ihe:iti:xdw:2013:workflowId";
    public static final String ID_TYPE_ENCOUNTER_ID   = "urn:ihe:iti:xds:2015:encounterId";

    public ReferenceId() {
        super();
    }

    public ReferenceId(CX cx) {
        super(cx);
    }

    public ReferenceId(
            String id,
            AssigningAuthority assigningAuthority,
            String idTypeCode,
            AssigningAuthority homeCommunityId)
    {
        super(id, assigningAuthority);
        setIdTypeCode(idTypeCode);
        setHomeCommunityId(homeCommunityId);
    }

    /**
     * @return ID type code (CX.5).
     */
    @XmlAttribute(name = "idTypeCode")
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

    /**
     * @return home community ID (CX.6).
     */
    @XmlAttribute(name = "homeCommunityId")
    @XmlJavaTypeAdapter(value = AssigningAuthorityAdapter.class)
    public AssigningAuthority getHomeCommunityId() {
        AssigningAuthority assigningAuthority = new AssigningAuthority(getHapiObject().getCx6_AssigningFacility());
        return assigningAuthority.isEmpty() ? null : assigningAuthority;
    }

    /**
     * @param homeCommunityId
     *          home community ID (CX.6).
     */
    public void setHomeCommunityId(AssigningAuthority homeCommunityId) {
        setAssigningAuthority(homeCommunityId, getHapiObject().getCx6_AssigningFacility());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ReferenceId that = (ReferenceId) o;

        if (getHomeCommunityId() != null ? !getHomeCommunityId().equals(that.getHomeCommunityId()) : that.getHomeCommunityId() != null)
            return false;
        if (getIdTypeCode() != null ? !getIdTypeCode().equals(that.getIdTypeCode()) : that.getIdTypeCode() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getIdTypeCode() != null ? getIdTypeCode().hashCode() : 0);
        result = 31 * result + (getHomeCommunityId() != null ? getHomeCommunityId().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", getId())
                .append("assigningAuthority", getAssigningAuthority())
                .append("idTypeCode", getIdTypeCode())
                .append("homeCommunityId", getHomeCommunityId())
                .toString();
    }
}
