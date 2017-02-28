
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LDAPResultCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LDAPResultCode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="success"/&gt;
 *     &lt;enumeration value="operationsError"/&gt;
 *     &lt;enumeration value="protocolError"/&gt;
 *     &lt;enumeration value="timeLimitExceeded"/&gt;
 *     &lt;enumeration value="sizeLimitExceeded"/&gt;
 *     &lt;enumeration value="compareFalse"/&gt;
 *     &lt;enumeration value="compareTrue"/&gt;
 *     &lt;enumeration value="authMethodNotSupported"/&gt;
 *     &lt;enumeration value="strongAuthRequired"/&gt;
 *     &lt;enumeration value="referral"/&gt;
 *     &lt;enumeration value="adminLimitExceeded"/&gt;
 *     &lt;enumeration value="unavailableCriticalExtension"/&gt;
 *     &lt;enumeration value="confidentialityRequired"/&gt;
 *     &lt;enumeration value="saslBindInProgress"/&gt;
 *     &lt;enumeration value="noSuchAttribute"/&gt;
 *     &lt;enumeration value="undefinedAttributeType"/&gt;
 *     &lt;enumeration value="inappropriateMatching"/&gt;
 *     &lt;enumeration value="constraintViolation"/&gt;
 *     &lt;enumeration value="attributeOrValueExists"/&gt;
 *     &lt;enumeration value="invalidAttributeSyntax"/&gt;
 *     &lt;enumeration value="noSuchObject"/&gt;
 *     &lt;enumeration value="aliasProblem"/&gt;
 *     &lt;enumeration value="invalidDNSyntax"/&gt;
 *     &lt;enumeration value="aliasDerefencingProblem"/&gt;
 *     &lt;enumeration value="inappropriateAuthentication"/&gt;
 *     &lt;enumeration value="invalidCredentials"/&gt;
 *     &lt;enumeration value="insufficientAccessRights"/&gt;
 *     &lt;enumeration value="busy"/&gt;
 *     &lt;enumeration value="unavailable"/&gt;
 *     &lt;enumeration value="unwillingToPerform"/&gt;
 *     &lt;enumeration value="loopDetect"/&gt;
 *     &lt;enumeration value="namingViolation"/&gt;
 *     &lt;enumeration value="objectClassViolation"/&gt;
 *     &lt;enumeration value="notAllowedOnNonLeaf"/&gt;
 *     &lt;enumeration value="notAllowedOnRDN"/&gt;
 *     &lt;enumeration value="entryAlreadyExists"/&gt;
 *     &lt;enumeration value="objectClassModsProhibited"/&gt;
 *     &lt;enumeration value="affectMultipleDSAs"/&gt;
 *     &lt;enumeration value="other"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "LDAPResultCode")
@XmlEnum
public enum LDAPResultCode {

    @XmlEnumValue("success")
    SUCCESS("success"),
    @XmlEnumValue("operationsError")
    OPERATIONS_ERROR("operationsError"),
    @XmlEnumValue("protocolError")
    PROTOCOL_ERROR("protocolError"),
    @XmlEnumValue("timeLimitExceeded")
    TIME_LIMIT_EXCEEDED("timeLimitExceeded"),
    @XmlEnumValue("sizeLimitExceeded")
    SIZE_LIMIT_EXCEEDED("sizeLimitExceeded"),
    @XmlEnumValue("compareFalse")
    COMPARE_FALSE("compareFalse"),
    @XmlEnumValue("compareTrue")
    COMPARE_TRUE("compareTrue"),
    @XmlEnumValue("authMethodNotSupported")
    AUTH_METHOD_NOT_SUPPORTED("authMethodNotSupported"),
    @XmlEnumValue("strongAuthRequired")
    STRONG_AUTH_REQUIRED("strongAuthRequired"),
    @XmlEnumValue("referral")
    REFERRAL("referral"),
    @XmlEnumValue("adminLimitExceeded")
    ADMIN_LIMIT_EXCEEDED("adminLimitExceeded"),
    @XmlEnumValue("unavailableCriticalExtension")
    UNAVAILABLE_CRITICAL_EXTENSION("unavailableCriticalExtension"),
    @XmlEnumValue("confidentialityRequired")
    CONFIDENTIALITY_REQUIRED("confidentialityRequired"),
    @XmlEnumValue("saslBindInProgress")
    SASL_BIND_IN_PROGRESS("saslBindInProgress"),
    @XmlEnumValue("noSuchAttribute")
    NO_SUCH_ATTRIBUTE("noSuchAttribute"),
    @XmlEnumValue("undefinedAttributeType")
    UNDEFINED_ATTRIBUTE_TYPE("undefinedAttributeType"),
    @XmlEnumValue("inappropriateMatching")
    INAPPROPRIATE_MATCHING("inappropriateMatching"),
    @XmlEnumValue("constraintViolation")
    CONSTRAINT_VIOLATION("constraintViolation"),
    @XmlEnumValue("attributeOrValueExists")
    ATTRIBUTE_OR_VALUE_EXISTS("attributeOrValueExists"),
    @XmlEnumValue("invalidAttributeSyntax")
    INVALID_ATTRIBUTE_SYNTAX("invalidAttributeSyntax"),
    @XmlEnumValue("noSuchObject")
    NO_SUCH_OBJECT("noSuchObject"),
    @XmlEnumValue("aliasProblem")
    ALIAS_PROBLEM("aliasProblem"),
    @XmlEnumValue("invalidDNSyntax")
    INVALID_DN_SYNTAX("invalidDNSyntax"),
    @XmlEnumValue("aliasDerefencingProblem")
    ALIAS_DEREFENCING_PROBLEM("aliasDerefencingProblem"),
    @XmlEnumValue("inappropriateAuthentication")
    INAPPROPRIATE_AUTHENTICATION("inappropriateAuthentication"),
    @XmlEnumValue("invalidCredentials")
    INVALID_CREDENTIALS("invalidCredentials"),
    @XmlEnumValue("insufficientAccessRights")
    INSUFFICIENT_ACCESS_RIGHTS("insufficientAccessRights"),
    @XmlEnumValue("busy")
    BUSY("busy"),
    @XmlEnumValue("unavailable")
    UNAVAILABLE("unavailable"),
    @XmlEnumValue("unwillingToPerform")
    UNWILLING_TO_PERFORM("unwillingToPerform"),
    @XmlEnumValue("loopDetect")
    LOOP_DETECT("loopDetect"),
    @XmlEnumValue("namingViolation")
    NAMING_VIOLATION("namingViolation"),
    @XmlEnumValue("objectClassViolation")
    OBJECT_CLASS_VIOLATION("objectClassViolation"),
    @XmlEnumValue("notAllowedOnNonLeaf")
    NOT_ALLOWED_ON_NON_LEAF("notAllowedOnNonLeaf"),
    @XmlEnumValue("notAllowedOnRDN")
    NOT_ALLOWED_ON_RDN("notAllowedOnRDN"),
    @XmlEnumValue("entryAlreadyExists")
    ENTRY_ALREADY_EXISTS("entryAlreadyExists"),
    @XmlEnumValue("objectClassModsProhibited")
    OBJECT_CLASS_MODS_PROHIBITED("objectClassModsProhibited"),
    @XmlEnumValue("affectMultipleDSAs")
    AFFECT_MULTIPLE_DS_AS("affectMultipleDSAs"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    LDAPResultCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LDAPResultCode fromValue(String v) {
        for (LDAPResultCode c: LDAPResultCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
