/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.controls;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.ASN1Encodable;

import java.io.IOException;

/**
 * @author Dmytro Rud
 * @since 3.7.5
 */
abstract public class AbstractControl {

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private boolean criticality;

    protected AbstractControl(String type, boolean criticality) {
        this.type = type;
        this.criticality = criticality;
    }

    abstract protected ASN1Encodable[] getASN1SequenceElements() throws IOException;

}
