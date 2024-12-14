/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object for the Remove Metadata transaction.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Boris Stanojevic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoveMetadata", propOrder = {"references"})
@XmlRootElement(name = "removeMetadata")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class RemoveMetadata implements Serializable {
    @Serial
    private static final long serialVersionUID = -737326382128159189L;

    @XmlElement(name = "reference")
    @Getter private final List<ObjectReference> references = new ArrayList<>();

}
