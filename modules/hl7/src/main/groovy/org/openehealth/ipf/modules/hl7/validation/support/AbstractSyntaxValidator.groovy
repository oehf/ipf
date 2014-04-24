/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.support

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.conf.ProfileException
import ca.uhn.hl7v2.conf.check.DefaultValidator
import ca.uhn.hl7v2.conf.check.ProfileNotHL7CompliantException
import ca.uhn.hl7v2.conf.spec.message.ProfileStructure
import ca.uhn.hl7v2.conf.spec.message.SegGroup
import ca.uhn.hl7v2.conf.spec.message.StaticDef
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.util.Terser

/**
 * Subclass of HAPI's conformance validator, that stops checking at the segment level, i.e. the
 * existence and cardinality of fields are not checked.
 * 
 * @author Christian Ohr
 */
public class AbstractSyntaxValidator extends DefaultValidator {

    /**
     * Omit the checks for correct version, trigger event and structure
     */
    @Override
    public HL7Exception[] validate(Message message, StaticDef profile) throws ProfileException, HL7Exception {
        List<HL7Exception> exList = new ArrayList<HL7Exception>();
        exList.addAll(doTestGroup(message, profile, profile.getIdentifier(), true));
        return exList.toArray(new HL7Exception[exList.size()]);
    }

    @Override
    public List<HL7Exception> testStructure(Structure s, ProfileStructure profile, String profileID) throws ProfileException {
        List<HL7Exception> exList = []
        if (profile instanceof SegGroup) {
            if (s instanceof Group) {
                exList.addAll(testGroup(s, profile, profileID))
            } else {
                exList.add(new ProfileNotHL7CompliantException(
                        "Mismatch between a group in the profile and the structure ${s.class.name} in the message"));
            }
        }
        // Skip testing of segments
        return exList
    }
}
