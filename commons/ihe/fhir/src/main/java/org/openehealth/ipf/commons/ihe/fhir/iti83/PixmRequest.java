package org.openehealth.ipf.commons.ihe.fhir.iti83;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.UriType;
import org.openehealth.ipf.commons.ihe.fhir.FhirObject;

/**
 * A ITI-83 PIXm request object
 *
 * @author Christian Ohr
 * @since 3.1
 */
public class PixmRequest implements FhirObject {

    private final Identifier sourceIdentifier;
    private final UriType requestedDomain;

    public PixmRequest(Identifier sourceIdentifier, UriType requestedDomain) {
        Validate.notNull(sourceIdentifier, "source Identifier is null");
        this.sourceIdentifier = sourceIdentifier;
        this.requestedDomain = requestedDomain;
    }

    public Identifier getSourceIdentifier() {
        return sourceIdentifier;
    }

    public UriType getRequestedDomain() {
        return requestedDomain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PixmRequest that = (PixmRequest) o;

        if (!sourceIdentifier.equals(that.sourceIdentifier)) return false;
        return !(requestedDomain != null ? !requestedDomain.equals(that.requestedDomain) : that.requestedDomain != null);

    }

    @Override
    public int hashCode() {
        int result = sourceIdentifier.hashCode();
        result = 31 * result + (requestedDomain != null ? requestedDomain.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PixmRequest{" +
                "sourceIdentifier=" + sourceIdentifier +
                ", requestedDomain=" + requestedDomain +
                '}';
    }
}
