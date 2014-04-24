/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.model

import ca.uhn.hl7v2.conf.spec.message.*
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.validation.ValidationException
import ca.uhn.hl7v2.validation.impl.AbstractMessageRule
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.validation.support.AbstractSyntaxValidator

/**
 * Allows to add rules for checking the existence and cardinality of groups
 * and segments for a given message.
 * This is not intended to be a replacement for a full HL7 conformance profile
 * check, however, it quickly allows to check the message on group and segment
 * level using a syntax well known from HL7v2 specifications.
 * <p>
 * The differences from the HL7 Abstract Syntax and the DSL syntax provided
 * by this class are:
 * <p>
 * <ul>
 * <li> Structures are joined with a comma character
 * <li> Groups are explicitly written as Groovy method call (see example below)
 * </ul>
 * <p>
 * The cardinality of segments is:
 * <ul>
 * <li><b>XYZ</b>: (1..1)
 * <li><b>[ XYZ ]</b>: (0..1)
 * <li><b>{ XYZ }</b>: (1..*)
 * <li><b>[{ XYZ }]</b>: (0..*)
 * </ul>
 * <p>
 * Differing from the HL7 Abstract syntax, groups are written as method call, having the
 * contained structures as arguments, e.g. the HL7 Abstract Syntax:
 * <p>
 * <pre>
 * ...
 *             PROCEDURE
 * [{  PR1
 *  [  {  ROL  }  ]
 * }]
 *             PROCEDURE
 * </pre>
 * <p>becomes<p>
 * <pre>
 * ...
 *   [{ PROCEDURE(
 *        PR1,
 *        [ { ROL } ]
 *   )}]
 * ...
 * </pre>
 *
 *
 */
class AbstractSyntaxRule extends AbstractMessageRule {

    private AbstractSyntaxValidator validator
    private Object[] args

    private static final Map<String, StaticDef> staticDefCache = new LinkedHashMap<String, StaticDef>(10, 0.75f) {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 100;
        }
    }

    AbstractSyntaxRule(Object... args) {
        this.args = args
        this.validator = new AbstractSyntaxValidator()
    }

    @Override
    ValidationException[] apply(Message msg) {

        String msgStructID = MessageUtils.messageStructure(msg)
        StaticDef staticDef
        synchronized (this) {
            if (staticDefCache.containsKey(msgStructID)) {
                staticDef = staticDefCache.get(msgStructID)
            } else {
                staticDef = new StaticDef()
                staticDef.msgStructID = msgStructID
                staticDef.eventType = MessageUtils.triggerEvent(msg)
                staticDef.msgType = MessageUtils.eventType(msg)
                analyzeSegments(staticDef, 1, args)
                staticDefCache.put(msgStructID, staticDef)
            }
        }
        validator.validate(msg, staticDef)
    }

    protected def analyzeSegments(AbstractSegmentContainer container, int pos, Object... args) {
        args.each {
            if (it instanceof String) {
                // Required segment
                segment(container, pos++, it, 1, 1)
            } else if (it instanceof Collection) {
                if (it[0] instanceof String) {
                    // Optional segment
                    segment(container, pos++, it[0], 0, 1)
                } else if (it[0] instanceof Closure) {
                    it[0].delegate = this
                    def result = it[0].call()
                    if (result instanceof String) {
                        // Optional repeatable segment
                        segment(container, pos++, result, 0, 255)
                    } else {
                        // Optional repeatable group
                        group(container, pos++, result.name, 0, 255, result.args)
                    }
                } else if (it[0] instanceof Map) {
                    group(container, pos++, it[0].name, 0, 255, it[0].args)
                }

            } else if (it instanceof Closure) {
                it.delegate = this
                def result = it.call()
                if (result instanceof String) {
                    // Repeatable segment
                    segment(container, pos++, result, 1, 255)
                } else if (result instanceof Collection) {
                    if (result[0] instanceof String) {
                        // Repeatable optional segment.
                        segment(container, pos++, result[0], 0, 255)
                    } else {
                        // Optional repeatable group
                        group(container, pos++, result[0].name, 0, 255, result[0].args)
                    }
                } else if (result instanceof Map) {
                    group(container, pos++, result.name, 1, 255, result.args)
                }
            }
        }
    }

    // Called on groups.
    def methodMissing(String name, args) {
        return [name: name, args: args]
    }

    // Adds a group to the container and calls analyzeSegment recursively
    protected void group(AbstractSegmentContainer container, int pos, String name, int min, int max, args) {
        AbstractSegmentContainer p = new SegGroup([min: min, max: max, name: name])
        p.usage = usage(min, max)
        container.setChild(pos, p)
        analyzeSegments(p, 1, args)
    }

    // Adds a segment to the container
    protected void segment(AbstractSegmentContainer container, int pos, String name, int min, int max) {
        ProfileStructure p = new Seg([min: min, max: max, name: name])
        p.usage = usage(min, max)
        container.setChild(pos, p)
    }

    private def usage(min, max) {
        return min == 0 ?
                (max == 0 ? 'X' : 'O') :
                'R'
    }
}
