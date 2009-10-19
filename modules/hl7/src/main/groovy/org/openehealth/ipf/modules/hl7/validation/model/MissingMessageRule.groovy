/**
 * 
 */
package org.openehealth.ipf.modules.hl7.validation.model

import ca.uhn.hl7v2.validation.MessageRuleimport ca.uhn.hl7v2.validation.ValidationExceptionimport ca.uhn.hl7v2.validation.ValidationExceptionimport ca.uhn.hl7v2.model.Message

/**
 * A private rule that may be used if no other message rule could be found
 * 
 * @author i000161
 *
 */
class MissingMessageRule implements MessageRule {

	String description
	String sectionReference
	
	MissingMessageRule() {
        super()
    }
	
	MissingMessageRule(String description, String sectionReference) {
        super()
		this.description = description
		this.sectionReference = sectionReference
    }
    
    /**
     * @see ca.uhn.hl7v2.validation.MessageRule#test(ca.uhn.hl7v2.model.Message)
     */
    public ValidationException[] test(Message msg){
         [ new ValidationException("Message rule required for ${msg.eventType}^${msg.triggerEvent} (${msg.version})" +
		   'but none could be found. Probably the message type is not allowed in this context') ] as ValidationException[]
    }
	
}
