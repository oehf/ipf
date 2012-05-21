/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.cda

import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Act
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import groovy.util.slurpersupport.GPathResult
import groovy.util.XmlSlurper


/**
 * Abstract validator class from which the CDA validators should inherit from.
 * It provides a variety of helper assert methods, each of them throw an
 * {@link ValidationException} if the assertion is not true.
 * 
 * @author Christian Ohr
 */
public abstract class AbstractValidator implements Validator {
	
    GPathResult vocabulary
    
    AbstractValidator() {        
    }
    
    AbstractValidator(String vocabularyURL) {
        this.vocabulary = new XmlSlurper().parse(getClass().getResourceAsStream(vocabularyURL))
    }
    
    static void fail(constraint, message) {
        throw new ValidationException("$constraint violated. $message")
    }
    
	static void assertTrue(constraint, condition) {
		condition = resolve(condition)
		if (!condition) {
			throw new ValidationException("$constraint violated. Condition does not match")
		}
	}
	
	static void assertFalse(constraint, condition) {
		condition = resolve(condition)
		if (condition) {
			throw new ValidationException("$constraint violated. Condition does match")
		}
	}
	
	static void assertNull(constraint, object) {
		if (object != null) {
			throw new ValidationException("$constraint violated. Object must be null")            
		}
	}
	
	static void assertNotNull(constraint, object) {
		if (object == null) {
			throw new ValidationException("$constraint violated. Object must not be null")            
		}
	}
	
	static void assertEquals(constraint, condition, object) {
		condition = resolve(condition, object)
		if (!(object.equals(condition))) {
			throw new ValidationException("$constraint violated. Object must be $condition, but was $object")            
		}
	}
	
	static void assertNotEquals(constraint, condition, object) {
		condition = resolve(condition, object)
		if (object.equals(condition)) {
			throw new ValidationException("$constraint violated. Object must not be $condition")            
		}
	}
	
	static void assertMatches(constraint, condition, object) {
		condition = resolve(condition, object)
		if (!(object.matches(condition))) {
			throw new ValidationException("$constraint violated. Object must match $condition, but was $object")            
		}
	}
	
	static void assertNotMatches(constraint, condition, object) {
		condition = resolve(condition, object)
		if (object.matches(condition)) {
			throw new ValidationException("$constraint violated. Object must not match $condition")            
		}
	}
	
	static void assertContains(constraint, condition, List object) {
		condition = resolve(condition, object)
		if (!(condition in object)) {
			throw new ValidationException("$constraint violated. Object must be $condition, but was $object")            
		}
	}
	
	static void assertNotContains(constraint, condition, List object) {
		condition = resolve(condition, object)
		if (condition in object) {
			throw new ValidationException("$constraint violated. Object $object must not be $condition, but was $object")            
		}
	}
	
	static void assertOneOf(constraint, List condition, object) {
		condition = resolve(condition, object)
		if (!(object in condition)) {
			throw new ValidationException("$constraint violated. Object must be one of $condition, but was $object")            
		}
	}
	
	static void assertNoneOf(constraint, List condition, object) {
		condition = resolve(condition, object)
		if (object in condition) {
			throw new ValidationException("$constraint violated. Object must not be one of $condition, but was $object")            
		}
	}    
	
	static void assertMinSize(constraint, minSize, List object) {
		minSize = resolve(minSize, object)
		if (object.size() < minSize) {
			throw new ValidationException("$constraint violated. Object must have at least $minSize element(s), but has ${object.size}")            
		}
	}
	
	static void assertMaxSize(constraint, maxSize, List object) {
		maxSize = resolve(maxSize, object)
		if (object.size() > maxSize) {
			throw new ValidationException("$constraint violated. Object must have at most $maxSize element(s), but has ${object.size}")            
		}
	}
	
	static void assertSize(constraint, size, List object) {
		size = resolve(size, object)
		if (object.size() != size) {
			throw new ValidationException("$constraint violated. Object must have $size element(s), but has ${object.size}")            
		}
	}    
	
	static void assertInstanceOf(constraint, Class condition, object) {
		condition = resolve(condition, object)
		if (object.class.isAssignableFrom(condition)) {
			throw new ValidationException("$constraint violated. Object must be of type $condition, but was ${object.class}")            
		}
	}
	
	void assertCode(constraint, root, code) {
	    def codeSystem = vocabulary.system.find { 
	        it.@root == root
	    }
	    def found = codeSystem?.code.findAll {
	        it.@value == code.code && 
	        it.@codeSystem == code.codeSystem
	    }.size()
	    if (found == 0) {
	        throw new ValidationException("$constraint violated. Code ${code.code} is not in ValueSet ${codeSystem.@root}")
	    }
	}
	
	
	private static def resolve(condition) {
		if (condition instanceof Closure) {
			condition.call()
		} else {
			condition
		}
	}
	
	private static def resolve(condition, object) {
		if (condition instanceof Closure) {
			condition.call(object)
		} else {
			condition
		}    
	}
	
}

