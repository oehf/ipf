/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.iti48.exceptions;

import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapFault;

import javax.xml.namespace.QName;

/**
 * A SOAP exception for an unknown value set identifier. Defined by IHE.
 *
 * @author Quentin Ligier
 */
public class UnknownValueSetException extends SoapFault {

    public UnknownValueSetException() {
        super("Unknown value set", Soap12.getInstance().getSender());
        this.setSubCode(new QName(null, "NAV"));
    }
}