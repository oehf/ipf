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
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Locale;

/**
 * Custom HL7 MLLP codec. Ability to set the {@link ca.uhn.hl7v2.HapiContext}, e.g. to use some other
 * {@link ca.uhn.hl7v2.parser.ModelClassFactory}. Also allows to set error handling in case of
 * malformed input.
 * <p/>
 */
public class CustomHL7MLLPCodec extends HL7MLLPCodec {

    private CustomHL7MLLPConfig config = new CustomHL7MLLPConfig();

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return new CustomHL7MLLPDecoder(config);
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return new HL7MLLPEncoder(config);
    }

    public void setHapiContext(HapiContext context) {
        config.setHapiContext(context);
    }

    public HapiContext getHapiContext() {
        return config.getHapiContext();
    }

    public void setCharset(Charset charset) {
        config.setCharset(charset);
    }

    public void setCharset(String charsetName) {
        config.setCharset(Charset.forName(charsetName));
    }

    public Charset getCharset() {
        return config.getCharset();
    }

    public boolean isConvertLFtoCR() {
        return config.isConvertLFtoCR();
    }

    public void setConvertLFtoCR(boolean convertLFtoCR) {
        config.setConvertLFtoCR(convertLFtoCR);
    }

    public char getStartByte() {
        return config.getStartByte();
    }

    public void setStartByte(char startByte) {
        config.setStartByte(startByte);
    }

    public char getEndByte1() {
        return config.getEndByte1();
    }

    public void setEndByte1(char endByte1) {
        config.setEndByte1(endByte1);
    }

    public char getEndByte2() {
        return config.getEndByte2();
    }

    public void setEndByte2(char endByte2) {
        config.setEndByte2(endByte2);
    }

    public boolean isValidate() {
        return config.isValidate();
    }

    public void setValidate(boolean validate) {
        config.setValidate(validate);
    }

    public boolean isProduceString() {
        return config.isProduceString();
    }

    public void setProduceString(boolean apply) {
        config.setProduceString(apply);
    }

    public void setUnmappableCharacterErrorAction(CodingErrorAction action) {
        config.setUnmappableCharacterErrorAction(action);
    }

    public void setMalformedInputErrorAction(CodingErrorAction action) {
        config.setMalformedInputErrorAction(action);
    }

    public void setUnmappableCharacterErrorAction(String action) {
        config.setUnmappableCharacterErrorAction(fromString(action));
    }

    public void setMalformedInputErrorAction(String action) {
        config.setMalformedInputErrorAction(fromString(action));
    }

    private CodingErrorAction fromString(String action) {
        switch (action.toUpperCase(Locale.ROOT)) {
            case "IGNORE" : return CodingErrorAction.IGNORE;
            case "REPLACE" : return CodingErrorAction.REPLACE;
            case "REPORT" : return CodingErrorAction.REPORT;
            default: throw new IllegalArgumentException(action + " is not a valid CodingErrorAction (IGNORE, REPLACE, REPORT)");
        }
    }
}
