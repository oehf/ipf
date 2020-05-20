/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.core.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * JUnit Rule allowing conditional execution of tests or whole test classes. The condition
 * must implement {@link Predicate} with a {@link Void} type parameter.
 * <p/>
 * Example:
 * <pre>
 *     public class SomeTest {
 *
 *        &#064;Rule public Conditional rule = Conditional.skipOnWindows();
 *
 *        &#064;Test
 *        public void testNotOnWindows() {
 *           rule.verify();
 *       }
 *
 *        &#064;Test
 *        public void testOnlyWindows() {
 *           rule.negate().verify();
 *       }
 *    }
 * </pre>
 */
public class ConditionalRule implements TestRule {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionalRule.class);
    private Predicate<Void> predicate;
    private String reason;

    public ConditionalRule() {
    }

    // Build a condition

    public ConditionalRule runIf(Predicate<Void> condition) {
        this.predicate = condition;
        return this;
    }

    public ConditionalRule ifSystemProperty(final String property, final Predicate<String> condition) {
        return runIf(source ->
                condition.test(System.getProperty(property))).reason("condition matches on system property " + property);
    }

    public ConditionalRule ifSystemPropertyIs(final String property, final String value) {
        return ifSystemProperty(property, s ->
                s != null && s.equalsIgnoreCase(value)).reason("system property " + property + " equals " + value);
    }
    public ConditionalRule negate() {
        if (predicate == null)
            throw new IllegalArgumentException("Must have defined a condition before negating it");
        predicate = predicate.negate();
        reason = "not " + reason;
        return this;
    }

    public ConditionalRule skipOnWindows() {
        return ifSystemProperty("os.name", s ->
                s.startsWith("Windows")).negate().reason("skipped because running on Windows");
    }

    public ConditionalRule skipOnTravis() {
        return ifSystemPropertyIs("TRAVIS", "true")
            .negate().reason("skipped because running on Travis");
    }

    public ConditionalRule reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void verify() {
        if (predicate != null) {
            if (!predicate.test(null)) throw new IgnoredException();
        }
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new ConditionalStatement(this, base, description);
    }

    public String getReason() {
        return reason;
    }

    private static class ConditionalStatement extends Statement {
        private final Statement statement;
        private final Description description;
        private final ConditionalRule conditionalRule;

        public ConditionalStatement(ConditionalRule conditionalRule, Statement statement, Description description) {
            this.statement = statement;
            this.conditionalRule = conditionalRule;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                conditionalRule.verify();
                statement.evaluate();
            } catch (Throwable e) {
                if (isIgnoredException(e)) {
                    LOG.warn("Did not execute test {}: {} ", description, conditionalRule.getReason());
                    return;
                }
                throw e;
            }
        }
    }

    private static boolean isIgnoredException(Throwable throwable) {
        var t = throwable;
        List<Throwable> list = new ArrayList<>();
        while (t != null && !list.contains(t)) {
            if (t instanceof IgnoredException) return true;
            list.add(throwable);
            t = t.getCause();
        }
        return false;
    }

    private static class IgnoredException extends RuntimeException {

        public IgnoredException() {
            super();
        }
    }

}
