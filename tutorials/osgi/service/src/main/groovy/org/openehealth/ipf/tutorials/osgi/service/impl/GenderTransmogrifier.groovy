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
package org.openehealth.ipf.tutorials.osgi.service.impl

import org.openehealth.ipf.commons.core.modules.api.Transmogrifier
/**
 * @author Martin Krasser
 */
public class GenderTransmogrifier  implements Transmogrifier {

    Object zap(Object msg, Object[] params) {
        msg.PID[8] = msg.PID[8].mapGender()
        msg
    }
    
}
