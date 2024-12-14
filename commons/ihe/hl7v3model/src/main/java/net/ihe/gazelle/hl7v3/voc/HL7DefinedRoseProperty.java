/**
 * HL7DefinedRoseProperty.java
 * <p>
 * File generated from the voc::HL7DefinedRoseProperty uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration HL7DefinedRoseProperty.
 *
 */

@XmlType(name = "HL7DefinedRoseProperty")
@XmlEnum
@XmlRootElement(name = "HL7DefinedRoseProperty")
public enum HL7DefinedRoseProperty {
	@XmlEnumValue("ApplicationRoleI")
	APPLICATIONROLEI("ApplicationRoleI"),
	@XmlEnumValue("Cardinality")
	CARDINALITY("Cardinality"),
	@XmlEnumValue("DTsymbol")
	DTSYMBOL("DTsymbol"),
	@XmlEnumValue("DeleteFromMIM")
	DELETEFROMMIM("DeleteFromMIM"),
	@XmlEnumValue("DevelopingCommit")
	DEVELOPINGCOMMIT("DevelopingCommit"),
	@XmlEnumValue("EndState")
	ENDSTATE("EndState"),
	@XmlEnumValue("HMD")
	HMD("HMD"),
	@XmlEnumValue("ID")
	ID("ID"),
	@XmlEnumValue("InstancedDTsymbo")
	INSTANCEDDTSYMBO("InstancedDTsymbo"),
	@XmlEnumValue("IsPrimitiveDT")
	ISPRIMITIVEDT("IsPrimitiveDT"),
	@XmlEnumValue("IsReferenceDT")
	ISREFERENCEDT("IsReferenceDT"),
	@XmlEnumValue("IsSubjectClass")
	ISSUBJECTCLASS("IsSubjectClass"),
	@XmlEnumValue("MIM_id")
	MIMID("MIM_id"),
	@XmlEnumValue("MandatoryInclusi")
	MANDATORYINCLUSI("MandatoryInclusi"),
	@XmlEnumValue("MayRepeat")
	MAYREPEAT("MayRepeat"),
	@XmlEnumValue("ModelDate")
	MODELDATE("ModelDate"),
	@XmlEnumValue("ModelDescription")
	MODELDESCRIPTION("ModelDescription"),
	@XmlEnumValue("ModelID")
	MODELID("ModelID"),
	@XmlEnumValue("ModelName")
	MODELNAME("ModelName"),
	@XmlEnumValue("ModelVersion")
	MODELVERSION("ModelVersion"),
	@XmlEnumValue("MsgID")
	MSGID("MsgID"),
	@XmlEnumValue("Organization")
	ORGANIZATION("Organization"),
	@XmlEnumValue("RcvResp")
	RCVRESP("RcvResp"),
	@XmlEnumValue("RespComm_id")
	RESPCOMMID("RespComm_id"),
	@XmlEnumValue("StartState")
	STARTSTATE("StartState"),
	@XmlEnumValue("StateAttribute")
	STATEATTRIBUTE("StateAttribute"),
	@XmlEnumValue("StateTransition")
	STATETRANSITION("StateTransition"),
	@XmlEnumValue("V23_Datatype")
	V23DATATYPE("V23_Datatype"),
	@XmlEnumValue("V23_Fields")
	V23FIELDS("V23_Fields"),
	@XmlEnumValue("Vocab_domain")
	VOCABDOMAIN("Vocab_domain"),
	@XmlEnumValue("Vocab_strength")
	VOCABSTRENGTH("Vocab_strength"),
	@XmlEnumValue("zhxID")
	ZHXID("zhxID");
	
	private final String value;

    HL7DefinedRoseProperty(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static HL7DefinedRoseProperty fromValue(String v) {
        for (HL7DefinedRoseProperty c: HL7DefinedRoseProperty.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}