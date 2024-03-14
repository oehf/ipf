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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object for a single Study.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Clay Sebourn
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveStudy", propOrder = {"studyInstanceUID", "retrieveSerieses"})
@XmlRootElement(name = "retrieveStudy")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class RetrieveStudy implements Serializable
{
    private static final long serialVersionUID = 8999352499981099420L;

    protected String studyInstanceUID;
    @XmlElementRef
    protected List<RetrieveSeries> retrieveSerieses;

    /**
     * Constructs the Study request.
     */
    public RetrieveStudy() {}

    /**
     * Constructs the Study request.
     *
     * @param studyInstanceUID          the study instance UID.
     * @param retrieveSerieses          the series requests.
     */
    public RetrieveStudy(String studyInstanceUID, List<RetrieveSeries> retrieveSerieses) {
        this.studyInstanceUID = studyInstanceUID;
        this.retrieveSerieses = retrieveSerieses;
    }

    /**
     * Gets the value of the studyInstanceUID property.
     *
     * @return studyInstanceUID     {@link String }
     *
     */
    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    /**
     * @param studyInstanceUID
     *          the unique ID of the Study instance.
     */
    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    /**
     * Gets the value of the retrieveSerieses property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the seriesRequest property.
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRetrieveSerieses().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link RetrieveSeries }
     *
     * @return  the series requests
     */
    public List<RetrieveSeries> getRetrieveSerieses() {
        if (retrieveSerieses == null) {
            retrieveSerieses = new ArrayList<>();
        }
        return this.retrieveSerieses;
    }

}