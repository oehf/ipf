/*
 * Copyright 2012 the original author or authors.
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

import ca.uhn.hl7v2.model.v25.datatype.XPN;

/**
 * This class represents a name.
 * <p>
 * It is derived from the HL7v2 data type XPN. It only contains
 * naming related fields of these data types.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are
 * removed from the HL7 string.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class XpnName extends Name<XPN> {
    private static final long serialVersionUID = -1443721482370374457L;

    public XpnName() {
        super(new XPN(MESSAGE));
    }

    public XpnName(XPN xpn) {
        super(xpn);
    }


    /**
     * Constructs a name.
     * @param familyName
     *          the family name (XPN.1.1).
     * @param givenName
     *          the given name (XPN.2).
     * @param secondAndFurtherGivenNames
     *          the second and further names (XPN.3).
     * @param suffix
     *          the suffix (XPN.4).
     * @param prefix
     *          the prefix (XPN.5).
     * @param degree
     *          academical degree (XPN.6).
     */
    public XpnName(String familyName, String givenName, String secondAndFurtherGivenNames, String suffix, String prefix, String degree) {
        this();
        setFamilyName(familyName);
        setGivenName(givenName);
        setSecondAndFurtherGivenNames(secondAndFurtherGivenNames);
        setSuffix(suffix);
        setPrefix(prefix);
        setDegree(degree);
    }


    @Override
    public String getFamilyName() {
        return getHapiObject().getXpn1_FamilyName().getFn1_Surname().getValue();
    }

    @Override
    public void setFamilyName(String value) {
        setValue(getHapiObject().getXpn1_FamilyName().getFn1_Surname(), value);
    }

    @Override
    public String getGivenName() {
        return getHapiObject().getXpn2_GivenName().getValue();
    }

    @Override
    public void setGivenName(String value) {
        setValue(getHapiObject().getXpn2_GivenName(), value);
    }

    @Override
    public String getSecondAndFurtherGivenNames() {
        return getHapiObject().getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof().getValue();
    }

    @Override
    public void setSecondAndFurtherGivenNames(String value) {
        setValue(getHapiObject().getXpn3_SecondAndFurtherGivenNamesOrInitialsThereof(), value);
    }

    @Override
    public String getSuffix() {
        return getHapiObject().getXpn4_SuffixEgJRorIII().getValue();
    }

    @Override
    public void setSuffix(String value) {
        setValue(getHapiObject().getXpn4_SuffixEgJRorIII(), value);
    }

    @Override
    public String getPrefix() {
        return getHapiObject().getXpn5_PrefixEgDR().getValue();
    }

    @Override
    public void setPrefix(String value) {
        setValue(getHapiObject().getXpn5_PrefixEgDR(), value);
    }

    @Override
    public String getDegree() {
        return getHapiObject().getXpn6_DegreeEgMD().getValue();
    }

    @Override
    public void setDegree(String value) {
        setValue(getHapiObject().getXpn6_DegreeEgMD(), value);
    }
}
