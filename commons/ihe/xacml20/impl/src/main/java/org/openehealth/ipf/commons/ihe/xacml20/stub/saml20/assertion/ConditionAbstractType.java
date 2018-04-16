
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.delegation.DelegationRestrictionType;


/**
 * <p>Java class for ConditionAbstractType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionAbstractType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConditionAbstractType")
@XmlSeeAlso({
    AudienceRestrictionType.class,
    OneTimeUseType.class,
    ProxyRestrictionType.class,
    DelegationRestrictionType.class
})
public abstract class ConditionAbstractType {


}
