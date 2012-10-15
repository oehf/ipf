/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.config;

import java.util.Map;

/**
 * 
 * This class should be used to define the custom HL7 model classes
 * in the spring context definition.
 * 
 * <pre class="code">
 *
 *  &lt;bean id="customClasses" 
 *    class="org.openehealth.ipf.modules.hl7.config.CustomModelClasses"&gt;
 *    &lt;property name="modelClasses"&gt;
 *      &lt;map&gt;
 *        &lt;entry key="2.5"&gt;
 *          &lt;list&gt;
 *            &lt;value&gt;org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25&lt;/value&gt;
 *          &lt;/list&gt;  
 *        &lt;/entry&gt;
 *      &lt;/map&gt;
 *    &lt;/property&gt;
 *  &lt;/bean&gt;</pre>
 * 
 * @author Boris Stanojevic
 */
public class CustomModelClasses {

    private Map<String, String[]> modelClasses;

    public Map<String, String[]> getModelClasses() {
        return modelClasses;
    }

    public void setModelClasses(Map<String, String[]> modelClasses) {
        this.modelClasses = modelClasses;
    }
}
