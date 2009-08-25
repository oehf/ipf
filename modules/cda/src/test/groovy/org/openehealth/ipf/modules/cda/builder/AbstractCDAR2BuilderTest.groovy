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
package org.openehealth.ipf.modules.cda.builder

import org.openehealth.ipf.modules.cda.AbstractCDAR2Testimport org.junit.Before
/**
 * @author Christian Ohr
 */
public abstract class AbstractCDAR2BuilderTest extends AbstractCDAR2Test {
	
	static def builder
	static def loaded = []
	
	@Before
	public void setUp() throws Exception {
		if (!builder)
            builder = new CDAR2Builder(getClass().getClassLoader()) 
	}
		
}