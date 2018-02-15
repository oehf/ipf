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
package org.openehealth.ipf.modules

import junit.framework.Test
import junit.framework.TestSuite
import org.openehealth.ipf.modules.cda.*
import org.openehealth.ipf.modules.cda.builder.*

/**
 * @author: Stefan Ivanov
 */
public class CDAR2TestSuite{
  public static Test suite() {
    TestSuite suite = new TestSuite()
    suite.addTestSuite(CDAR2BuilderComponent2Test.class)
    suite.addTestSuite(CDAR2BuilderStructureActTest.class)
    suite.addTestSuite(CDAR2BuilderStructureEncounterTest.class)
    suite.addTestSuite(CDAR2BuilderStructureObservationTest.class)
    suite.addTestSuite(CDAR2BuilderStructureObservationMediaTest.class)
    suite.addTestSuite(CDAR2BuilderStructureOrganizerTest.class)
    suite.addTestSuite(CDAR2BuilderStructureProcedureTest.class)
    suite.addTestSuite(CDAR2BuilderStructureRegionOfInterestTest.class)
    suite.addTestSuite(CDAR2BuilderStructureSubstanceAdministrationTest.class)
    suite.addTestSuite(CDAR2BuilderStructureSupplyTest.class)
    suite.addTestSuite(CDAR2NarrativeTest.class)
    // suite.addTestSuite(CDAR2ExtractorTest.class);
    return suite
  }
  
}