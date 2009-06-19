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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

/**
 * Represents an identifiable person.
 * <p>
 * This class contains members from the HL7v2 XCN data type. The XDS profile
 * imposes some limitations on the XCN type. Most notably the XCN.9
 * component has the same restrictions as the CX.4 component (as described
 * in {@link Identifiable}. 
 * @author Jens Riemschneider
 */
public class Person {    
    private Identifiable id;        // XCN.1 and XCN.9.2
    private Name name;              // XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6

    public Identifiable getId() {
        return id;
    }

    public void setId(Identifiable id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
