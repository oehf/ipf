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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.Message;
import org.apache.camel.converter.IOConverter;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;

import ca.uhn.hl7v2.parser.Parser;


/**
 * Various helper methods for data transformation.
 * 
 * @author Dmytro Rud
 */
public class MllpMarshalUtils {

    private MllpMarshalUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }
    
    
    /**
     * Converts a set of some standard PIX/PDQ-related data types to String.
     * @param message
     *      Camel message containing the data to be converted. 
     * @param charset
     *      character set. 
     * @param parser 
     *      HL7 parser. 
     * @return
     *      String representing the original exchange or <tt>null</tt>
     *      when the data type is unknown. 
     * @throws Exception
     *      nn parsing and marshaling errors.
     */
    public static String marshalStandardTypes(Message message, String charset, Parser parser) throws Exception {
        Object body = message.getBody();
        if( ! typeSupported(body)) {
            return null;
        }
        
        String s = null;
        if(body instanceof String) {
            s = (String) body;
        } else if(body instanceof MessageAdapter) {
            s = body.toString();
        } else if(body instanceof ca.uhn.hl7v2.model.Message) {
            s = parser.encode((ca.uhn.hl7v2.model.Message) body);
        } else if(body instanceof File) {
            s = IOConverter.toString((File) body).replace('\n', '\r');
        } else {
            // In standard Camel distribution this will concern  
            // byte[], InputStream and ByteBuffer.
            // See also: http://camel.apache.org/list-of-type-conversions.html
            byte[] bytes = message.getBody(byte[].class);
            if(bytes != null) {
                s = new String(bytes, charset);
            }
        }
        return s;
    }
    
    
    /**
     * Determines whether the given object belongs to the predefined
     * set of supported data types. 
     * @param body
     *      The object to check. 
     * @return
     *      <code>true</code> when the type of the object is supported 
     *      by the PIX/PDQ adapter out-of-box, <code>false</code> otherwise.
     */
    public static boolean typeSupported(Object body) {
        final Class<?>[] knownTypes = new Class<?>[] {
            String.class,
            MessageAdapter.class,
            ca.uhn.hl7v2.model.Message.class,
            File.class,
            InputStream.class,
            java.nio.ByteBuffer.class,
            byte[].class
        };
        
        for(Class<?> type : knownTypes) {
            try {
                type.cast(body);
                return true;
            } catch (ClassCastException cce) {
                // nop
            }
        }
        
        return false;
    }

    
    /**
     * Unmarshalls the message contents by converting it from 
     * {@link InputStream} to {@link MessageAdapter}.
     *   
     * @param message
     *      camel message containing the data to be unmarshalled.
     * @param charset
     *      character set name for HL7 transformation. 
     * @param parser 
     *      HL7 parser.
     * @return  
     *      String representation of message contents, as a convenience.  
     */
    public static String unmarshal(Message message, String charset, Parser parser) throws Exception {
        String s = convertBodyToString(message, charset);
        MessageAdapter msg = MessageAdapters.make(parser, s);
        message.setBody(msg);
        return s;
    }

    
    /**
     * Converts message contents to a {@link String} using the given character set
     * and replaces all <tt>'\n'</tt>'s with <tt>'\r'</tt>'s. 
     */
    public static String convertBodyToString(Message message, String charset) throws Exception {
        InputStream stream = message.getBody(InputStream.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, charset));
        String s = IOConverter.toString(br);
        s = s.replace('\n', '\r');
        return s;
    }
    
    
    /**
     * Converts the contents of the given Camel message to a {@link MessageAdapter}.  
     * @param message
     *      Camel message to be converted.
     * @param charset
     *      character set.
     * @return
     *      a {@link MessageAdapter} or <code>null</code> when it was impossible
     *      to get or create one.
     * @param parser 
     *      HL7 parser. 
     * @throws Exception
     */
    public static MessageAdapter extractMessageAdapter(
            Message message, 
            String charset, 
            Parser parser) throws Exception 
    {
        Object body = message.getBody();
        MessageAdapter msg = null;
        if(body instanceof MessageAdapter) {
            msg = (MessageAdapter) body;
        } else if(body instanceof ca.uhn.hl7v2.model.Message) {
            msg = new MessageAdapter(parser, (ca.uhn.hl7v2.model.Message) body);
        } else {
            // process all other types (String, File, InputStream, ByteBuffer, byte[])
            // by means of the standard routine.  An exception here will be o.k.
            String s = marshalStandardTypes(message, charset, parser);
            if(s != null) {
                s = s.replace('\n', '\r');
                msg = MessageAdapters.make(parser, s);
            }
        } 
        return msg;
    }

    
    /**
     * Formats and returns error message of an exception.
     * <p>
     * In particular, all line break characters must be removed, 
     * otherwise they will break the structure of an HL7 NAK.
     * @param t
     *      thrown exception.
     * @return
     *      formatted error message from the given exception.
     */
    public static String formatErrorMessage(Throwable t) { 
        String s = t.getMessage();
        if(s == null) {
            s = t.getClass().getName();
        }
        s = s.replace('\n', ';'); 
        s = s.replace('\r', ';');
        return s;
    }

    
    /**
     * Generates a NAK message on processing errors on the basis of  
     * of the thrown exception and the original HAPI request message.
     * @param t
     *      thrown exception.
     * @param original
     *      original HAPI request message.
     * @param config
     *      configuration parameters of the given transaction.                  
     */
    public static MessageAdapter createNak(
            Throwable t, 
            ca.uhn.hl7v2.model.Message original,
            MllpTransactionConfiguration config)  
    {
        AbstractHL7v2Exception hl7Exception;
        if(t instanceof AbstractHL7v2Exception) {
            hl7Exception = (AbstractHL7v2Exception) t; 
        } else if(t.getCause() instanceof AbstractHL7v2Exception) {
            hl7Exception = (AbstractHL7v2Exception) t.getCause();
        } else {
            hl7Exception = new HL7v2Exception(
                    MllpMarshalUtils.formatErrorMessage(t), 
                    config.getRequestErrorDefaultErrorCode(), 
                    t); 
        }

        ca.uhn.hl7v2.model.Message nak = (ca.uhn.hl7v2.model.Message) MessageUtils.nak(
                original, 
                hl7Exception, 
                config.getRequestErrorDefaultAckTypeCode());

        return new MessageAdapter(nak);
    }
    
}
