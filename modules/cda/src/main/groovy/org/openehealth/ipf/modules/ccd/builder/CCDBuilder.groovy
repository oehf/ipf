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
package org.openehealth.ipf.modules.ccd.builder

import java.lang.ClassLoader
import org.openehealth.ipf.modules.cda.builder.CDAR2Builder

import org.openhealthtools.ihe.common.cdar2.*


/**
 * CCDBuilder extends CDABuilder by implementing contraints as described in
 * the CCD specification. It furthermore introduces an additional level of
 * builder 'abstractness', primarily in the body sections. This avoids
 * building 'boilerplate' structures.
 *
 * Example: the CCD spec defines the "purpose" of a CCD document being written
 * as a structured body section with specific structure, codes, templateIds etc.
 * By defining a "purpose" and "purposeActivity" extension, the following CCD
 * fragment
 *
 * <pre>
 *
 * <component>
 *  <section>
 *    <templateId root="2.16.840.1.113883.10.20.1.13"/>
 *    <code code="48764-5" codeSystem="2.16.840.1.113883.6.1"
 *          codeSystemName="LOINC" displayName="Summary purpose"/>
 *    <title>Summary purpose</title>
 *    <text>Transfer of Care</text>
 *    <entry typeCode="DRIV">
 *      <act classCode="ACT" moodCode="EVN">
 *        <templateId root="2.16.840.1.113883.10.20.1.30"/>
 *        <code code="23745001" codeSystem="2.16.840.1.113883.6.96"
 *              codeSystemName="SNOMED CT" displayName="Documentation procedure"/>
 *        <statusCode code="completed"/>
 *        <entryRelationship typeCode="RSON">
 *          <act classCode="ACT" moodCode="EVN">
 *            <code code="308292007" codeSystem="2.16.840.1.113883.6.96" displayName="Transfer of care"/>
 *            <statusCode code="completed"/>
 *          </act>
 *        </entryRelationship>
 *      </act>
 *    </entry>
 *  </section>
 * </component>
 *
 * </pre>
 *
 * drastically boils down by exploiting all static constraints to:
 *
 * <pre>
 *
 *  purpose {
 *    text('Transfer of Care')
 *    purposeActivity() {
 *       act {
 *          code(code:'308292007', codeSystem:'2.16.840.1.113883.6.96', displayName:'Transfer of care')
 *          statusCode('completed')
 *       }
 *    }
 *  }
 *
 * </pre>
 * 
 * @author Christian Ohr
 */
public class CCDBuilder extends CDAR2Builder{
	
	public CCDBuilder(ClassLoader loader) {
	   super(loader)
      new CCDModelExtension(builder:this).extensions.call()
	}


	protected void actBuilder() {
        super.actBuilder()
		define(getClass().getResource('/ccdbuilders/ContinuityOfCareDocumentBuilder.groovy'))
		define(getClass().getResource('/ccdbuilders/PurposeBuilder.groovy'))
		define(getClass().getResource('/ccdbuilders/PayersBuilder.groovy'))
		define(getClass().getResource('/ccdbuilders/AdvanceDirectivesBuilder.groovy'))
		define(getClass().getResource('/ccdbuilders/FamilyHistoryBuilder.groovy'))
		define(getClass().getResource('/ccdbuilders/SupportBuilder.groovy'))
	}
}
