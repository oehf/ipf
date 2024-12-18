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

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an object reference.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectReference", propOrder = {"home", "id"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class ObjectReference implements Serializable {
    @Serial
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

}
