/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7;

/**
 * HL7 Error codes and messages
 * 
 * @author Christian Ohr
 *
 */
public enum HL70357 {

	MESSAGE_ACCEPTED(0, "Message accepted"), 
	SEGMENT_SEQUENCE_ERROR(100, "Segment sequence error"), 
	REQUIRED_FIELD_MISSING(101, "Required field missing"), 
	DATA_TYPE_ERROR(102, "Data type error"), 
	TABLE_VALUE_NOT_FOUND(103, "Table value not found"),
	UNSUPPORTED_MESSAGE_TYPE(200, "Unsupported message type"),
	UNSUPPORTED_EVENT_CODE(201, "Unsupported event code"),
	UNSUPPORTED_PROCESSING_ID(202, "Unsupported processing id"),
	UNSUPPORTED_VERSION_ID(203, "Unsupported version id"),
	UNKNOWN_KEY_IDENTIFIER(204, "Unknown key identifier"),
	DUPLICATE_KEY_IDENTIFIER(205, "Duplicate key identifier"),
	APPLICATION_RECORD_LOCKED(206, "Application record locked"),
	APPLICATION_INTERNAL_ERROR(207, "Application internal error");

	private final int errCode;
	private final String message;

	HL70357(int errCode, String message) {
		this.errCode = errCode;
		this.message = message;
	}

	public static String messageFor(int errCode) {
		for (HL70357 err : HL70357.values()) {
			if (err.errCode == errCode) {
				return err.message;
			}
		}
		return "Unknown error";
	}
}
