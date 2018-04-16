package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dmytro Rud
 */
@XmlType(name = "CX", namespace = "http://swisscom.com/hlt/asd/xuagen")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CX {

    private String id;
    private String assigningAuthorityId;

    @XmlAttribute(required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(required = true)
    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    public String toHl7String() {
        return id + "^^^&" + assigningAuthorityId + "&ISO";
    }

    @Override
    public String toString() {
        return "CX{" +
                "id='" + id + '\'' +
                ", assigningAuthorityId='" + assigningAuthorityId + '\'' +
                '}';
    }
}
