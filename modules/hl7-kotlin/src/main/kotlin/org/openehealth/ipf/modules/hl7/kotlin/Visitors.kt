/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin

import ca.uhn.hl7v2.Location
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.MessageVisitorSupport
import ca.uhn.hl7v2.model.MessageVisitors.visitStructures
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Structure

/**
 * @author Christian Ohr
 */
object Visitors {


    fun eachWithIndex(group: Group, c: (Structure, String) -> Unit): Group = group.apply {
        accept(visitStructures(EachStructureLocationVisitor(c)))
    }

    fun findIndexValues(group: Group, c: (Structure) -> Boolean) =
            findPairs(group, c).map { it.first }

    fun findIndexOf(group: Group, c: (Structure) -> Boolean): String? =
            findPairs(group, c, true).firstOrNull()?.first

    fun findLastIndexOf(group: Group, c: (Structure) -> Boolean): String? =
            findPairs(group, c).lastOrNull()?.first

    /**
     * It is easier to access the first/last element from a list of pairs instead of from a map
     */
    private fun findPairs(group: Group, c: (Structure) -> Boolean, findFirst: Boolean = false): List<Pair<String, Structure>> {
        val visitor = FindStructureVisitor(c, findFirst)
        group.accept(visitStructures(visitor))
        return visitor.paths.map { terserToDsl(it.first) to it.second }
    }

}


private class FindStructureVisitor(
        private val test: (Structure) -> Boolean,
        private val findFirst: Boolean) : MessageVisitorSupport() {

    val paths = ArrayList<Pair<Location, Structure>>()

    override fun end(group: Group, location: Location): Boolean {
        if (!done() && test(group)) {
            paths.add(location to group)
        }
        return !done()
    }

    override fun end(segment: Segment, location: Location): Boolean {
        if (!done() && test(segment)) {
            paths.add(location to segment)
        }
        return !done()
    }

    private fun done(): Boolean = findFirst && !paths.isEmpty()

}

private class EachStructureLocationVisitor(private val consume: (Structure, String) -> Unit) : MessageVisitorSupport() {

    override fun end(group: Group, location: Location): Boolean {
        consume(group, terserToDsl(location))
        return super.end(group, location)
    }

    override fun end(segment: Segment, location: Location): Boolean {
        consume(segment, terserToDsl(location))
        return super.end(segment, location)
    }
}

// TODO adapt this to kotlin syntax?
private fun terserToDsl(location: Location): String {
    val terserSpec = location.toString()
    val start = if (terserSpec.startsWith('/')) 1 else 0
    val end = if (terserSpec.endsWith('/')) terserSpec.length - 1 else terserSpec.length
    return terserSpec.substring(start, end).replace('/', '.')
}
