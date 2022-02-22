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
package org.openehealth.ipf.commons.ihe.hpd.controls.pagination;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.*;
import org.openehealth.ipf.commons.ihe.hpd.controls.AbstractControl;
import org.openehealth.ipf.commons.ihe.hpd.controls.ControlUtils;

/**
 * See <a href="https://www.ietf.org/rfc/rfc2696.txt">RFC 2696</a>
 * "LDAP Control Extension for Simple Paged Results Manipulation".
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class Pagination extends AbstractControl {

    public static final String TYPE = "1.2.840.113556.1.4.319";

    static {
        ControlUtils.getMAP().put(TYPE, Pagination.class);
    }

    @Getter
    @Setter
    private int size;

    @Getter
    @Setter
    private byte[] cookie;

    public Pagination(int size, byte[] cookie, boolean criticality) {
        super(TYPE, criticality);
        this.size = size;
        this.cookie = (cookie == null) ? new byte[0] : cookie;
    }

    public Pagination(ASN1Sequence asn1Sequence, boolean criticality) {
        super(TYPE, criticality);
        ASN1Integer sizeObject = (ASN1Integer) asn1Sequence.getObjectAt(0);
        ASN1OctetString cookieObject = (ASN1OctetString) asn1Sequence.getObjectAt(1);
        this.size = sizeObject.getValue().intValueExact();
        this.cookie = cookieObject.getOctets();
    }

    @Override
    protected ASN1Encodable[] getASN1SequenceElements() {
        return new ASN1Encodable[]{
                new ASN1Integer(size),
                new DEROctetString(cookie)
        };
    }

    public boolean isEmptyCookie() {
        return ((cookie == null) || (cookie.length == 0));
    }

}
