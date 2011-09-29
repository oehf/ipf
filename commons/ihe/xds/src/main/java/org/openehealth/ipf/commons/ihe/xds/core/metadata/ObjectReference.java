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
import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an object reference.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectReference", propOrder = {"home", "id"})
public class ObjectReference implements Serializable {
    private static final long serialVersionUID = 5442558815484966722L;

    @XmlAttribute(name = "uuid")
    private String id;
    @XmlAttribute(name = "homeCommunityId")
    private String home;
    
    /**
     * Constructs an object reference.
     */
    public ObjectReference() {}
    
    /**
     * Constructs an object reference.
     * @param id
     *          the id of the referenced object.
     */
    public ObjectReference(String id) {
        this.id = id;
    }

    /**
     * Constructs an object reference.
     * @param id
     *          the id of the referenced object.
     * @param home
     *          the ID of the community that the object was created in.
     */
    public ObjectReference(String id, String home) {
        this.id = id;
        this.home = home;
    }

    /**
     * @return the id of the referenced object.
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id
     *          the id of the referenced object.
     */
    public void setId(String id) {
        this.id = id;
    }
        
    /**
     * @return the ID of the community that the object was created in.
     */
    public String getHome() {
        return home;
    }
    
    /**
     * @param home
     *          the ID of the community that the object was created in.
     */
    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((home == null) ? 0 : home.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        ObjectReference other = (ObjectReference) obj;
        if (home == null) {
            if (other.home != null)
                return false;
        } else if (!home.equals(other.home))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
