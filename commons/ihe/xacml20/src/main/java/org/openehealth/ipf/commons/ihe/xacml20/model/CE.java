/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.model;

/**
 * @author Dmytro Rud
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE} instead.
 */
@Deprecated(since = "5.1")
public class CE extends org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CE {

    public CE() {
        super();
    }

    public CE(String code, String codeSystem, String codeSystemName, String displayName) {
        super(code, codeSystem, codeSystemName, displayName);
    }

}
