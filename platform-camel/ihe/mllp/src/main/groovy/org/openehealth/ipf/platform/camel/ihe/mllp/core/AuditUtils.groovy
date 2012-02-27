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
package org.openehealth.ipf.platform.camel.ihe.mllp.core

import org.apache.camel.Exchange
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.SelectorClosure
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor

/**
 * Various ATNA-auditing related utilities.
 * <p>
 * All generic Groovy stuff is collected here.
 *   
 * @author Dmytro Rud
 */
class AuditUtils {
    private static final transient Log LOG = LogFactory.getLog(AuditUtils.class);
    
    private AuditUtils() {
        throw new IllegalStateException('Helper class, do not instantiate');
    }

    
    /**
     * Enriches the given audit dataset with HL7-related 
     * information common for all PIX/PDQ transactions
     * contained in the request message.
     */
    static void enrichGenericAuditDatasetFromRequest(
            MllpAuditDataset auditDataset, 
            MessageAdapter msg)
    {
        auditDataset.sendingApplication   = msg.MSH[3].value ?: ''
        auditDataset.sendingFacility      = msg.MSH[4].value ?: ''
        auditDataset.receivingApplication = msg.MSH[5].value ?: ''
        auditDataset.receivingFacility    = msg.MSH[6].value ?: ''
        auditDataset.messageType          = msg.MSH[9][2].value ?: ''
        auditDataset.messageControlId     = msg.MSH[10].value ?: ''
    }
    
    
    /**
     * Checks whether auditing is possible and whether it should 
     * be performed.  If both anwers are positive, performs it.
     * <p>
     * Unified for server and client sides.
     * 
     * @param auditDataset
     *      The pre-filled audit dataset instance. 
     * @param allowIncompleteAudit
     *      Whether incomplete ATNA audit records are allowed as well.
     * @param auditStrategy
     *      The actual auditing functionality holder.  
     * @param fault
     *      {@code true}, when the event outcome should be 
     *      <tt>[MAJOR_]FAILURE<tt> instead of <tt>SUCCESS</tt>.
     */
    static void finalizeAudit(
            MllpAuditDataset auditDataset,
            boolean allowIncompleteAudit,
            MllpAuditStrategy auditStrategy, 
            boolean fault)
    {
        if(auditDataset == null) {
            LOG.warn('Audit dataset is not initialized');
            return;
        }
        
        try {
            String[] fields = concatenate(
                MllpAuditDataset.GENERIC_NECESSARY_AUDIT_FIELDS,
                auditStrategy.getNecessaryFields(auditDataset.getMessageType()));

            if(auditDataset.isAuditingPossible(fields, true, allowIncompleteAudit)) {            
                RFC3881EventOutcomeCodes eventOutcome = fault ? 
                        RFC3881EventOutcomeCodes.MAJOR_FAILURE : 
                        RFC3881EventOutcomeCodes.SUCCESS;
                
                auditStrategy.doAudit(eventOutcome, auditDataset);
            }

        } catch (Exception e) {
            LOG.error('ATNA auditing failed', e);
        }
    }
    
    
    /**
     * Concatenates two {@link String} arrays.
     */
    static String[] concatenate(String[] src1, String[] src2) {
        String[] result = new String[src1.length + src2.length];
        System.arraycopy(src1, 0, result, 0, src1.length);
        System.arraycopy(src2, 0, result, src1.length, src2.length);
        result
    }


    /**
     * Returns <code>true</code> when the given {@link MessageAdapter}
     * does contain code 'AA' or 'CA' in MSA-1.
     * <p>
     * <code>null</code> values, damaged messages, etc. will lead
     * to <code>false</code> return values as well.
     */
    static boolean isPositiveAck(MessageAdapter msg) {
        try {
            return (msg.MSA[1].value in ['AA', 'CA'])
        } catch (Exception e) {
            return false
        }
    }
     

    /**
     * Returns a list of patient IDs from the given repeatable field 
     * or <code>null</code>, when there are no patient IDs.
     */
    static List pidList(SelectorClosure repeatable) {
        repeatable().collect { cx -> MessageUtils.pipeEncode(cx.target) } ?: null
    }
     
    
    /**
      * Returns string representation of the request message by extracting it
      * from the corresponding header of the given Camel exchange (preferred) 
      * or by serializing the given message adapter. 
      */
    static String getRequestString(Exchange exchange, MessageAdapter msg) {
        exchange.in.headers[Hl7v2Interceptor.ORIGINAL_MESSAGE_STRING_HEADER_NAME] ?: msg.toString()
    }
}
