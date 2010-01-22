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
import ca.uhn.hl7v2.util.ReadOnlyMessageIterator
import ca.uhn.hl7v2.parser.EncodingCharacters
import ca.uhn.hl7v2.parser.PipeParser;
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adapt

/**
 * @author Christian Ohr
 */
class GroupAdapterIterator extends ReadOnlyMessageIterator {
	
	private static final EncodingCharacters ec = new EncodingCharacters('|' as char, "^~\\&")

	static FilterIterator iterator(GroupAdapter adapter) {
		iterator(adapter.target)
	}
	
	/**
	 * TODO hardcoded to return groups and non-empty segments. Shall this be customizable?
	 * 
	 * @param group
	 * @return
	 */
	static FilterIterator iterator(Group group) {
		Iterator iterator = new GroupAdapterIterator(group)
		DelegatingPredicate predicate = new DelegatingPredicate( {
			! it.isEmpty()
		})
		new FilterIterator(iterator, predicate)
	}
	
	private GroupAdapterIterator(Group group) {
		super(group)
	}
	
	@Override
	public Object next() {
		adapt(super.next())
	}
	
}