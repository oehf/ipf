package org.openehealth.ipf.commons.xml;

import net.sf.saxon.lib.ResourceRequest;
import net.sf.saxon.lib.ResourceResolver;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.InputSource;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.util.Objects;

/**
 * ResourceResolver used to correctly resolve import commands in xslt or xquery content. The referenced resource
 * (stylesheet or document) can be found somewhere in the classloader's classpath. If it can't be found, the default
 * ResourceResolver is used.
 *
 * @author Quentin Ligier
 **/
public class ClasspathResourceResolver implements ResourceResolver {

    private final ResourceResolver standardResolver;

    /**
     * Constructor.
     *
     * @param resolver The standard resolver to use if the resource isn't found in the classpath.
     */
    public ClasspathResourceResolver(final ResourceResolver resolver) {
        this.standardResolver = Objects.requireNonNull(resolver);
    }

    /**
     * Resolve by searching the classpath, fallback to default resolution strategy.
     *
     * @param resourceRequest The SAXON resource request.
     * @return the resource {@link Source}.
     * @throws XPathException if the request is invalid in some way, or if the identified resource is unsuitable,
     * or if resolution is to fail rather than being delegated to another resolver.
     */
    @Override
    public Source resolve(final ResourceRequest resourceRequest) throws XPathException {
        final var url = getClass().getClassLoader().getResource(resourceRequest.relativeUri);
        if (url != null) {
            var saxSource = new SAXSource();
            saxSource.setInputSource(new InputSource(url.toString()));
            saxSource.setSystemId(url.toString());
            return saxSource;
        } else {
            return standardResolver.resolve(resourceRequest);
        }
    }
}
