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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * Request object for a single Series.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Clay Sebourn
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveSeries", propOrder = {"seriesInstanceUID", "documents"})
@XmlRootElement(name = "retrieveSeries")
public class RetrieveSeries implements Serializable
{
    private static final long serialVersionUID = 8999352499981099421L;

    protected String seriesInstanceUID;
    @XmlElementRef
    private List<RetrieveDocument> documents = new ArrayList<RetrieveDocument>();

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
    public RetrieveSeries(String seriesInstanceUID, List<RetrieveDocument> documents) {
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
    public List<RetrieveDocument> getDocuments() {
        return documents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((seriesInstanceUID == null) ? 0 : seriesInstanceUID.hashCode());
        result = prime * result + ((documents == null) ? 0 : documents.hashCode());
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
        RetrieveSeries other = (RetrieveSeries) obj;

        if (seriesInstanceUID == null) {
            if (other.seriesInstanceUID != null)
                return false;
        } else if (!seriesInstanceUID.equals(other.seriesInstanceUID))
            return false;

        if (documents == null) {
            if (other.documents != null)
                return false;
        } else if (!documents.equals(other.documents))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
