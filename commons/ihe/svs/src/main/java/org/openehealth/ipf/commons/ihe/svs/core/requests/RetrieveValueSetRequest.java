package org.openehealth.ipf.commons.ihe.svs.core.requests;

import lombok.Data;
import lombok.NonNull;

import javax.xml.bind.annotation.*;

/**
 *
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveValueSetRequestType", propOrder = { "valueSet" })
@XmlRootElement(name = "RetrieveValueSetRequest")
@Data
public class RetrieveValueSetRequest {

    @XmlElement(name = "ValueSet", required = true)
    @NonNull
    private ValueSetRequest valueSet;

    /**
     * Empty constructor for JAXB.
     */
    public RetrieveValueSetRequest() {
    }

    public RetrieveValueSetRequest(@NonNull final ValueSetRequest valueSet) {
        this.valueSet = valueSet;
    }
}
