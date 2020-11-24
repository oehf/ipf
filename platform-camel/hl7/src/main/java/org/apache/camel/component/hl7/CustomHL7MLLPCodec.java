/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.hl7;

import ca.uhn.hl7v2.HapiContext;

import java.lang.reflect.Field;
import java.nio.charset.CodingErrorAction;
import java.util.Locale;

/**
 * Custom HL7 MLLP codec. Ability to set the {@link ca.uhn.hl7v2.HapiContext}, e.g. to use some other
 * {@link ca.uhn.hl7v2.parser.ModelClassFactory}. Also allows to set error handling in case of
 * malformed input.
 * <p/>
 */
public class CustomHL7MLLPCodec extends HL7MLLPCodec {

    public void setHapiContext(HapiContext context) {
        config().setHapiContext(context);
    }

    public HapiContext getHapiContext() {
        return config().getHapiContext();
    }

    public void setUnmappableCharacterErrorAction(CodingErrorAction action) {
        config().setUnmappableCharacterErrorAction(action);
    }

    public void setMalformedInputErrorAction(CodingErrorAction action) {
        config().setMalformedInputErrorAction(action);
    }

    public void setUnmappableCharacterErrorAction(String action) {
        config().setUnmappableCharacterErrorAction(fromString(action));
    }

    public void setMalformedInputErrorAction(String action) {
        config().setMalformedInputErrorAction(fromString(action));
    }

    private CodingErrorAction fromString(String action) {
        switch (action.toUpperCase(Locale.ROOT)) {
            case "IGNORE":
                return CodingErrorAction.IGNORE;
            case "REPLACE":
                return CodingErrorAction.REPLACE;
            case "REPORT":
                return CodingErrorAction.REPORT;
            default:
                throw new IllegalArgumentException(action + " is not a valid CodingErrorAction (IGNORE, REPLACE, REPORT)");
        }
    }

    private HL7MLLPConfig config() {
        try {
            var field = HL7MLLPCodec.class.getDeclaredField("config");
            field.setAccessible(true);
            var value = field.get(this);
            field.setAccessible(false);
            return (HL7MLLPConfig) value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
