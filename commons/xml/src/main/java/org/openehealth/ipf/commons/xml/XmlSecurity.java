/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.FeatureKeys;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Applies XXE and SSRF countermeasures to the JAXP and Saxon components that process untrusted
 * input.
 * <p>
 * Restricting a {@code SchemaFactory} does <em>not</em> restrict the {@link Validator} it produces:
 * schema compilation and instance validation are separate parsers with separate settings, so both
 * have to be hardened explicitly.
 * <p>
 * Every property is applied individually and failures are only logged, because the available
 * settings depend on which JAXP implementation is in use — Saxon rejects several attributes that the
 * JDK accepts and vice versa, and an unsupported name raises an exception rather than being ignored.
 *
 * @since 6.0
 */
@Slf4j
@UtilityClass
public class XmlSecurity {

    /** Empty string means "no protocol is permitted" for the JAXP {@code ACCESS_EXTERNAL_*} properties. */
    private static final String NO_ACCESS = "";

    /**
     * Protocols still allowed when compiling schemas: enough to follow {@code xs:import} and
     * {@code xs:include} to a sibling file or to a resource inside a jar/fat jar (a jar in a jar), but not
     * over the network.
     */
    private static final String LOCAL_ACCESS_ONLY = "file,jar,nested";

    private static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";

    /**
     * Shared factory for {@link #newXmlReader()}. {@link SAXParserFactory} is not specified to be thread-safe,
     * so access to it is synchronized; creating a parser is cheap compared to the parsing itself.
     */
    private static final SAXParserFactory UNTRUSTED_SAX_PARSER_FACTORY = newUntrustedSaxParserFactory();

    /**
     * Hardens schema compilation. Schemas are trusted classpath resources, so this is defense in
     * depth only.
     * <p>
     * {@link XMLConstants#ACCESS_EXTERNAL_SCHEMA} is restricted to local protocols rather than
     * denied outright. The IHE schemas are split across many files that pull each other in via
     * {@code xs:import} and {@code xs:include}, and enabling secure processing alone already denies
     * all external schema access implicitly, which prevents any schema from compiling. Allowing
     * {@code file} and {@code jar} keeps those imports working while still blocking network fetches.
     * The instance-document side has no such need — see {@link #harden(Validator)}.
     */
    public static void harden(SchemaFactory factory) {
        setFeature(factory::setFeature, XMLConstants.FEATURE_SECURE_PROCESSING, true, "SchemaFactory");
        setProperty(factory::setProperty, XMLConstants.ACCESS_EXTERNAL_DTD, NO_ACCESS, "SchemaFactory");
        setProperty(factory::setProperty, XMLConstants.ACCESS_EXTERNAL_SCHEMA, LOCAL_ACCESS_ONLY, "SchemaFactory");
    }

    /**
     * Hardens validation of an instance document. This is the important one: the document being
     * validated is untrusted, and without these settings a {@code DOCTYPE} in it can read local
     * files or make the validator issue outbound requests.
     */
    public static void harden(Validator validator) {
        setFeature(validator::setFeature, XMLConstants.FEATURE_SECURE_PROCESSING, true, "Validator");
        setProperty(validator::setProperty, XMLConstants.ACCESS_EXTERNAL_DTD, NO_ACCESS, "Validator");
        setProperty(validator::setProperty, XMLConstants.ACCESS_EXTERNAL_SCHEMA, NO_ACCESS, "Validator");
    }

    /**
     * Hardens a SAX parser factory for parsing untrusted documents: any {@code DOCTYPE} declaration is
     * rejected, which rules out external entities, external DTDs and entity expansion altogether. Inbound
     * messages of the supported IHE profiles never legitimately carry a {@code DOCTYPE}.
     * <p>
     * Unlike the other settings in this class, rejecting {@code DOCTYPE} declarations is mandatory: if the
     * parser implementation does not support it, this method fails instead of continuing unprotected.
     *
     * @param factory SAX parser factory to harden
     * @throws IllegalStateException if the factory cannot be configured to reject {@code DOCTYPE} declarations
     * @since 6.1
     */
    public static void harden(SAXParserFactory factory) {
        setFeature(factory::setFeature, XMLConstants.FEATURE_SECURE_PROCESSING, true, "SAXParserFactory");
        setFeature(factory::setFeature, "http://xml.org/sax/features/external-general-entities", false, "SAXParserFactory");
        setFeature(factory::setFeature, "http://xml.org/sax/features/external-parameter-entities", false, "SAXParserFactory");
        setFeature(factory::setFeature, "http://apache.org/xml/features/nonvalidating/load-external-dtd", false, "SAXParserFactory");
        factory.setXIncludeAware(false);
        try {
            factory.setFeature(DISALLOW_DOCTYPE_DECL, true);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("SAX parser cannot be configured to reject DOCTYPE declarations", e);
        }
    }

    /**
     * Creates a namespace-aware {@link XMLReader} for parsing untrusted documents, configured as described in
     * {@link #harden(SAXParserFactory)}. Readers are not thread-safe; create one per parse.
     *
     * @return a new hardened XML reader
     * @since 6.1
     */
    public static XMLReader newXmlReader() {
        try {
            final SAXParser parser;
            synchronized (UNTRUSTED_SAX_PARSER_FACTORY) {
                parser = UNTRUSTED_SAX_PARSER_FACTORY.newSAXParser();
            }
            return parser.getXMLReader();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException("Could not create a hardened XML reader", e);
        }
    }

    private static SAXParserFactory newUntrustedSaxParserFactory() {
        var factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        harden(factory);
        return factory;
    }

    /**
     * Hardens an XSLT transformer factory. Stylesheets are loaded from the classpath and are trusted,
     * but the transformed document is not, and a transformer resolves external DTDs by default.
     */
    public static void harden(TransformerFactory factory) {
        setFeature(factory::setFeature, XMLConstants.FEATURE_SECURE_PROCESSING, true, "TransformerFactory");
        setAttribute(factory, XMLConstants.ACCESS_EXTERNAL_DTD, NO_ACCESS);
        setAttribute(factory, XMLConstants.ACCESS_EXTERNAL_STYLESHEET, NO_ACCESS);
    }

    /**
     * Variant of {@link #harden(TransformerFactory)} for factories that only ever process trusted
     * classpath resources, never inbound messages.
     * <p>
     * Some of the bundled Schematron rule sets are assembled from entity files — {@code HITSP_C32.sch}
     * pulls in {@code 2.16.840.1.113883.10.20.2.7.ent}, for example — so compiling them requires
     * external entity resolution. Local protocols are therefore permitted here while network access
     * stays blocked. Do not use this for a factory that transforms untrusted input: allowing
     * {@code file} would let a {@code DOCTYPE} in an inbound document read local files.
     */
    public static void hardenForTrustedResources(TransformerFactory factory) {
        setFeature(factory::setFeature, XMLConstants.FEATURE_SECURE_PROCESSING, true, "TransformerFactory");
        setAttribute(factory, XMLConstants.ACCESS_EXTERNAL_DTD, LOCAL_ACCESS_ONLY);
        setAttribute(factory, XMLConstants.ACCESS_EXTERNAL_STYLESHEET, LOCAL_ACCESS_ONLY);
    }

    /**
     * Hardens a Saxon configuration used for XQuery and XSLT. Disabling external functions is the
     * significant control here: it stops a query from reaching arbitrary Java methods by reflection.
     */
    public static void harden(Configuration configuration) {
        setSaxonProperty(configuration, FeatureKeys.ALLOW_EXTERNAL_FUNCTIONS, false);
        setSaxonProperty(configuration, FeatureKeys.DTD_VALIDATION, false);
        setSaxonProperty(configuration, FeatureKeys.XINCLUDE, false);
    }

    private interface FeatureSetter {
        void set(String name, boolean value) throws Exception;
    }

    private interface PropertySetter {
        void set(String name, Object value) throws Exception;
    }

    private static void setFeature(FeatureSetter setter, String name, boolean value, String target) {
        try {
            setter.set(name, value);
        } catch (Exception e) {
            log.debug("{} does not support feature {}: {}", target, name, e.getMessage());
        }
    }

    private static void setProperty(PropertySetter setter, String name, String value, String target) {
        try {
            setter.set(name, value);
        } catch (Exception e) {
            log.debug("{} does not support property {}: {}", target, name, e.getMessage());
        }
    }

    private static void setAttribute(TransformerFactory factory, String name, String value) {
        try {
            factory.setAttribute(name, value);
        } catch (Exception e) {
            log.debug("TransformerFactory does not support attribute {}: {}", name, e.getMessage());
        }
    }

    private static void setSaxonProperty(Configuration configuration, String name, boolean value) {
        try {
            configuration.setBooleanProperty(name, value);
        } catch (Exception e) {
            log.debug("Saxon configuration does not support {}: {}", name, e.getMessage());
        }
    }
}
