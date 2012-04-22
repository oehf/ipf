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

import ca.uhn.hl7v2.model.v25.datatype.XCN;

/**
 * This class represents a name.
 * <p>
 * It is derived from the HL7v2 data type XCN. It only contains
 * naming related fields of these data types.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are
 * removed from the HL7 string.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
public class XcnName extends Name<XCN> {
    private static final long serialVersionUID = 1003106925101895418L;

    public XcnName() {
        super(new XCN(MESSAGE));
    }

    public XcnName(XCN xcn) {
        super(xcn);
    }


    /**
     * Constructs a name.
     * @param familyName
     *          the family name (XCN.2.1).
     * @param givenName
     *          the given name (XCN.3).
     * @param secondAndFurtherGivenNames
     *          the second and further names (XCN.4).
     * @param suffix
     *          the suffix (XCN.5).
     * @param prefix
     *          the prefix (XCN.6).
     * @param degree
     *          academical degree (XCN.7).
     */
    public XcnName(String familyName, String givenName, String secondAndFurtherGivenNames, String suffix, String prefix, String degree) {
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
        return getHapiObject().getXcn2_FamilyName().getFn1_Surname().getValue();
    }

    @Override
    public void setFamilyName(String value) {
        setValue(getHapiObject().getXcn2_FamilyName().getFn1_Surname(), value);
    }

    @Override
    public String getGivenName() {
        return getHapiObject().getXcn3_GivenName().getValue();
    }

    @Override
    public void setGivenName(String value) {
        setValue(getHapiObject().getXcn3_GivenName(), value);
    }

    @Override
    public String getSecondAndFurtherGivenNames() {
        return getHapiObject().getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().getValue();
    }

    @Override
    public void setSecondAndFurtherGivenNames(String value) {
        setValue(getHapiObject().getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof(), value);
    }

    @Override
    public String getSuffix() {
        return getHapiObject().getXcn5_SuffixEgJRorIII().getValue();
    }

    @Override
    public void setSuffix(String value) {
        setValue(getHapiObject().getXcn5_SuffixEgJRorIII(), value);
    }

    @Override
    public String getPrefix() {
        return getHapiObject().getXcn6_PrefixEgDR().getValue();
    }

    @Override
    public void setPrefix(String value) {
        setValue(getHapiObject().getXcn6_PrefixEgDR(), value);
    }

    @Override
    public String getDegree() {
        return getHapiObject().getXcn7_DegreeEgMD().getValue();
    }

    @Override
    public void setDegree(String value) {
        setValue(getHapiObject().getXcn7_DegreeEgMD(), value);
    }
}
