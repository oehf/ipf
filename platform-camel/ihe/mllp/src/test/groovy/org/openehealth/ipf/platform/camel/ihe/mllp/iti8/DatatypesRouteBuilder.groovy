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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.parser.PipeParser
import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.hl7.HL7v2
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent

import java.nio.ByteBuffer

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage
import static org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils.typeSupported

/**
 * Camel route to test datatypes handling.
 * @author Dmytro Rud
 */
class DatatypesRouteBuilder extends SpringRouteBuilder {

     // for datatype-related tests
     final static int CONTENT_TYPE_COUNT = 18
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
         
         def x = exchange.in.body.generateACK()

         switch(contentType) {

         
         /* --------------- Positive responses (0-8) --------------- */
         
         // String
         case 0:
             x = new PipeParser().encode(x)
             assertTrue(x instanceof String)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // HAPI Message
         case 1:
             assertTrue(x instanceof Message)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break

         // InputStream
         case 2:
             x = new PipeParser().encode(x)
             x = new ByteArrayInputStream(x.getBytes())
             assertTrue(x instanceof InputStream)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break

         // NIO ByteBuffer
         case 3:
             x = new PipeParser().encode(x).getBytes()
             x = ByteBuffer.wrap(x)
             assertTrue(x instanceof ByteBuffer)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // byte[]
         case 4:
             x = new PipeParser().encode(x).getBytes()
             assertTrue(x instanceof byte[])
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // Known data type (a String), header set to "ERROR" (and should be ignored)
         case 5:
             x = new PipeParser().encode(x)
             assertTrue(x instanceof String)
             assertTrue(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AE
             break
             
         // Unsupported data type, header set to "OK"
         case 6:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AA
             break
             
         // Null body, header set to "OK"
         case 7:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AA
             break

             
         /* --------------- Should cause exceptions in the route (9-12) --------------- */
             
         // Unsupported data type, header not set
         case 8:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = null
             break

         // Unsupported data type, header set to garbage
         case 9:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = "The world is not enough"
             break
             
         // Null body, header not set 
         case 10:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = null
             break

         // Null body, header set to garbage 
         case 11:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = "But it is such a perfect place to start"
             break

             
         /* --------------- Should generate NAKs (13-18) --------------- */
             
         // Unsupported data type, header set to "ALLES SCHLIMM"
         case 12:
             x = Math.PI
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AE
             break

         // Null body, header set to "FAILURE"
         case 13:
             resultMessage(exchange).body = null
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AE
             break
         
         // Exception as data, header not set
         case 14:
             x = new Exception('Vorbei sind die Ferien')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             break
             
         // Exception thrown, header not set
         case 15:
             x = new Exception('Die Schule beginnt')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             throw x

         // Exception as data, header set to "OK" (and should be ignored)
         case 16:
             x = new Exception('Die Zeit ist vergangen')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).body = x
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AA
             break
             
         // Exception thrown, header set to "OK" (and should be ignored)
         case 17:
             x = new Exception('So schnell, wie der Wind')
             assertTrue(x instanceof Exception)
             assertFalse(typeSupported(x))
             resultMessage(exchange).headers[MllpComponent.ACK_TYPE_CODE_HEADER] = AcknowledgmentCode.AA
             throw x

         }
     }
     
     
     void configure() throws Exception {

         // port 8087 -- consumer-side datatype handling
         from('xds-iti8://0.0.0.0:18187?audit=false')
             .onException(Exception.class)
                 .maximumRedeliveries(0)
                 .end()
             .process {
                 prepareContents(currentContentType++, it) 
             }
         
         // port 8088 -- producer-side datatype handling
         from('xds-iti8://0.0.0.0:18188?audit=false')
             .transform(HL7v2.ack())
         
    }
}

