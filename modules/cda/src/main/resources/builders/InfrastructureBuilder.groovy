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
package builders

import groovytools.builder.*

infrastructureRoot() {
	properties { 
	    nullFlavor()
	    typeId(schema:'typeId') 
	}
	collections {
	    templateIds(collection:'templateId') {
	        templateId(schema:'ii')
	    }
	    realmCodes(collection:'realmCode') {
	        realmCode(schema:'cs')
	    }
	}
}

typeId(schema:'ii', factory:'POCDMT000040_INFRASTRUCTURE_ROOT_TYPE_ID')
