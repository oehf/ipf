/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;

/**
 * Tests the fix of issue #69 (Problem with RSP_K21.getRSP_K21_QUERY_RESPONSE())
 * 
 * @author Mitko Kolev
 */
public class RSP_K21Test {

	@Test
	public void testGetQUERY_RESPONSE(){
		//Fix of issue #69 (Problem with RSP_K21.getRSP_K21_QUERY_RESPONSE())
		RSP_K21 msg = new RSP_K21();
		RSP_K21_QUERY_RESPONSE response = msg.getQUERY_RESPONSE();
		assertNotNull (response);
		assertTrue(response instanceof RSP_K21_QUERY_RESPONSE);
	}
	
	@Test
	public void testGetRSP_K21_QUERY_RESPONSEReps(){
		RSP_K21 msg = new RSP_K21();
		assertEquals(0 , msg.getQUERY_RESPONSEReps());
	}
	
	@Test
	public void testGetRSP_K21_QUERY_RESPONSERepsWithParams() throws Exception {
		RSP_K21 msg = new RSP_K21();
		RSP_K21_QUERY_RESPONSE response = msg.getQUERY_RESPONSE(0);
		assertNotNull (response);
		response = msg.getQUERY_RESPONSE(1);
		assertNotNull (response);
	}
}
