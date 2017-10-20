/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.message

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.Location
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.MessageVisitorSupport
import ca.uhn.hl7v2.model.Segment

import static ca.uhn.hl7v2.model.MessageVisitors.visit
import static ca.uhn.hl7v2.model.MessageVisitors.visitStructures

/**
 * Tool class that provides methods that collect structure locations
 */
final class Visitors {

    static Group eachWithIndex(Group group, Closure<?> c) {
        EachStructureVisitor visitor = new EachStructureVisitor(c)
        visit(group, visitStructures(visitor))
        group
    }

    static List<String> findIndexValues(Group group, Closure<?> c) {
        FindStructureVisitor visitor = new FindStructureVisitor(c, false)
        visit(group, visitStructures(visitor))
        List<String> result = visitor.paths.collect { terserToDsl(it) }
        result
    }

    static String findIndexOf(Group group, Closure<?> c) {
        FindStructureVisitor visitor = new FindStructureVisitor(c, true)
        visit(group, visitStructures(visitor))
        Location location = visitor.paths.isEmpty() ? null : visitor.paths[0]
        terserToDsl(location)
    }

    static String findLastIndexOf(Group group, Closure<?> c) {
        FindStructureVisitor visitor = new FindStructureVisitor(c, false)
        visit(group, visitStructures(visitor))
        Location location = visitor.paths.isEmpty() ? null : visitor.paths[visitor.paths.size() - 1]
        terserToDsl(location)
    }

    private static String terserToDsl(Location location) {
        if (location != null) {
            String terserSpec = location.toString()
            int start = terserSpec.startsWith('/') ? 1 : 0
            int end = terserSpec.endsWith('/') ? terserSpec.length() - 1 : terserSpec.length()
            return terserSpec.substring(start, end).replace('/', '.')
        }
        return null
    }

    private static class FindStructureVisitor extends MessageVisitorSupport {

        List<Location> paths = new ArrayList<>()
        private final Closure<?> closure
        private final boolean findFirst

        FindStructureVisitor(Closure<?> closure, boolean findFirst) {
            this.closure = closure
            this.findFirst = findFirst
        }

        @Override
        boolean end(Group group, Location location) throws HL7Exception {
            if (!done() && closure.call(group)) {
                paths.add(location)
            }
            !done()
        }

        @Override
        boolean end(Segment segment, Location location) throws HL7Exception {
            if (!done() && closure.call(segment)) {
                paths.add(location)
            }
            !done()
        }

        boolean done() {
            boolean found = findFirst && !paths.isEmpty()
            found
        }

    }

    private static class EachStructureVisitor extends MessageVisitorSupport {
        private final Closure<?> closure

        EachStructureVisitor(Closure<?> closure) {
            this.closure = closure
        }

        @Override
        boolean end(Group group, Location location) throws HL7Exception {
            closure.call(group, terserToDsl(location))
            super.end(group, location)
        }

        @Override
        boolean end(Segment segment, Location location) throws HL7Exception {
            closure.call(segment, terserToDsl(location))
            super.end(segment, location)
        }
    }
}
