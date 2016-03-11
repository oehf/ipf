package org.openehealth.ipf.commons.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * URN abstraction, following the pattern of URI and URL classes. This is not meant as a generic
 * URN representation as defined in RFC 2141, but limited to its uses in healthcare domain,
 * specifically for encoding UUIDs and OIDs.
 *
 * Note that in URNs the namespace identifier is case-insensitive
 */
public final class URN implements Comparable<URN>, Serializable {

    public static final String UUID = "uuid";
    public static final String OID = "oid";
    public static final String PIN = "pin";
    private static final String PREFIX = "urn";
    private static final String SEP = ":";
    private static final Pattern REGEX = Pattern.compile("^urn:[A-Za-z0-9][A-Za-z0-9-]{0,31}:[A-Za-z0-9()+,\\-.:=@;$_!*'%/?#]+$");

    private final URI uri;

    public URN(String text) throws URISyntaxException {
        if (!REGEX.matcher(text).matches()) {
            throw new URISyntaxException(text, "Not a valid URN");
        }
        this.uri = URI.create(text);
    }

    public static URN create(String text) throws URISyntaxException {
        return new URN(text);
    }

    public URN(URI uri) throws URISyntaxException {
        this(uri.toString());
    }

    public URN(UUID uuid) throws URISyntaxException {
        this(String.format("%s%s%s%2$s%s", PREFIX, SEP, UUID, uuid.toString()));
    }

    public URN(String namespaceId, String namespaceSpecificString) throws URISyntaxException {
        this(String.format("%s%s%s%2$s%s", PREFIX, SEP, namespaceId, namespaceSpecificString));
    }

    @Override
    public int compareTo(URN urn) {
        return equals(urn) ? 0 : uri.compareTo(urn.uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URN urn = (URN) o;
        return getNamespaceId().equalsIgnoreCase(urn.getNamespaceId()) &&
                getNamespaceSpecificString().equals(urn.getNamespaceSpecificString());
    }

    @Override
    public int hashCode() {
        int result = getNamespaceId().toLowerCase(Locale.ROOT).hashCode();
        result = 31 * result + getNamespaceSpecificString().hashCode();
        return result;
    }

    public static boolean isURN(final String text) {
        try {
            new URN(text);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    /**
     * @return a new URI object representing this URN
     */
    public URI toURI() {
        return URI.create(uri.toString());
    }

    /**
     * @return the namespace ID of the URN
     */
    public String getNamespaceId() {
        return this.part(1);
    }

    public boolean isNamespace(String namespace) {
        return getNamespaceId().equalsIgnoreCase(namespace);
    }

    /**
     * @return namespace-specific string, i.e. an OID or UUID value
     */
    public String getNamespaceSpecificString() {
        try {
            return URLDecoder.decode(this.part(2), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public String toString() {
        return uri.toString();
    }

    private String part(final int index) {
        return uri.toString().split(SEP, 3)[index];
    }
}
