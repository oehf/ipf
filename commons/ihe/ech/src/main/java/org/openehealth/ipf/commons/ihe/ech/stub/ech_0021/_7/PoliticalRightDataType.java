
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0021._7;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for politicalRightDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="politicalRightDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="restrictedVotingAndElectionRightFederation" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "politicalRightDataType", propOrder = {
    "restrictedVotingAndElectionRightFederation"
})
public class PoliticalRightDataType {

    protected Boolean restrictedVotingAndElectionRightFederation;

    /**
     * Gets the value of the restrictedVotingAndElectionRightFederation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRestrictedVotingAndElectionRightFederation() {
        return restrictedVotingAndElectionRightFederation;
    }

    /**
     * Sets the value of the restrictedVotingAndElectionRightFederation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRestrictedVotingAndElectionRightFederation(Boolean value) {
        this.restrictedVotingAndElectionRightFederation = value;
    }

}
