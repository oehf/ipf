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

import org.openhealthtools.ihe.common.cdar2.*
import org.junit.Test
import org.junit.Assert
import org.junit.Before

/**
 * @author Christian Ohr
 */
public class CDAR2BuilderWithCodeTest extends AbstractCDAR2BuilderTest{
	
    // Some medication data, stored in a list of maps
	private def data = [
	
	[medication:'Albuterol inhalant',
	instructions:'2 puffs QID',
	startDate:null,
	period:'6 h',
	routeCode:'IPINHL',
	dose:'2',
	administrationUnitCode:'415215001',
	medicationCode:'307782',
	id:'cdbd33f0-6cde-11db-9fe1-0800200c9a66'],
	
	[medication:'Prednisone',
	instructions:'20mg PO daily',
	startDate:'20000328',
	period:'24 h',
	routeCode:'PO',
	dose:'1',
	medicationCode:'312615',
	id:'cdbd5b03-6cde-11db-9fe1-0800200c9a66'] 
	
	]
	
	
	@Test
	public void testBuildMedicationSection() {
		POCDMT000040Section section = builder.build {
			section {
				templateId('2.16.840.1.113883.10.20.1.8')
				code('10160-0@2.16.840.1.113883.6.1')
				title('Medications')
				text {
					table(border:'1',width:'100%') {
						thead {
							tr {
								th('Medication')
								th('Instructions')
								th('Start date')
							}
						}
						tbody {
						    // Iterate over all medications. Must assign a iteration variable!
							data.each { m ->
								tr {
									td(m.medication)
									td(m.instructions)
									td(m.startDate ?: '') // Avoid 'null' output
								}
							}
						}
					}
				}
			    // Iterate over all medications. Must assign a iteration variable!
				data.each { m ->
				    // Insert diagnostic output...
					// println "Creating medication " + m.medication
					entry {
						substanceAdministration(classCode:'SBADM', moodCode:'EVN'){
							id(m.id)
							// Conditional element. Skip if not available
							if (m.startDate) {
								effectiveTime(make {
									pivlts { period(m.period) }
								})
							}
							routeCode(code:m.routeCode, codeSystem:'2.16.840.1.113883.5.112')
							doseQuantity(m.dose)
							consumable {
								manufacturedProduct {
									manufacturedLabeledDrug {
										code(code:m.medicationCode,codeSystem:'2.16.840.1.113883.6.96') { 
										    originalText(m.medication) 
										}
									}
								}
							}
						}
					}
				}
			}
		}
		Assert.assertNotNull(section)
		Assert.assertEquals 2, section.entry.size()
		Assert.assertEquals 'cdbd33f0-6cde-11db-9fe1-0800200c9a66', section.entry[0].substanceAdministration.id[0].root
		Assert.assertEquals 'cdbd5b03-6cde-11db-9fe1-0800200c9a66', section.entry[1].substanceAdministration.id[0].root
		Assert.assertEquals 0, section.entry[0].substanceAdministration.effectiveTime.size()
		Assert.assertEquals 1, section.entry[1].substanceAdministration.effectiveTime.size()
	}
	
}
