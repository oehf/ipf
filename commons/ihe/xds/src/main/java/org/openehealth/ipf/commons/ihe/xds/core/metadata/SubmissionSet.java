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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XDS submission set according to the IHE XDS specification.
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmissionSet", propOrder = {
        "sourceId", "submissionTime", "authors", "intendedRecipients", "contentTypeCode"})
@XmlRootElement(name = "submissionSet")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class SubmissionSet extends XDSMetaClass implements Serializable {
    @Serial
    private static final long serialVersionUID = 5961980266312684583L;
    
    @XmlElement(name = "author")
    @Getter private final List<Author> authors = new ArrayList<>();
    @Getter @Setter private Code contentTypeCode;
    @XmlElement(name = "intendedRecipient")
    @Getter private final List<Recipient> intendedRecipients = new ArrayList<>();
    @Getter @Setter private String sourceId;
    @Getter private Timestamp submissionTime;

    /**
     * @param author
     *          the author of the submission.
     */
    public void setAuthor(Author author) {
        authors.clear();
        authors.add(author);
    }

    @JsonProperty
    public void setSubmissionTime(Timestamp submissionTime) {
        this.submissionTime = submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = Timestamp.fromHL7(submissionTime);
    }

}
