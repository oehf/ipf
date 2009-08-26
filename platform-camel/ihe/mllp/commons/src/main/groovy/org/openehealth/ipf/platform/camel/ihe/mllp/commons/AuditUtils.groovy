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

import org.openehealth.ipf.modules.hl7dsl.CompositeAdapter
import org.openehealth.ipf.modules.hl7dsl.GroupAdapter
import org.openehealth.ipf.modules.hl7dsl.SelectorClosure

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.openehealth.ipf.modules.hl7dsl.CompositeAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter

import org.apache.mina.common.IoSession
import java.net.SocketAddress


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
     * Enriches the given audit dataset with IOSession-related
     * information common for all PIX/PDQ transactions.
     */
    static void enrichGenericAuditDatasetFromSession(
            MllpAuditDataset auditDataset) 
    {
        // TODO: Make this work again with real addresses. In Camel 2.0 we cannot
        // 		 access the IoSession anymore.
        auditDataset.localAddress  = 'local'
        auditDataset.remoteAddress = 'remote'
    }

    
    /**
     * Enriches the given audit dataset with HL7-related 
     * information common for all PIX/PDQ transactions.
     */
    static void enrichGenericAuditDatasetFromMessage(
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
     * Reformats machine address created by {@link SocketAddress.toString()}.
     * <p>
     * Source strings:
     * <ul>
     * <li>known hostname: <tt>hostname.zone.org/141.44.162.126[:8888]</tt></li>
     * <li>unknown hostname: <tt>/141.44.162.126[:8888]</tt></li>
     * </ul>
     *      
     * Resulting strings, respectively:
     * <ul>
     * <li>known hostname: <tt>hostname.zone.org[:8888]</tt></li>
     * <li>unknown hostname: <tt>141.44.162.126</tt> (without port number, even if it was present)</li>
     * </ul>
     */
    static String formatMachineAddress(SocketAddress address) {
        String s = address.toString()
        int pos1 = s.indexOf('/')
        int pos2 = s.indexOf(':', pos1)
        if(pos1 == 0) {
            return s[1..(Math.max(pos2, 0) - 1)] 
        } else {
            String portNumber = (pos2 < 0) ? '' : s[pos2..(s.length() - 1)]
            return s[0..(pos1 - 1)] + portNumber
        }
    }
    
     
    /**
     * Deletes trailing protocol name, URL parameters and, probably, port number.
     * <p>
     * For example, 
     * <tt>mina:tcp://localhost:8989?tralivali=figlimigli</tt> will become
     * <tt>localhost:8989</tt><br>, while
     * <tt>mina:tcp://141.44.162.126:8989?tralivali=figlimigli</tt> will become
     * <tt>141.44.162.126</tt>.
     */
    static String formatEndpointAddress(String address) {
       int pos1 = address.indexOf('//')
       int pos2 = address.indexOf('?', pos1)
       String s = address[(pos1 + 2)..((pos2 < 0) ? -1 : (pos2 - 1))]
       if('0123456789'.contains(s[0])) {
           pos2 = s.indexOf(':')
           return (pos2 < 0) ? s : s[0..(pos2 - 1)]
       }
       return s
    }
     
     
    /**
     * Returns <code>true</code> when the given {@link MessageAdapter}
     * does not contain code 'AA' in MSA-1.
     * <p>
     * Invalid messages (without MSA segment, etc.) are considered  
     * as error mesasges too.
     */
    static boolean isErrorMessage(MessageAdapter msg) {
        msg.MSA[1]?.value != 'AA'
    }
}
