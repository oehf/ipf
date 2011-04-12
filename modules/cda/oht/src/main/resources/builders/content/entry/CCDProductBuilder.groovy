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
 * Chapter 3.9.2.4: Representation of a product
 *
 * Template Definitions:
 *      Product (2.16.840.1.113883.10.20.1.53)
 *
 * Dependencies:
 *
 */

// CONF-356: A product (templateId 2.16.840.1.113883.10.20.1.53) SHALL be represented with
//           the ManufacturedProduct class.
ccd_manufacturedProduct(schema:'manufacturedProduct') {
   properties {
      // CONF-357: A ManufacturedProduct in a product template SHALL contain exactly
      //           one manufacturedProduct / manufacturedMaterial.
      // CONF-358: A manufacturedMaterial in a product template SHALL contain
      //           exactly one manufacturedMaterial / code
      // CONF-363: A manufacturedMaterial in a product template SHALL contain
      //           exactly one Material / code / originalText, which represents
      //           the generic name of the product.
      manufacturedMaterial(schema:'material', req:true,
              check: { it.code && it.code.originalText })
   }
   collections {
      templateIds(collection:'templateId', def: {
          getMetaBuilder().buildList {
              ii(root:'2.16.840.1.113883.10.20.1.53')
          }
      })
   }
}

// end of Product