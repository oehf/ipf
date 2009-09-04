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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import ca.uhn.hl7v2.parser.Parser

/**
 * Groovy subroutines for HL7 message acceptance checks.
 * @author Dmytro Rud
 */
class AcceptanceCheckUtils {

     private AcceptanceCheckUtils() {
         throw new IllegalStateException('Helper class, do not instantiate');
     }

     
    /**
     * Performs transaction-specific acceptance test of the given request message.
     */
     static void checkRequestAcceptance(
             MessageAdapter msg, 
             MllpTransactionConfiguration config,
             Parser parser) throws MllpAcceptanceException 
     {
         checkMessageAcceptance(msg, config, parser, 'Request')
     }
     
     
     /**
      * Performs transaction-specific acceptance test of the given response message.
      */
     static void checkResponseAcceptance(
             MessageAdapter msg, 
             MllpTransactionConfiguration config,
             Parser parser) throws MllpAcceptanceException 
     {
         checkMessageAcceptance(msg, config, parser, 'Response')
         
         if( ! ['AA', 'AR', 'AE'].contains(msg.MSA[1]?.value)) {
             throw new MllpAcceptanceException("Bad response: missing or invalid MSA segment")
         }
     }
      
      
     /**
      * Performs acceptance test of the given message.
      * @param msg
      *          {@link MessageAdapter} representing the message.
      * @param config
      *          transaction-specific parameters.
      * @param parser
      *          HL7 parser.
      * @param direction
      *          either 'Resuest' or 'Response'.
      * @throws MllpAcceptanceException
      *          when the message cannot be accepted.
      */
     private static void checkMessageAcceptance(
             MessageAdapter msg, 
             MllpTransactionConfiguration config,
             Parser parser,
             String direction) throws MllpAcceptanceException 
     {
         def version = msg.MSH[12].value 
         if(version != config.hl7Version) {
             throw new MllpAcceptanceException("Invalid HL7 version ${version}", 203)
         }
         
         def msgType = msg.MSH[9][1].value
         if( ! config."isSupported${direction}MessageType"(msgType)) {
             throw new MllpAcceptanceException("Invalid message type ${msgType}", 200)
         }

         def triggerEvent = msg.MSH[9][2].value
         if( ! config."isSupported${direction}TriggerEvent"(msgType, triggerEvent)) {
             throw new MllpAcceptanceException("Invalid trigger event ${triggerEvent}", 201)
         }

         def structure = msg.MSH[9][3].value
         if(structure) {
             def expected = parser.getMessageStructureForEvent("${msgType}_${triggerEvent}", version)
             
             // the expected structure must be equal to the actual one,
             // but second components may be omitted in acknowledgements 
             if( ! ((structure == expected) || 
                    (structure.startsWith('ACK') && expected.startsWith('ACK'))))
             {
                 throw new MllpAcceptanceException("Invalid structure map ${structure}", 204)
             }
         }
     }

}
