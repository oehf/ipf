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

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Christian Ohr
 */

abstract class BaseModelExtension {

   protected static final Logger LOG = LoggerFactory.getLogger(BaseModelExtension.class)

   def builder

   def extensions = {
      register([])
   }

   BaseModelExtension() {
   }

   BaseModelExtension(builder) {
      this.builder = builder
   }

   abstract String templateId()

   abstract String extensionName()

   def register(Collection registered) {
      LOG.debug("Initializing extension {} ({})", extensionName(), templateId())
      registered.add(templateId())
   }


}
