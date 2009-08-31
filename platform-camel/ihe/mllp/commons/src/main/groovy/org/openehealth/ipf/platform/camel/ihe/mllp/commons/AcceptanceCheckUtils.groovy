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
 * 
 * @author Dmytro Rud
 */
class AcceptanceCheckUtils {

    /**
     * Performs transaction-specific acceptance test of the given request message.
     * @param msg
     *          {@link MessageAdapter} representing the message.
     * @param config
     *          transaction-specific parameters.
     * @throws MllpAcceptanceException
     *          when the message cannot be accepted.
     */
     static void checkRequestAcceptance(
             MessageAdapter msg, 
             MllpTransactionConfiguration config,
             Parser parser) throws MllpAcceptanceException 
     {
         def version = msg.MSH[12].value 
         if(version != config.hl7Version) {
             throw new MllpAcceptanceException("Invalid HL7 version ${version}", 203)
         }
         
         def msgType = msg.MSH[9][1].value
         if(msgType != config.allowedMessageType) {
             throw new MllpAcceptanceException("Invalid message type ${msgType}", 200)
         }

         def triggerEvent = msg.MSH[9][2].value
         def found = false
         for(int i = 0; i < config.allowedTriggerEvents.length; ++i) {
             if(config.allowedTriggerEvents[i] == triggerEvent) {
                 def structure = msg.MSH[9][3].value
                 if(structure) {
                     def expected = parser.getMessageStructureForEvent("${msgType}_${triggerEvent}", version)
                     if(structure != expected) {
                         throw new MllpAcceptanceException("Invalid structure map ${structure}", 204)
                     }
                 }
                 found = true
                 break
             }
         }
         if( ! found) {
             throw new MllpAcceptanceException("Invalid trigger event ${triggerEvent}", 201)
         }
     }
     
     
     /**
      * Performs transaction-agnostic acceptance test of the given response message.
      * @param msg
      *          {@link MessageAdapter} representing the message.
      * @throws MllpAcceptanceException
      *          when the message cannot be accepted.
      */
     static void checkResponseAcceptance(MessageAdapter msg) {
         if( ! ['AA', 'AR', 'AE'].contains(msg.MSA[1]?.value)) {
             throw new MllpAcceptanceException("Bad response: missing or invalid MSA segment")
         }
     }
}
