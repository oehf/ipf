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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.telemetry.Op;
import org.apache.camel.telemetry.Span;
import org.apache.camel.telemetry.SpanDecorator;
import org.apache.camel.telemetry.SpanDecoratorManagerImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.DefaultAuditContext;
import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.core.config.Registry;
import org.openehealth.ipf.platform.camel.ihe.core.IpfSpanDecorator;
import org.openehealth.ipf.platform.camel.ihe.ws.WsSpanDecorator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the span decorators against real endpoints of the real components, i.e. the metadata is
 * taken from the actual transaction configurations rather than from test doubles.
 *
 * @author Christian Ohr
 */
public class XdsSpanDecoratorTest {

    private CamelContext camelContext;

    /**
     * IPF endpoints resolve collaborators through the {@link ContextFacade}, which normally is backed
     * by a Spring context. Endpoint creation needs an {@link AuditContext} and nothing else, so a
     * minimal registry keeps this test free of a Spring context.
     */
    @BeforeAll
    public static void setUpRegistry() {
        var auditContext = new DefaultAuditContext();
        ContextFacade.setRegistry(new Registry() {
            @Override
            public Object bean(String name) {
                return null;
            }

            @Override
            public <T> T bean(Class<T> requiredType) {
                return requiredType.isInstance(auditContext) ? requiredType.cast(auditContext) : null;
            }

            @Override
            public <T> Map<String, T> beans(Class<T> requiredType) {
                return requiredType.isInstance(auditContext)
                        ? Map.of("auditContext", requiredType.cast(auditContext))
                        : Map.of();
            }
        });
    }

    @AfterAll
    public static void tearDownRegistry() {
        ContextFacade.clearRegistry();
    }

    /** Collects what a decorator sets on a span. */
    private static class RecordingSpan implements Span {
        private final Map<String, String> tags = new HashMap<>();

        @Override public void log(Map<String, String> fields) { }
        @Override public void setTag(String key, String value) { tags.put(key, value); }
        @Override public void setComponent(String component) { }
        @Override public void setError(boolean error) { }
    }

    static List<String> schemes() {
        return ServiceLoader.load(SpanDecorator.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(XdsSpanDecorator.class::isInstance)
                .map(SpanDecorator::getComponent)
                .sorted()
                .collect(Collectors.toList());
    }

    @BeforeEach
    public void setUp() {
        camelContext = new DefaultCamelContext();
    }

    @AfterEach
    public void tearDown() {
        camelContext.stop();
    }

    private RecordingSpan decorate(String uri) {
        Endpoint endpoint = camelContext.getEndpoint(uri);
        var decorator = new SpanDecoratorManagerImpl().get(endpoint);
        assertInstanceOf(XdsSpanDecorator.class, decorator, uri + " resolved to " + decorator.getClass().getSimpleName() + " instead of an XdsSpanDecorator");
        var span = new RecordingSpan();
        decorator.beforeTracingEvent(span, new DefaultExchange(camelContext), endpoint);
        return span;
    }

    /**
     * Every scheme of this module must carry the transaction metadata, and the values must come from
     * the transaction configuration rather than from the URI.
     * <p>
     * The transaction code is not asserted to equal the scheme: a transaction may be reachable under
     * more than one scheme, in which case all of them report the same transaction. The scheme itself
     * is carried by the {@code url.scheme} tag.
     */
    @ParameterizedTest
    @MethodSource("schemes")
    public void testTransactionMetadataOfEveryScheme(String scheme) {
        var span = decorate(scheme + "://localhost:8888/service");

        var transaction = span.tags.get(IpfSpanDecorator.TAG_TRANSACTION);
        assertNotNull(transaction, scheme + " has no transaction code");
        assertFalse(transaction.isBlank(), scheme + " has a blank transaction code");
        assertEquals(scheme, span.tags.get("url.scheme"), scheme + " does not report its own scheme");

        var description = span.tags.get(IpfSpanDecorator.TAG_TRANSACTION_NAME);
        assertNotNull(description, scheme + " has no transaction description");
        assertFalse(description.isBlank(), scheme + " has a blank transaction description");

        var query = span.tags.get(IpfSpanDecorator.TAG_QUERY);
        assertTrue("true".equals(query) || "false".equals(query), scheme + " has no query flag");

        var profile = span.tags.get(XdsSpanDecorator.TAG_PROFILE);
        assertNotNull(profile, scheme + " has no IHE profile");
        assertFalse(profile.isBlank(), scheme + " has a blank IHE profile");
    }

    @Test
    public void testSpanIsNamedAfterTheTransaction() {
        var endpoint = camelContext.getEndpoint("xds-iti18://localhost:8888/service");
        var decorator = new SpanDecoratorManagerImpl().get(endpoint);
        assertEquals("xds-iti18", decorator.getOperationName(new DefaultExchange(camelContext), endpoint));
    }

    @Test
    public void testKnownMetadataOfIti18() {
        var span = decorate("xds-iti18://localhost:8888/service");
        assertEquals("xds-iti18", span.tags.get(IpfSpanDecorator.TAG_TRANSACTION));
        assertEquals("Registry Stored Query", span.tags.get(IpfSpanDecorator.TAG_TRANSACTION_NAME));
        assertEquals("true", span.tags.get(IpfSpanDecorator.TAG_QUERY));
        assertEquals("XDS", span.tags.get(XdsSpanDecorator.TAG_PROFILE));
    }

    /**
     * ITI-62 is reachable both as an XDS and as an RMD transaction -- the same component class under
     * two schemes -- so both report the one transaction they implement, distinguished by the scheme.
     */
    @Test
    public void testTransactionReachableUnderTwoSchemes() {
        var asXds = decorate("xds-iti62://localhost:8888/service");
        var asRmd = decorate("rmd-iti62://localhost:8888/service");

        assertEquals("xds-iti62", asXds.tags.get(IpfSpanDecorator.TAG_TRANSACTION));
        assertEquals("xds-iti62", asRmd.tags.get(IpfSpanDecorator.TAG_TRANSACTION));
        assertEquals("Delete Document Set", asRmd.tags.get(IpfSpanDecorator.TAG_TRANSACTION_NAME));

        assertEquals("xds-iti62", asXds.tags.get("url.scheme"));
        assertEquals("rmd-iti62", asRmd.tags.get("url.scheme"));
    }

    /** ITI-41 is a feed, not a query, and belongs to the same profile. */
    @Test
    public void testKnownMetadataOfIti41() {
        var span = decorate("xds-iti41://localhost:8888/service");
        assertEquals("false", span.tags.get(IpfSpanDecorator.TAG_QUERY));
        assertEquals("XDS", span.tags.get(XdsSpanDecorator.TAG_PROFILE));
    }

    /** Cross-community transactions are tagged with their own profile, not with XDS. */
    @Test
    public void testCrossCommunityProfile() {
        assertEquals("XCA", decorate("xca-iti38://localhost:8888/service").tags.get(XdsSpanDecorator.TAG_PROFILE));
        assertEquals("XCF", decorate("xcf-iti63://localhost:8888/service").tags.get(XdsSpanDecorator.TAG_PROFILE));
        assertEquals("RMU", decorate("rmu-iti92://localhost:8888/service").tags.get(XdsSpanDecorator.TAG_PROFILE));
    }

    @Test
    public void testHomeCommunityIdIsTaggedWhenConfigured() {
        var withHcid = decorate("xca-iti38://localhost:8888/service?homeCommunityId=urn:oid:1.2.3.4.5");
        assertEquals("urn:oid:1.2.3.4.5", withHcid.tags.get(WsSpanDecorator.TAG_HOME_COMMUNITY_ID));

        var withoutHcid = decorate("xca-iti38://localhost:8888/service");
        assertFalse(withoutHcid.tags.containsKey(WsSpanDecorator.TAG_HOME_COMMUNITY_ID));
    }

    @Test
    public void testSpanKindFollowsTheDirectionOfTheTransaction() {
        var endpoint = camelContext.getEndpoint("xds-iti18://localhost:8888/service");
        var decorator = new SpanDecoratorManagerImpl().get(endpoint);
        assertEquals("SERVER", decorator.getSpanKind(Op.EVENT_RECEIVED.name()));
        assertEquals("CLIENT", decorator.getSpanKind(Op.EVENT_SENT.name()));
    }

    /**
     * No tag may carry patient or document identifiers: traces are commonly stored outside the
     * perimeter that protects health data.
     */
    @ParameterizedTest
    @MethodSource("schemes")
    public void testNoTagIsDerivedFromTheMessage(String scheme) {
        var span = decorate(scheme + "://localhost:8888/service");
        var allowed = List.of(
                IpfSpanDecorator.TAG_TRANSACTION,
                IpfSpanDecorator.TAG_TRANSACTION_NAME,
                IpfSpanDecorator.TAG_QUERY,
                XdsSpanDecorator.TAG_PROFILE,
                WsSpanDecorator.TAG_HOME_COMMUNITY_ID,
                // contributed by Camel's AbstractSpanDecorator, all endpoint properties
                "url.scheme", "url.path", "url.query", "exchangeId", "camel.uri", "camel.route.id",
                "server.address", "server.protocol");
        var unexpected = span.tags.keySet().stream().filter(tag -> !allowed.contains(tag)).sorted().toList();
        assertEquals(List.of(), unexpected, scheme + " sets unreviewed tags: " + unexpected);
    }
}
