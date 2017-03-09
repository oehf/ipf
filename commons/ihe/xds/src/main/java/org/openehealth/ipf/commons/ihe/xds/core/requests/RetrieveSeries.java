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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Request object for a single Series.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Clay Sebourn
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveSeries", propOrder = {"seriesInstanceUID", "documents"})
@XmlRootElement(name = "retrieveSeries")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class RetrieveSeries implements Serializable
{
    private static final long serialVersionUID = 8999352499981099421L;

    protected String seriesInstanceUID;
    @XmlElementRef
    private List<DocumentReference> documents = new ArrayList<>();

    /**
     * Constructs the RetrieveSeries.
     */
    public RetrieveSeries() {}

    /**
     * Constructs the Series request.
     *
     * @param seriesInstanceUID    he series instance UID.
     * @param documents            the documents.
     */
    public RetrieveSeries(String seriesInstanceUID, List<DocumentReference> documents) {
        this.seriesInstanceUID = seriesInstanceUID;
        this.documents = documents;
    }

    /**
     * Gets the value of the seriesInstanceUID property.
     *
     * @return seriesInstanceUID      {@link String }
     *
     */
    public String getSeriesInstanceUID() {
        return seriesInstanceUID;
    }

    /**
     * @param seriesInstanceUID
     *          the unique ID of the series instance.
     */
    public void setSeriesInstanceUID(String seriesInstanceUID) {
        this.seriesInstanceUID = seriesInstanceUID;
    }

    /**
     * @return the list of documents to retrieve.
     */
    public List<DocumentReference> getDocuments() {
        return documents;
    }

}
