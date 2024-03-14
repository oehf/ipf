/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters;

import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * @author Dmytro Rud
 */
@XmlType(name = "", propOrder = {"values"})
@XmlAccessorType(XmlAccessType.FIELD)
public class StringMapEntry {
    @XmlAttribute(name = "key", required = true)
    @Getter @Setter private String key;

    @XmlElement(name = "value", required = true)
    @Getter @Setter private ArrayList<String> values;
}
