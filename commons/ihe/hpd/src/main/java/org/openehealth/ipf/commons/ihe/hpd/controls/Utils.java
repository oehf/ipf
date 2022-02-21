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
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.encoders.Base64;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.Control;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.DsmlMessage;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.SearchResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods for mapping of Controls to and from DSMLv2 and Strings.
 *
 * @author Dmytro Rud
 * @since 3.7.5
 */
public class Utils {

    @Getter
    private static final Map<String, Class<? extends AbstractControl>> MAP = new HashMap<>();

    public static <T extends AbstractControl> T extractControl(String base64Value, String type, boolean criticality) throws IOException, ReflectiveOperationException {
        ASN1Sequence asn1Sequence = (ASN1Sequence) new ASN1InputStream(Base64.decode(base64Value)).readObject();
        Object result = MAP.get(type).getDeclaredConstructor(ASN1Sequence.class, boolean.class).newInstance(asn1Sequence, criticality);
        return (T) result;
    }

    public static <T extends AbstractControl> T extractControl(List<Control> controls, String type) throws IOException, ReflectiveOperationException {
        for (Control control : controls) {
            if (type.equals(control.getType())) {
                return extractControl((String) control.getControlValue(), type, control.isCriticality());
            }
        }
        return null;
    }

    public static <T extends AbstractControl> T extractControl(SearchResponse searchResponse, String type) throws IOException, ReflectiveOperationException {
        return ((searchResponse != null) && (searchResponse.getSearchResultDone() != null))
               ? extractControl(searchResponse.getSearchResultDone().getControl(), type)
               : null;
    }

    public static <T extends AbstractControl> T extractControl(DsmlMessage dsmlMessage, String type) throws IOException, ReflectiveOperationException {
        return (dsmlMessage != null)
                ? extractControl(dsmlMessage.getControl(), type)
                : null;
    }


    public static Control toDsmlv2(AbstractControl ac) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DLSequence asn1Sequence = new DLSequence(ac.getASN1SequenceElements());
        ASN1OutputStream.create(baos, ASN1Encoding.BER).writeObject(asn1Sequence);

        Control control = new Control();
        control.setType(ac.getType());
        control.setCriticality(ac.isCriticality());
        control.setControlValue(new String(Base64.encode(baos.toByteArray())));
        return control;
    }

    public static void setControl(SearchResponse response, AbstractControl ac) throws IOException {
        setControl(response.getSearchResultDone(), ac);
    }

    public static void setControl(DsmlMessage dsmlMessage, AbstractControl ac) throws IOException {
        List<Control> controls = dsmlMessage.getControl();
        for (int i = 0; i < controls.size(); ++i) {
            if (ac.getType().equals(controls.get(i).getType())) {
                controls.set(i, toDsmlv2(ac));
                return;
            }
        }
        controls.add(toDsmlv2(ac));
    }

}
