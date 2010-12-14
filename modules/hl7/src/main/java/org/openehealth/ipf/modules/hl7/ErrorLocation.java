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
 * Java Bean for HL7 locations
 * 
 * @author Christian Ohr
 * 
 */
public class ErrorLocation {

	private String segmentName = null;
	private int segmentRepetition = -1;
	private int fieldPosition = -1;

	private int fieldRepetition = -1;
	private int componentNumber = -1;
	private int subcomponentNumber = -1;

    public static int segmentRepetitionIndexingBase = 0;
    public static int fieldRepetitionIndexingBase = 0;

	public ErrorLocation() {
	}

	public ErrorLocation(String segmentName, int segmentRepetition,
			int fieldPosition, int fieldRepetition, int componentNumber,
			int subcomponentNumber) {
		super();
		this.segmentName = segmentName;
		this.segmentRepetition = segmentRepetition + segmentRepetitionIndexingBase;
		this.fieldPosition = fieldPosition;
		this.fieldRepetition = fieldRepetition + fieldRepetitionIndexingBase;
		this.componentNumber = componentNumber;
		this.subcomponentNumber = subcomponentNumber;
	}

	public String getSegmentName() {
		return segmentName;
	}

	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}

	public int getSegmentRepetition() {
		return segmentRepetition;
	}

	public void setSegmentRepetition(int segmentRepetition) {
		this.segmentRepetition = segmentRepetition;
	}

	public int getFieldPosition() {
		return fieldPosition;
	}

	public void setFieldPosition(int fieldPosition) {
		this.fieldPosition = fieldPosition;
	}

	public int getFieldRepetition() {
		return fieldRepetition;
	}

	public void setFieldRepetition(int fieldRepetition) {
		this.fieldRepetition = fieldRepetition;
	}

	public int getComponentNumber() {
		return componentNumber;
	}

	public void setComponentNumber(int componentNumber) {
		this.componentNumber = componentNumber;
	}

	public int getSubcomponentNumber() {
		return subcomponentNumber;
	}

	public void setSubcomponentNumber(int subcomponentNumber) {
		this.subcomponentNumber = subcomponentNumber;
	}

    public int getSegmentRepetitionIndexingBase() {
        return segmentRepetitionIndexingBase;
    }

    public void setSegmentRepetitionIndexingBase(int segmentRepetitionIndexingBase) {
        ErrorLocation.segmentRepetitionIndexingBase = segmentRepetitionIndexingBase;
    }

    public int getFieldRepetitionIndexingBase() {
        return fieldRepetitionIndexingBase;
    }

    public void setFieldRepetitionIndexingBase(int fieldRepetitionIndexingBase) {
        ErrorLocation.fieldRepetitionIndexingBase = fieldRepetitionIndexingBase;
    }
}
