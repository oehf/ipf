package org.openehealth.ipf.commons.ihe.fhir.iti83;

import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.fhir.FhirObject;

/**
 * A ITI-83 PIXm request object
 */
public class PixmRequest implements FhirObject {

    private final TokenParam sourceIdentifier;
    private final UriParam requestedDomain;

    public PixmRequest(TokenParam sourceIdentifier, UriParam requestedDomain) {
        Validate.notNull(sourceIdentifier, "source Identifier is null");
        this.sourceIdentifier = sourceIdentifier;
        this.requestedDomain = requestedDomain;
    }

    public TokenParam getSourceIdentifier() {
        return sourceIdentifier;
    }

    public UriParam getRequestedDomain() {
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
