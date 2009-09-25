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
 * Chapter 3.9.2.1.2: Supply activity
 * 
 * Template Definitions:
 *      Supply Activity (2.16.840.1.113883.10.20.1.34)
 *      Fulfillment Instruction (2.16.840.1.113883.10.20.1.43)
 *
 * Dependencies:
 *      Medication Status Observation (ccd_medicationStatus)
 *      Product (ccd_manufacturedProduct)
 *      Product Instance (ccd_manufacturedProduct)
 *      Comments (ccd_section)
 *      Product (ccd_product)
 */


// CONF-316: A supply activity (templateId 2.16.840.1.113883.10.20.1.34) 
//           SHALL be represented with Supply.
ccd_supplyActivity(schema:'ccd_supply'){
    properties {
        statusCode(schema:'cs', def: {
            getMetaBuilder().build {
                cs('active')
              }
        })
        // CONF-325: A supply activity MAY contain exactly one 
        //           Supply / participant / @typeCode = “LOC”, 
        //           to indicate the supply location.
        supplyLocation(schema:'participantRole')
        // CONF-334: A supply activity MAY contain one or more fulfillment instructions. 
        fulfillmentInstruction(schema:'ccd_fulfillmentInstruction')
        // CONF-351:	A supply activity MAY contain exactly one medication
        //           status observation.
        medicationStatus(schema:'ccd_medicationStatus')
        productInstance(schema:'ccd_productInstance')       
        product(schema:'ccd_product')       
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.34')
            }
        })
        // CONF-318: A supply activity SHALL contain at least one Supply / id 
        ids(collection:'id', min:1) {
            id(schema:'ii')
        }
    }
}

// CONF-335: A fulfillment instruction (templateId 2.16.840.1.113883.10.20.1.43) 
//           SHALL be represented with Act.
ccd_fulfillmentInstruction(schema:'act'){
    properties{
        // CONF-336: The value for “Act / @moodCode” in a fulfillment instruction 
        //           SHALL be “INT” “Intent” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(def:XDocumentActMood.INT_LITERAL)
    }
    collections {
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.43')
            }
        })        
    }
}

//CONF-355: A supply activity MAY contain exactly one Supply / product, the target 
//          of which is a product template.
ccd_product(schema:'product') {
    properties {
        manufacturedProduct(schema:'ccd_manufacturedProduct', req:true)
    }
}

