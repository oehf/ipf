/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.modules.hl7dsl

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.util.FilterIterator
import ca.uhn.hl7v2.parser.EncodingCharacters
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adapt

/**
 * Iterator class that does a depth-first traversal over message classes.
 * Inspired by HAPI's {@link ca.uhn.hl7v2.util.ReadOnlyMessageIterator}
 * 
 * @author Christian Ohr
 */
class GroupAdapterIterator implements Iterator {
	
	private static final EncodingCharacters ec = new EncodingCharacters('|' as char, "^~\\&")
	
	private List<? extends StructureAdapter> remainingStructures = []
	
	/**
	 * 
	 * @param group
	 * @return depth-first traversing iterator
	 */
	static FilterIterator iterator(Group group) {
		iterator(adapt(group))
	}
	
	static FilterIterator iterator(GroupAdapter group) {
		Iterator iterator = new GroupAdapterIterator(group)
		DelegatingPredicate predicate = new DelegatingPredicate( { ! it.isEmpty() })
		new FilterIterator(iterator, predicate)
	}
	
	private GroupAdapterIterator(GroupAdapter group) {
		addChildren(group)
	}
	
	@Override
	public Object next() {
		if (!hasNext()) { 
			throw new NoSuchElementException("No more structures in message")
		}
		addChildren(remainingStructures.pop());
	}
	
	private StructureAdapter addChildren(SegmentAdapter segment) {
		segment
	}
	
	private StructureAdapter addChildren(GroupAdapter group) {
		group?.names?.reverseEach { name ->
			reverseEachWithIndex(group.getAll(name)) { adapter, i ->
				remainingStructures << adapter.withPath(group, i)
			}
		}
		group
	}
		
	@Override
	public boolean hasNext() {
		! remainingStructures.isEmpty()
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not applicable")
	}
	
	private static void reverseEachWithIndex(List<StructureAdapter> a, Closure closure) {
		for (int i = a.size() - 1; i >= 0; i--) {
			closure.call(a[i], i)
		}
	}
	
}