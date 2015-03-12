/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.modules.cda;


import org.openhealthtools.mdht.uml.cda.CDAPackage;
import org.openhealthtools.mdht.uml.cda.ccd.CCDPackage;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.hitsp.HITSPPackage;
import org.openhealthtools.mdht.uml.cda.ihe.IHEPackage;

public final class CDAR2Utils {

    static {
        @SuppressWarnings("unused")
        CDAPackage cdaPackageInstance = CDAPackage.eINSTANCE;
    }

    private CDAR2Utils() {
    }

    public static CCDPackage initCCD() {
        return CCDPackage.eINSTANCE;
    }

    public static HITSPPackage initHITSPC32() {
        return HITSPPackage.eINSTANCE;
    }

    public static ConsolPackage initConsolCDA() {
        return ConsolPackage.eINSTANCE;
    }

    public static IHEPackage initIHEPCC() {
        return IHEPackage.eINSTANCE;
    }

}
