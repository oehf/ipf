/*
 * Copyright 2009 the original author or authors.
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

import ca.uhn.hl7v2.model.v25.datatype.CE;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.LocalizedStringAdapter;

/**
 * Represents a code.
 * <p> 
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Code", propOrder = {"code", "schemeName", "displayName"})
public class Code extends Hl7v2Based<CE> {
    private static final long serialVersionUID = 7603534956639945984L;

    private LocalizedString localizedString;


    /**
     * Constructs a code.
     */
    public Code() {
        super(new CE(MESSAGE));
    }

    /**
     * Constructs a code.
     */
    public Code(CE ce) {
        super(ce);
    }

    /**
     * Constructs a code.
     * @param code
     *          the value of the code.
     * @param displayName
     *          the display name of the code.
     * @param schemeName
     *          the schema of the code.
     */
    public Code(String code, LocalizedString displayName, String schemeName) {
        this();
        setCode(code);
        setDisplayName(displayName);
        setSchemeName(schemeName);
    }

    /**
     * @return the value of this code.
     */
    @XmlAttribute
    public String getCode() {
        return getHapiObject().getCe1_Identifier().getValue();
    }
    
    /**
     * @param code 
     *          the value of this code.
     */
    public void setCode(String code) {
        setValue(getHapiObject().getCe1_Identifier(), code);
    }
    
    /**
     * @return the display name of this code.
     */
    @XmlAttribute
    @XmlJavaTypeAdapter(value = LocalizedStringAdapter.class)
    public LocalizedString getDisplayName() {
        String value = getHapiObject().getCe2_Text().getValue();

        if (StringUtils.isEmpty(value)) {
            localizedString = null;
        }
        else if (localizedString != null) {
            localizedString.setValue(value);
        }
        else {
            localizedString = new LocalizedString(value);
        }

        return localizedString;
    }
    
    /**
     * @param displayName
     *          the display name of this code.
     */
    public void setDisplayName(LocalizedString displayName) {
        this.localizedString = displayName;
        if (displayName != null) {
            setValue(getHapiObject().getCe2_Text(), displayName.getValue());
        } else {
            getHapiObject().getCe2_Text().clear();
        }
    }
    
    /**
     * @return the schema of this code.
     */
    @XmlAttribute(name = "codeSystemName")
    public String getSchemeName() {
        return getHapiObject().getCe3_NameOfCodingSystem().getValue();
    }
    
    /**
     * @param schemeName
     *          the schema of this code.
     */
    public void setSchemeName(String schemeName) {
        setValue(getHapiObject().getCe3_NameOfCodingSystem(), schemeName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getDisplayName() == null) ? 0 : getDisplayName().hashCode());
        result = prime * result + ((getSchemeName() == null) ? 0 : getSchemeName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Code other = (Code) obj;
        if (getCode() == null) {
            if (other.getCode() != null)
                return false;
        } else if (!getCode().equals(other.getCode()))
            return false;
        if (getDisplayName() == null) {
            if (other.getDisplayName() != null)
                return false;
        } else if (!getDisplayName().equals(other.getDisplayName()))
            return false;
        if (getSchemeName() == null) {
            if (other.getSchemeName() != null)
                return false;
        } else if (!getSchemeName().equals(other.getSchemeName()))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("code", getCode())
                .append("displayName", getDisplayName())
                .append("schemeName", getSchemeName())
                .toString();
    }
}
