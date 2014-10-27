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
package org.openehealth.ipf.commons.ihe.hl7v2

import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7dsl.GroupAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.SegmentAdapter

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.GenericSegment
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Structure

/**
 * Basic validator for HL7 v.2. 
 * 
 * @author Dmytro Rud
 * @author Mitko Kolev
 */
public abstract class AbstractMessageAdapterValidator implements Validator<Object, Object> {

    /**
     * Indicates any arbitrary value. <b>For use only in checkSegmentValues</b><br>
     * Set an element of the 4-th parameter in checkSegmentValues to <code>ANY</code>
     * to allow arbitrary value for its corresponding field
     */
    public static String ANY = null
    
    /**
    * List of relevant segments for particular message types.
    * TODO add these rules to the validation context as closures
    */
    public abstract Map<String, Map<String, String>> getRules();

   /**
    *  @return the rules for type validation
    */
    public abstract DefaultValidationContext getValidationContext();
  
    
    /**
     * Performs validation of a HL7 message.</br>
     * Override the method to employ validation based on profiles.
     * 
     * @param msg
     *     {@link MessageAdapter} with the message to be validated.
     * @param profile
     *     can be used to provide the validator with additional information, 
     *     for example if the message is request or response.
     */
    void validate(final Object msg, final Object profile){
        Collection<Exception> violations  = []
        validateMessage(msg, violations);
        if (violations.size() > 0){
            throw new ValidationException("Message validation failed", violations)
        }
    }


    /**
     * Performs validation of a HL7 message.
     * @param msg
     *     {@link MessageAdapter} with the message to be validated.
     * @param violations
     *     collection to be used to store the validation errors.
     */
    void validate(final MessageAdapter msg){
        validate(msg, null);
    }

    /**
     * Performs validation of an HL7 message.
     * @param msg
     *     {@link MessageAdapter} with the message to be validated.
     * @param violations
     *     collection to be used to store the validation errors.
     */
    void validateMessage(Object msg, Collection<Exception> violations) {
        def msh91 = msg.MSH[9][1].value
        def msh92 = msg.MSH[9][2].value ?: ''

        // find rules that correspond the type of the given message
        def segmentNames = null
        Map<String, Map<String, String>> rules = getRules()
        for(triggerEvents in rules[msh91]?.keySet()) {
            if(triggerEvents.contains(msh92) || (triggerEvents == '*')) {
                segmentNames = rules[msh91][triggerEvents]
                break
            }
        }
        if( ! segmentNames) {
            throw new ValidationException("No validation rules defined for ${msh91}^${msh92}")
        }
        checkMessage(msg, segmentNames, violations)
        validateGroup(msg, 1, violations);
    }
    
    void validateGroup(final GroupAdapter group, int groupRepetition, final Collection<Exception> violations){
        Group groupTarget = group.target;
        def names = group.names;
        for(name in names) {
              Structure[] structs = groupTarget.getAll(name);
              structs.eachWithIndex { struct, repetition ->
                  def type
                  def adapter
                  if (Segment.class.isAssignableFrom(struct.getClass())){
                      type = 'Segment'
                      adapter = new SegmentAdapter(struct)
                  } else {//it can be only Group.class or Segment.class
                      type = 'Group'
                      adapter = new GroupAdapter(struct)
                  }
                  adapter.withPath(group, repetition)
                  String methodName = "validate${type}"
                  "${methodName}"(adapter, repetition, violations)
              }
          }
     }
     
     void validateSegment(SegmentAdapter segment, int segmentRepetition, Collection<Exception> violations){
         def seg = segment.target;
         int numFields = seg.numFields();
         for (int i in 1..numFields){
             def fields = seg.getField(i);
             //for repeatable fields, the number of elements in the "fields" array are > 1.
             fields.eachWithIndex { t, repetition ->
                 if (Composite.class.isAssignableFrom (t.getClass())){
                     validateCompositeType(t, i, repetition, "${segment.path}" , violations)
                 }
             }
         }
     }
     
      void validateCompositeType(Composite t, int fieldIndnex, int typeRepetition, String path, Collection<Exception> violations){
          def msg = new MessageAdapter(t.getMessage());
          String version = msg.target.getVersion();
          String messageType = msg.MSH[9][1];
          String triggerEvent = msg.MSH[9][2];
          def rules = getValidationContext().getCompositeTypeRules(version, messageType, triggerEvent, t.getClass());
          for (rule in rules){
              violations.addAll(rule.test(t, fieldIndnex, typeRepetition, path) as List)
          }
      }

    // --------------- Highest-level validation objects ---------------

    /**
     * Validates a message.
     */
    void checkMessage(MessageAdapter msg, String segmentNames, Collection<Exception> violations) {
        checkUnrecognizedSegments(msg.group, violations)
        for (segmentName in segmentNames.tokenize()) {
            "check${segmentName}"(msg, violations)
        }
    }

    /**
     * Searches for unrecognized segments in a Group.
     */
    void checkUnrecognizedSegments(Group group, Collection<Exception> violations) {
        for(name in group.names) {
            def c = group.getClass(name)
            if(c == GenericSegment.class) {
                violations.add(new Exception("Unknown segment ${name}"))
            } else if(Group.class.isAssignableFrom(c) && group.getAll(name)) {
                checkUnrecognizedSegments(group.get(name), violations)
            }
        }
    }

    // --------------- Reusable validation functions ---------------
    
    /**
     * Checks that a segment value is one in a list
     */
    void checkFieldInAllowedDomain(msg, String segmentName, int field, Collection<String> allowedDomain, Collection<Exception> violations){
        
        String value = msg."${segmentName}"[field].encode()
        for (allowedValue in allowedDomain){
            if (allowedValue.equals(value)){
                return;
            }
        }
        violations.add(new Exception("Expected one of ${allowedDomain} in ${msg.path}.${segmentName}-${field}, found ${value}"))
    }

    /**
     * Validates structure of a message segment.
     */
    void checkSegmentStructure(msg, String segmentName, Collection<Integer> fieldNumbers, Collection<Exception> violations) {
        def segment = msg."${segmentName}"
        for(i in fieldNumbers) {
            if(segment[i].isEmpty()) {
                violations.add(new Exception("Missing ${msg.path}.${segmentName}-${i}"))
            }
        }
    }

    /**
     * Validates structure of a message segment. Elements must be equals to the given ones
     */
    void checkSegmentValues(msg, String segmentName, Collection<Integer> fieldNumbers, Collection<String> values, Collection<Exception> violations) {
        def segment = msg."${segmentName}"
        checkSegmentStructure(msg, segmentName, fieldNumbers, violations)
        
        for(i in fieldNumbers) {
            String expected = values [i - 1]?.toString();
            String actual = segment[i]?.encode()
            if(expected != ANY && !(expected == actual)) {
                violations.add(new Exception("Expected ${expected} of ${msg.path}.${segmentName}-${i}, found ${actual}"))
            }
        }
    }

    // --------------- Segments, ordered alphabetically ---------------

	/**
	 * Validates segment ERR.
	 */
	void checkERR(MessageAdapter msg, Collection<Exception> violations){
		checkSegmentStructure(msg, 'ERR', [3, 4], violations)
	}
   
    /**
     * Validates segment MSA.
     */
    void checkMSA(MessageAdapter msg, Collection<Exception> violations) {
        checkSegmentStructure(msg, 'MSA', [1, 2], violations)
    }

    /**
     * Validates segment MSH.
     */
    void checkMSH(MessageAdapter msg, Collection<Exception> violations) {
        checkSegmentStructure(msg, 'MSH', [1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12], violations)
    }

    /**
     * Validates segment PID.
     */
    void checkPID(msg, Collection<Exception> violations) {
        checkPatientName(msg.PID[5], violations)
        checkPatientIdList(msg.PID[3], violations)
    }

    // --------------- Fine grained validation of particular fields ---------------

    /**
     * Validates patient name (datatype XPN).
     */
    void checkPatientName(xpn, Collection<Exception> violations) {
        if( ! (xpn[1].value || xpn[2].value)) {
            violations.add(new Exception('Missing patient name'))
        }
    }

    /**
     * Validates a single patient ID (datatype CX). Already covers CP-538.
     */
    void checkPatientId(cx, Collection<Exception> violations) {
        if( ! (cx[1].value && (cx[4][1].value || (cx[4][2].value && (cx[4][3].value == 'ISO'))))) {
            violations.add(new Exception('Incomplete patient ID'))
        }
    }

    /**
     * Validates patient ID list (datatype repeatable CX).
     */
    void checkPatientIdList(repeatableCX, Collection<Exception> violations) {
        repeatableCX().each { cx ->
            checkPatientId(cx, violations)
        }
    }

    /**
     * Validates short patient ID (i.e. without assigning authority, as in PIX Feed).
     */
    void checkShortPatientId(pid3, Collection<Exception> violations) {
        if(( ! pid3?.value) || ( ! pid3[1]?.value)) {
            violations.add(new Exception('Missing patient ID'))
        }
    }
}
