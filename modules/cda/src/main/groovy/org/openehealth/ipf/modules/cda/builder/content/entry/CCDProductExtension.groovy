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
package org.openehealth.ipf.modules.cda.builder.content.entry

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.BaseModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*


/**
 * 
 * 3.9.2.4: Representation of a product
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.53 Product
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProductExtension extends BaseModelExtension {
	
	CCDProductExtension() {
		super()
	}
	
	CCDProductExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
		super.register(registered)
		
		// required by Medication Activity (2.16.840.1.113883.10.20.1.24)
		POCDMT000040SubstanceAdministration.metaClass {
			
			setManufacturedProduct{ POCDMT000040ManufacturedProduct mp ->
				delegate.consumable = builder.build {
				    consumable(manufacturedProduct:mp)  
				}
			}
			
			getManufacturedProduct{ ->  
			    delegate.consumable?.manufacturedProduct  
			}
		}
		
		// required by Supply Activity (2.16.840.1.113883.10.20.1.34)
		POCDMT000040Supply.metaClass {
			setManufacturedProduct { POCDMT000040ManufacturedProduct mp ->
				delegate.product = builder.build { 
				    product(manufacturedProduct:mp) 
				}
			}
			
			getManufacturedProduct { -> 
			    delegate.product?.manufacturedProduct 
			}
		}
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Product'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.53'
	}
	
}
