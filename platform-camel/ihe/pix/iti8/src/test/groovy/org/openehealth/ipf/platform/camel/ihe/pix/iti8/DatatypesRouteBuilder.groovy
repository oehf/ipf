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
package org.openehealth.ipf.platform.camel.ihe.pix.iti8

import static junit.framework.Assert.*
import static org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpMarshalUtils.typeSupported
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage

import org.openehealth.ipf.platform.camel.ihe.mllp.commons.MllpComponent
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder

import java.nio.ByteBuffer
import ca.uhn.hl7v2.parser.PipeParser


/**
 * Camel route to test datatypes handling.
 * @author Dmytro Rud
 */
class DatatypesRouteBuilder extends SpringRouteBuilder {

     // for datatype-related tests
     final static int CONTENT_TYPE_COUNT = 19
     static boolean[] checkedContentTypes = new boolean[CONTENT_TYPE_COUNT]
     static int currentContentType = 0
     

     /**
      * Returns true when all tests have been performed.
      */
     static boolean allContentTypesChecked() {
         for(int i = 0; i < CONTENT_TYPE_COUNT; ++i) {
             if( ! checkedContentTypes[i]) {
                 return false
             }
         }
         return true
     }
     

     /**
      * Resets the tests registration table and current number.
      */
     static void cleanCheckedContentTypes() {
         for(int i = 0; i < CONTENT_TYPE_COUNT; ++i) {
             checkedContentTypes[i] = false
         }
         currentContentType = 0
     }
     

     /**
      * Prepares response contents for the given content type.
      */
     static void prepareContents(int contentType, Exchange exchange) throws Exception {
         checkedContentTypes[contentType] = true
         
         def x = MessageUtils.ack(exchange.in.body.target)

         switch(contentType) {

         
         /* --------------- Positive responses (0-8) --------------- */
         
         // String
         case 0:
             x = new PipeParser().encode(x)
             assertTrue(x instanceof String)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break

         // Message Abrakadapter
         case 1:
             x = new MessageAdapter(x)
             assertTrue(x instanceof MessageAdapter)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // HAPI Message
         case 2:
             assertTrue(x instanceof ca.uhn.hl7v2.model.Message)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break

         // InputStream
         case 3:
             x = new PipeParser().encode(x)
             x = new ByteArrayInputStream(x.getBytes())
             assertTrue(x instanceof InputStream)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break

         // NIO ByteBuffer
         case 4:
             x = new PipeParser().encode(x).getBytes()
             x = ByteBuffer.wrap(x)
             assertTrue(x instanceof ByteBuffer)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // byte[]
         case 5:
             x = new PipeParser().encode(x).getBytes()
             assertTrue(x instanceof byte[])
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // Known data type (a String), header set to "ERROR" (and should be ignored)
         case 6:
             x = new PipeParser().encode(x)
             assertTrue(x instanceof String)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AE
             break
             
         // Unsupported data type, header set to "OK"
         case 7:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AA
             break
             
         // Null body, header set to "OK"
         case 8:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AA
             break

             
         /* --------------- Should cause exceptions in the route (9-12) --------------- */
             
         // Unsupported data type, header not set
         case 9:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = null
             break

         // Unsupported data type, header set to garbage
         case 10:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = "The world is not enough"
             break
             
         // Null body, header not set 
         case 11:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = null
             break

         // Null body, header set to garbage 
         case 12:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = "But it is such a perfect place to start"
             break

             
         /* --------------- Should generate NAKs (13-18) --------------- */
             
         // Unsupported data type, header set to "ALLES SCHLIMM"
         case 13:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AE
             break

         // Null body, header set to "FAILURE"
         case 14:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AE
             break
         
         // Exception as data, header not set
         case 15:
             x = new Exception('Vorbei sind die Ferien')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // Exception thrown, header not set
         case 16:
             x = new Exception('Die Schule beginnt')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             throw x

         // Exception as data, header set to "OK" (and should be ignored)
         case 17:
             x = new Exception('Die Zeit ist vergangen')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AA
             break
             
         // Exception thrown, header set to "OK" (and should be ignored)
         case 18:
             x = new Exception('So schnell, wie der Wind')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AckTypeCode.AA
             throw x

         }
     }
     
     
     void configure() throws Exception {

         // port 8880 -- consumer-side datatype handling
         from('xds-iti8://0.0.0.0:8880?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 prepareContents(currentContentType++, it) 
             }
         
         // port 8881 -- producer-side datatype handling
         from('xds-iti8://0.0.0.0:8881?audit=false')
             .process {
                 resultMessage(it).body = MessageUtils.ack(it.in.body.target)
             }
         
    }
}

