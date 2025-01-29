
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for maritalDataRestrictedUndoMarriedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maritalDataRestrictedUndoMarriedType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0011/8}maritalDataType">
 *       &lt;sequence>
 *         &lt;element name="maritalStatus">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.ech.ch/xmlns/eCH-0011/8}maritalStatusType">
 *               &lt;enumeration value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dateOfMaritalStatus" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maritalDataRestrictedUndoMarriedType")
public class MaritalDataRestrictedUndoMarriedType
    extends MaritalDataType
{


}
