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
package org.openehealth.ipf.modules.hl7dsl;

import org.codehaus.groovy.runtime.InvokerInvocationException;

import ca.uhn.hl7v2.util.FilterIterator

/**
 * Bridge between HAPI Predicate and Closure
 * 
 * @author Christian Ohr
 *
 */
class DelegatingPredicate implements FilterIterator.Predicate {
	
	private Closure closure
	
	public DelegatingPredicate(Closure closure) {
		super();
		this.closure = closure;
	}

	@Override
	public boolean evaluate(Object args) {
		(Boolean)callClosure(args)
	}
	
	private Object callClosure(Object args) {
		try {
			return closure.call(args);
		} catch (InvokerInvocationException e) {
			if (e.getCause() instanceof RuntimeException) {
				throw (RuntimeException)e.getCause();
			} else {
				throw e;
			}
		}
	}	
	
}
