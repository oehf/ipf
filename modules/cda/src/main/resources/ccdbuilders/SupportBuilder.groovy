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
package ccdbuilders

import org.openhealthtools.ihe.common.cdar2.*

// Chapter 3.3: Support

// CONF-108: CCD MAY contain one or more patient guardians.
// CONF-109: A patient guardian SHALL be represented with 
//           ClinicalDocument / recordTarget / patientRole / patient / guardian.


ccd_support(schema:'guardian'){
     properties{
         
     }
     collections{
         
     }
 }