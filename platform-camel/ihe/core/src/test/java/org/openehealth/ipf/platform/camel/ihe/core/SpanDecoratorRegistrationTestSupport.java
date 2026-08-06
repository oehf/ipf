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
package org.openehealth.ipf.platform.camel.ihe.core;

import org.apache.camel.Component;
import org.apache.camel.telemetry.SpanDecorator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Guards the completeness of the telemetry support of a transaction module: every endpoint scheme it
 * contributes to Camel must also have a span decorator, otherwise the transaction silently falls back
 * to Camel's default decorator and loses its metadata.
 * <p>
 * A newly added transaction fails these checks until its decorator is written and registered, which
 * is what keeps the coverage of the eHealth transactions honest as they are added.
 * <p>
 * Both the endpoint schemes and the span decorators are read from the service descriptors of the
 * module under test, recognized by being present as files rather than inside a jar. Neither package
 * names nor class names are relied upon, so modules that share package names -- as the FHIR modules
 * of different FHIR versions do -- are handled correctly.
 * <p>
 * Subclasses add nothing; they merely place the checks in the module to be guarded.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class SpanDecoratorRegistrationTestSupport {

    private static final String COMPONENT_DESCRIPTORS = "META-INF/services/org/apache/camel/component/";
    private static final String DECORATOR_DESCRIPTOR = "META-INF/services/org.apache.camel.telemetry.SpanDecorator";

    /**
     * Endpoint schemes that are a synonym for another transaction, mapped to the name that transaction
     * carries. Both schemes stay separately decorated: {@code camel-telemetry} keys its decorators on
     * {@link SpanDecorator#getComponent()}, so two decorators returning the same string would leave one
     * of the two schemes without telemetry.
     */
    private static final Map<String, String> SYNONYM_SCHEMES = Map.of(
            "xds-iti8", "pix-iti8",
            "rmd-iti62", "xds-iti62");

    /**
     * Endpoint schemes whose component has no {@code InteractionId} to ask for a transaction name, with
     * the reason. Kept explicit rather than skipped silently, and asserted to still apply by
     * {@link #testTheExceptionsAreStillNeeded()}.
     */
    private static final Map<String, String> WITHOUT_INTERACTION_ID = Map.of(
            "fhir", "generic component, not bound to a single transaction",
            "mllp", "generic component, not bound to a single transaction",
            "mllp-dispatch", "dispatching component, serves several transactions",
            "mhd-iti68", "served by a ServletComponent, which is not interaction aware",
            "mhd-iti68-bin", "served by a ServletComponent, which is not interaction aware");

    /**
     * Endpoint schemes whose transaction carries options taken from the endpoint URI, so that its
     * configuration is built only once a component is configured -- long after a span decorator is
     * registered. Whether it exists yet therefore depends on what else has run in the same JVM, which
     * is why these schemes are compared when the configuration happens to be there and passed over when
     * it is not. Asserting its absence instead would make these tests depend on execution order.
     */
    private static final Map<String, String> LAZILY_CONFIGURED = Map.of(
            "pam-iti30", "Hl7v2InteractionId#init",
            "pam-iti31", "Hl7v2InteractionId#init",
            "mhd-iti66", "FhirInteractionId#init",
            "mhd-iti67", "FhirInteractionId#init",
            "qedm-pcc44", "FhirInteractionId#init");

    /**
     * @return the endpoint schemes the module under test contributes to Camel, which are the names of
     *      the component descriptor files.
     */
    private Set<String> endpointSchemes() {
        var schemes = new TreeSet<String>();
        for (Path directory : ownResources(COMPONENT_DESCRIPTORS)) {
            try (var entries = Files.list(directory)) {
                entries.forEach(entry -> schemes.add(entry.getFileName().toString()));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return schemes;
    }

    /**
     * @return the span decorators the module under test registers, instantiated the same way
     *      {@link java.util.ServiceLoader} would.
     */
    private List<SpanDecorator> registeredDecorators() {
        var decorators = new ArrayList<SpanDecorator>();
        for (Path descriptor : ownResources(DECORATOR_DESCRIPTOR)) {
            try {
                for (String line : Files.readAllLines(descriptor, StandardCharsets.UTF_8)) {
                    var className = line.replaceAll("#.*", "").trim();
                    if (!className.isEmpty()) {
                        decorators.add(Class.forName(className)
                                .asSubclass(SpanDecorator.class)
                                .getDeclaredConstructor()
                                .newInstance());
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("cannot instantiate the decorators listed in " + descriptor, e);
            }
        }
        return decorators;
    }

    /**
     * @return the given classpath resource, as far as the module under test ships it. Resources of the
     *      modules it depends on come from jars and are skipped, and so are those of its own test
     *      fixtures: a component declared only for a test is not something the module ships, and
     *      therefore needs no span decorator.
     */
    private List<Path> ownResources(String name) {
        try {
            var paths = new ArrayList<Path>();
            for (URL url : Collections.list(getClass().getClassLoader().getResources(name))) {
                if ("file".equals(url.getProtocol()) && !url.getPath().contains("test-classes")) {
                    paths.add(Path.of(url.toURI()));
                }
            }
            return paths;
        } catch (Exception e) {
            throw new IllegalStateException("cannot enumerate " + name, e);
        }
    }

    /**
     * @param scheme an endpoint scheme the module under test contributes.
     * @return the name of the transaction the component registered under that scheme serves, or
     *      {@code null} if the component does not stand for a single transaction or its configuration
     *      is not available yet. A string in angle brackets is returned when the component could not be
     *      examined at all, so that the assertion message says why rather than just "null".
     */
    private String transactionName(String scheme) {
        Component component;
        try {
            component = Class.forName(componentClassName(scheme))
                    .asSubclass(Component.class)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception | LinkageError e) {
            return "<cannot instantiate the component: " + e + ">";
        }
        if (!(component instanceof InteractionAwareComponent interactionAware)) {
            return null;
        }
        var interactionId = interactionAware.getInteractionId();
        if (interactionId == null || interactionId.getTransactionConfiguration() == null) {
            return null;
        }
        return interactionId.getTransactionConfiguration().getName();
    }

    /**
     * @return the component class the descriptor of the given scheme names.
     */
    private String componentClassName(String scheme) {
        for (Path directory : ownResources(COMPONENT_DESCRIPTORS)) {
            var descriptor = directory.resolve(scheme);
            if (Files.exists(descriptor)) {
                try {
                    for (String line : Files.readAllLines(descriptor, StandardCharsets.UTF_8)) {
                        var trimmed = line.trim();
                        if (trimmed.startsWith("class=")) {
                            return trimmed.substring("class=".length()).trim();
                        }
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        throw new IllegalStateException("no class= entry in the component descriptor of " + scheme);
    }

    /**
     * @return the transaction name expected for the given scheme, which is the scheme itself unless the
     *      scheme is a synonym for another transaction.
     */
    private static String expectedTransactionName(String scheme) {
        return SYNONYM_SCHEMES.getOrDefault(scheme, scheme);
    }

    private Set<String> decoratedSchemes() {
        return registeredDecorators().stream()
                .map(SpanDecorator::getComponent)
                .collect(java.util.stream.Collectors.toCollection(TreeSet::new));
    }

    @Test
    public void testTheModuleContributesEndpointSchemes() {
        assertFalse(endpointSchemes().isEmpty(),
                "no Camel component descriptors of the module under test found -- nothing can be guarded");
    }

    @Test
    public void testEverySchemeHasADecorator() {
        var undecorated = new TreeSet<>(endpointSchemes());
        undecorated.removeAll(decoratedSchemes());
        assertEquals(Set.of(), undecorated,
                "endpoint schemes without a span decorator: " + undecorated
                        + " -- add one and list it in " + DECORATOR_DESCRIPTOR);
    }

    @Test
    public void testNoDecoratorClaimsAnUnknownScheme() {
        var orphaned = new TreeSet<>(decoratedSchemes());
        orphaned.removeAll(endpointSchemes());
        assertEquals(Set.of(), orphaned, "span decorators for schemes this module does not provide: " + orphaned);
    }

    @Test
    public void testEachDecoratorClaimsExactlyOneScheme() {
        assertEquals(registeredDecorators().size(), decoratedSchemes().size(),
                "two decorators claim the same scheme, so one of them would be dropped by camel-telemetry");
    }

    /**
     * The span decorators name their scheme with a string literal, which for nearly every transaction
     * repeats the name its {@code TransactionConfiguration} already carries. The duplication is not
     * avoidable -- {@code camel-telemetry} keys its decorators on {@link SpanDecorator#getComponent()}
     * while building a static map, i.e. before any endpoint exists to derive the name from -- so it is
     * pinned here instead: together with the checks above, which tie every scheme to exactly one
     * decorator, this asserts that what a decorator returns is what the transaction is called.
     * <p>
     * A transaction whose name and scheme deliberately differ belongs in {@link #SYNONYM_SCHEMES}.
     */
    @Test
    public void testEverySchemeIsNamedAfterItsTransaction() {
        var mismatches = new TreeMap<String, String>();
        for (String scheme : endpointSchemes()) {
            if (WITHOUT_INTERACTION_ID.containsKey(scheme)) {
                continue;
            }
            var name = transactionName(scheme);
            if (name == null && LAZILY_CONFIGURED.containsKey(scheme)) {
                continue;
            }
            if (!expectedTransactionName(scheme).equals(name)) {
                mismatches.put(scheme, String.valueOf(name));
            }
        }
        assertEquals(Map.of(), mismatches,
                "these endpoint schemes do not match the name of the transaction they serve (scheme -> name): "
                        + mismatches + " -- rename one of the two, or, if they are meant to differ, add the"
                        + " scheme to SYNONYM_SCHEMES or WITHOUT_INTERACTION_ID with a reason");
    }

    /**
     * Keeps the exception lists from rotting: an entry that is no longer justified would silently
     * exempt a scheme from the check above. {@link #LAZILY_CONFIGURED} is not checked here, because an
     * absent configuration is what that list describes rather than evidence that the entry is stale.
     */
    @Test
    public void testTheExceptionsAreStillNeeded() {
        var obsolete = new TreeMap<String, String>();
        for (String scheme : endpointSchemes()) {
            var name = transactionName(scheme);
            if (WITHOUT_INTERACTION_ID.containsKey(scheme) && name != null) {
                obsolete.put(scheme, "now has a transaction name (" + name + "), remove it from WITHOUT_INTERACTION_ID");
            } else if (SYNONYM_SCHEMES.containsKey(scheme) && scheme.equals(name)) {
                obsolete.put(scheme, "no longer a synonym, remove it from SYNONYM_SCHEMES");
            }
        }
        assertEquals(Map.of(), obsolete, "obsolete exceptions: " + obsolete);
    }

    /**
     * Matching by component class name would be no more precise than matching by scheme, and IPF
     * relies on the latter throughout.
     */
    @Test
    public void testDecoratorsDoNotMatchByComponentClassName() {
        var withClassName = registeredDecorators().stream()
                .filter(decorator -> decorator.getComponentClassName() != null)
                .map(decorator -> decorator.getClass().getSimpleName())
                .sorted()
                .toList();
        assertEquals(List.of(), withClassName);
    }
}
