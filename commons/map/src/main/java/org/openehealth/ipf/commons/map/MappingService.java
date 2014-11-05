/*
 * Copyright 2008-2009 the original author or authors.
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
package org.openehealth.ipf.commons.map;

import java.util.Collection;
import java.util.Set;

/**
 * Interface for mapping a set of keysets into corresponding values.
 * <p>
 * The back end mapping implementation is not defined by this interface. Examples
 * could be a simple {@link java.util.Map} or a remote terminology service, for
 * which proper implementations of this interface must be provided.
 * <p> 
 * 
 * @author Christian Ohr
 *
 */
public interface MappingService {

	/**
	 * @param mappingKey mapping name
	 * @param key left side of the mapping
	 * @return the value mapped by a registered mapping. The implementation of the
	 *         mapping implements the behavior if the key does not exist.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object get(Object mappingKey, Object key);

	/**
	 * @param mappingKey mapping name
	 * @param key left side of the mapping
	 * @param defaultValue default right side if there is no mapping for the code
	 * @return the value mapped by a registered mapping. If no mapping exists, the
	 *         default value if returned. The implementation of the mapping
	 *         implements the behavior if the key does not exist.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object get(Object mappingKey, Object key, Object defaultValue);

	/**
	 * Reverse mapping (Optional implementation)
	 * 
	 * @param mappingKey mapping name
	 * @param value right side of the mapping
	 * @return the key mapped by a registered mapping. The implementation of the
	 *         mapping implements the behavior if the value does not exist.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object getKey(Object mappingKey, Object value);

	/**
	 * Reverse mapping (Optional implementation)
	 * 
	 * @param mappingKey mapping name
	 * @param value right side of the mapping
	 * @param defaultKey default left side
	 * @return the key mapped by a registered mapping. If no mapping exists, the
	 *         default value if returned. The implementation of the mapping
	 *         implements the behavior if the key does not exist.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object getKey(Object mappingKey, Object value, Object defaultKey);
	
	/**
	 * @param mappingKey mapping name
	 * @return a formal identification of the key system (e.g. an OID)
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object getKeySystem(Object mappingKey);

	/**
	 * @param mappingKey mapping name
	 * @return a formal identification of the value system (e.g. an OID)
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Object getValueSystem(Object mappingKey);

	/**
	 * @return a set of names of all registered mappings. Changes to the set do
	 *         not change the registry itself.
	 */
	Set<?> mappingKeys();

	/**
	 * @param mappingKey
	 * @return key set of a registered mapping. Changes to the set do not change
	 *         the registry itself.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Set<?> keys(Object mappingKey);

	/**
	 * @param mappingKey
	 * @return value set of a registered mapping. Changes to the set do not
	 *         change the registry itself.
	 * @throws IllegalArgumentException
	 *             if the mappingKey is not registered.
	 */
	Collection<?> values(Object mappingKey);

}
