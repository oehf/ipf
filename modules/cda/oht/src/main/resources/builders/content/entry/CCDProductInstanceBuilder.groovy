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
package builders.content.entry

import org.openhealthtools.ihe.common.cdar2.*

/**
 * Chapter 3.14.2.2: Procedure related products 
 * 
 * Template Definitions:
 *      Product Instance (2.16.840.1.113883.10.20.1.52)
 */


//CONF-449: A product instance (templateId 2.16.840.1.113883.10.20.1.52) SHALL be 
//          represented with the ParticipantRole class.
ccd_productInstance(schema:'participantRole'){
  properties{
      // CONF-450: The value for “participantRole / @classCode” in a product instance SHALL be
      //           “MANU” “Manufactured product” 2.16.840.1.113883.5.110 RoleClass STATIC.
      classCode(factory:'ROLE_CLASS_MANUFACTURED_PRODUCT', 
              def: RoleClassManufacturedProduct.MANU_LITERAL)
  }
  collections{
      templateIds(def: {
          getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.52')
          }
      })
  }
}

// end of Product Instance