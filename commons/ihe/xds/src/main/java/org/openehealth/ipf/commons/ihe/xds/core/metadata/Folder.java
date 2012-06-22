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

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.DateAdapter;

/**
 * Represents an XDS folder according to the IHE XDS specification.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Folder", propOrder = {"lastUpdateTime", "codeList"})
@XmlRootElement(name = "folder")
public class Folder extends XDSMetaClass implements Serializable {
    private static final long serialVersionUID = -1923451867453561796L;
    
    @XmlElement(name = "code")
    private final List<Code> codeList = new ArrayList<Code>();
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(value = DateAdapter.class)
    private String lastUpdateTime;

    /**
     * @return the last time this folder was updated.
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * @param lastUpdateTime
     *          the last time this folder was updated.
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    /**
     * @return the list of codes for this folder.
     */
    public List<Code> getCodeList() {
        return codeList;
    }
        
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((codeList == null) ? 0 : codeList.hashCode());
        result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Folder other = (Folder) obj;
        if (codeList == null) {
            if (other.codeList != null)
                return false;
        } else if (!codeList.equals(other.codeList))
            return false;
        if (lastUpdateTime == null) {
            if (other.lastUpdateTime != null)
                return false;
        } else if (!lastUpdateTime.equals(other.lastUpdateTime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
