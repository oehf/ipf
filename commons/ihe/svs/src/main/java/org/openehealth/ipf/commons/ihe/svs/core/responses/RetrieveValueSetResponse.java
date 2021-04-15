package org.openehealth.ipf.commons.ihe.svs.core.responses;

import lombok.Data;
import lombok.NonNull;
import org.openehealth.ipf.commons.ihe.svs.core.responses.jaxbadapters.OffsetDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.OffsetDateTime;

/**
 *
 *
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrieveValueSetResponseType", propOrder = {"valueSet"})
@XmlRootElement(name = "RetrieveValueSetResponse")
@Data
public class RetrieveValueSetResponse {

    @XmlElement(name = "ValueSet", required = true)
    @NonNull
    private ValueSetResponse valueSet;

    @XmlAttribute(name = "cacheExpirationHint")
    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    protected OffsetDateTime cacheExpirationHint;

    /**
     * Empty constructor for JAXB.
     */
    public RetrieveValueSetResponse() {
    }

    public RetrieveValueSetResponse(@NonNull final ValueSetResponse valueSet,
                                    final OffsetDateTime cacheExpirationHint) {
        this.valueSet = valueSet;
        this.cacheExpirationHint = cacheExpirationHint;
    }
}
