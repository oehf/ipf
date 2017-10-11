
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for SearchRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="filter" type="{urn:oasis:names:tc:DSML:2:0:core}Filter"/&gt;
 *         &lt;element name="attributes" type="{urn:oasis:names:tc:DSML:2:0:core}AttributeDescriptions" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dn" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" /&gt;
 *       &lt;attribute name="scope" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="baseObject"/&gt;
 *             &lt;enumeration value="singleLevel"/&gt;
 *             &lt;enumeration value="wholeSubtree"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="derefAliases" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="neverDerefAliases"/&gt;
 *             &lt;enumeration value="derefInSearching"/&gt;
 *             &lt;enumeration value="derefFindingBaseObj"/&gt;
 *             &lt;enumeration value="derefAlways"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="sizeLimit" type="{urn:oasis:names:tc:DSML:2:0:core}MAXINT" default="0" /&gt;
 *       &lt;attribute name="timeLimit" type="{urn:oasis:names:tc:DSML:2:0:core}MAXINT" default="0" /&gt;
 *       &lt;attribute name="typesOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchRequest", propOrder = {
    "filter",
    "attributes"
})
public class SearchRequest
    extends DsmlMessage
{

    @XmlElement(required = true)
    protected Filter filter;
    protected AttributeDescriptions attributes;
    @XmlAttribute(name = "dn", required = true)
    protected String dn;
    @XmlAttribute(name = "scope", required = true)
    protected SearchRequest.SearchScope scope;
    @XmlAttribute(name = "derefAliases", required = true)
    protected SearchRequest.DerefAliasesType derefAliases;
    @XmlAttribute(name = "sizeLimit")
    protected Long sizeLimit;
    @XmlAttribute(name = "timeLimit")
    protected Long timeLimit;
    @XmlAttribute(name = "typesOnly")
    protected Boolean typesOnly;

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link Filter }
     *     
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Filter }
     *     
     */
    public void setFilter(Filter value) {
        this.filter = value;
    }

    /**
     * Gets the value of the attributes property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeDescriptions }
     *     
     */
    public AttributeDescriptions getAttributes() {
        return attributes;
    }

    /**
     * Sets the value of the attributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeDescriptions }
     *     
     */
    public void setAttributes(AttributeDescriptions value) {
        this.attributes = value;
    }

    /**
     * Gets the value of the dn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDn() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDn(String value) {
        this.dn = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link SearchRequest.SearchScope }
     *     
     */
    public SearchRequest.SearchScope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchRequest.SearchScope }
     *     
     */
    public void setScope(SearchRequest.SearchScope value) {
        this.scope = value;
    }

    /**
     * Gets the value of the derefAliases property.
     * 
     * @return
     *     possible object is
     *     {@link SearchRequest.DerefAliasesType }
     *     
     */
    public SearchRequest.DerefAliasesType getDerefAliases() {
        return derefAliases;
    }

    /**
     * Sets the value of the derefAliases property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchRequest.DerefAliasesType }
     *     
     */
    public void setDerefAliases(SearchRequest.DerefAliasesType value) {
        this.derefAliases = value;
    }

    /**
     * Gets the value of the sizeLimit property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getSizeLimit() {
        if (sizeLimit == null) {
            return  0L;
        } else {
            return sizeLimit;
        }
    }

    /**
     * Sets the value of the sizeLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSizeLimit(Long value) {
        this.sizeLimit = value;
    }

    /**
     * Gets the value of the timeLimit property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getTimeLimit() {
        if (timeLimit == null) {
            return  0L;
        } else {
            return timeLimit;
        }
    }

    /**
     * Sets the value of the timeLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTimeLimit(Long value) {
        this.timeLimit = value;
    }

    /**
     * Gets the value of the typesOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTypesOnly() {
        if (typesOnly == null) {
            return false;
        } else {
            return typesOnly;
        }
    }

    /**
     * Sets the value of the typesOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTypesOnly(Boolean value) {
        this.typesOnly = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="neverDerefAliases"/>
     *     &lt;enumeration value="derefInSearching"/>
     *     &lt;enumeration value="derefFindingBaseObj"/>
     *     &lt;enumeration value="derefAlways"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum DerefAliasesType {

        @XmlEnumValue("neverDerefAliases")
        NEVER_DEREF_ALIASES("neverDerefAliases"),
        @XmlEnumValue("derefInSearching")
        DEREF_IN_SEARCHING("derefInSearching"),
        @XmlEnumValue("derefFindingBaseObj")
        DEREF_FINDING_BASE_OBJ("derefFindingBaseObj"),
        @XmlEnumValue("derefAlways")
        DEREF_ALWAYS("derefAlways");
        private final String value;

        DerefAliasesType(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static SearchRequest.DerefAliasesType fromValue(String v) {
            for (SearchRequest.DerefAliasesType c: SearchRequest.DerefAliasesType.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="baseObject"/>
     *     &lt;enumeration value="singleLevel"/>
     *     &lt;enumeration value="wholeSubtree"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum SearchScope {

        @XmlEnumValue("baseObject")
        BASE_OBJECT("baseObject"),
        @XmlEnumValue("singleLevel")
        SINGLE_LEVEL("singleLevel"),
        @XmlEnumValue("wholeSubtree")
        WHOLE_SUBTREE("wholeSubtree");
        private final String value;

        SearchScope(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static SearchRequest.SearchScope fromValue(String v) {
            for (SearchRequest.SearchScope c: SearchRequest.SearchScope.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
