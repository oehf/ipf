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
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object for the Retrieve Imaging Document Set transaction.
 * <p>
 * Lists are pre-created and can therefore never be <code>null</code>.
 * @author Clay Sebourn
 */

@XmlRootElement(name = "retrieveImagingDocumentSet")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveImagingDocumentSet", propOrder = {"retrieveStudies", "transferSyntaxIds"})
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class RetrieveImagingDocumentSet implements Serializable {
    private static final long serialVersionUID = -8999352499981099419L;

    @XmlElementRef
    private final List<RetrieveStudy> retrieveStudies = new ArrayList<>();

    @XmlElement(name = "transferSyntaxId")
    private final List<String> transferSyntaxIds = new ArrayList<>();

    /**
     * @return the list of StudyRequests to retrieve.
     */
    public List<RetrieveStudy> getRetrieveStudies() {
        return retrieveStudies;
    }

    /**
     *
     * @return the list of TransferSyntaxUID
     */
    public List<String> getTransferSyntaxIds() {
        return transferSyntaxIds;
    }

}
