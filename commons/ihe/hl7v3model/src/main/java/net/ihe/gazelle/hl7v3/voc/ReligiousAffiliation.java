/**
 * ReligiousAffiliation.java
 * <p>
 * File generated from the voc::ReligiousAffiliation uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration ReligiousAffiliation.
 *
 */

@XmlType(name = "ReligiousAffiliation")
@XmlEnum
@XmlRootElement(name = "ReligiousAffiliation")
public enum ReligiousAffiliation {
	@XmlEnumValue("1001")
	_1001("1001"),
	@XmlEnumValue("1002")
	_1002("1002"),
	@XmlEnumValue("1003")
	_1003("1003"),
	@XmlEnumValue("1004")
	_1004("1004"),
	@XmlEnumValue("1005")
	_1005("1005"),
	@XmlEnumValue("1006")
	_1006("1006"),
	@XmlEnumValue("1007")
	_1007("1007"),
	@XmlEnumValue("1008")
	_1008("1008"),
	@XmlEnumValue("1009")
	_1009("1009"),
	@XmlEnumValue("1010")
	_1010("1010"),
	@XmlEnumValue("1011")
	_1011("1011"),
	@XmlEnumValue("1012")
	_1012("1012"),
	@XmlEnumValue("1013")
	_1013("1013"),
	@XmlEnumValue("1014")
	_1014("1014"),
	@XmlEnumValue("1015")
	_1015("1015"),
	@XmlEnumValue("1016")
	_1016("1016"),
	@XmlEnumValue("1017")
	_1017("1017"),
	@XmlEnumValue("1018")
	_1018("1018"),
	@XmlEnumValue("1019")
	_1019("1019"),
	@XmlEnumValue("1020")
	_1020("1020"),
	@XmlEnumValue("1021")
	_1021("1021"),
	@XmlEnumValue("1022")
	_1022("1022"),
	@XmlEnumValue("1023")
	_1023("1023"),
	@XmlEnumValue("1024")
	_1024("1024"),
	@XmlEnumValue("1025")
	_1025("1025"),
	@XmlEnumValue("1026")
	_1026("1026"),
	@XmlEnumValue("1027")
	_1027("1027"),
	@XmlEnumValue("1028")
	_1028("1028"),
	@XmlEnumValue("1029")
	_1029("1029"),
	@XmlEnumValue("1030")
	_1030("1030"),
	@XmlEnumValue("1031")
	_1031("1031"),
	@XmlEnumValue("1032")
	_1032("1032"),
	@XmlEnumValue("1033")
	_1033("1033"),
	@XmlEnumValue("1034")
	_1034("1034"),
	@XmlEnumValue("1035")
	_1035("1035"),
	@XmlEnumValue("1036")
	_1036("1036"),
	@XmlEnumValue("1037")
	_1037("1037"),
	@XmlEnumValue("1038")
	_1038("1038"),
	@XmlEnumValue("1039")
	_1039("1039"),
	@XmlEnumValue("1040")
	_1040("1040"),
	@XmlEnumValue("1041")
	_1041("1041"),
	@XmlEnumValue("1042")
	_1042("1042"),
	@XmlEnumValue("1043")
	_1043("1043"),
	@XmlEnumValue("1044")
	_1044("1044"),
	@XmlEnumValue("1045")
	_1045("1045"),
	@XmlEnumValue("1046")
	_1046("1046"),
	@XmlEnumValue("1047")
	_1047("1047"),
	@XmlEnumValue("1048")
	_1048("1048"),
	@XmlEnumValue("1049")
	_1049("1049"),
	@XmlEnumValue("1050")
	_1050("1050"),
	@XmlEnumValue("1051")
	_1051("1051"),
	@XmlEnumValue("1052")
	_1052("1052"),
	@XmlEnumValue("1053")
	_1053("1053"),
	@XmlEnumValue("1054")
	_1054("1054"),
	@XmlEnumValue("1055")
	_1055("1055"),
	@XmlEnumValue("1056")
	_1056("1056"),
	@XmlEnumValue("1057")
	_1057("1057"),
	@XmlEnumValue("1058")
	_1058("1058"),
	@XmlEnumValue("1059")
	_1059("1059"),
	@XmlEnumValue("1060")
	_1060("1060"),
	@XmlEnumValue("1061")
	_1061("1061"),
	@XmlEnumValue("1062")
	_1062("1062"),
	@XmlEnumValue("1063")
	_1063("1063"),
	@XmlEnumValue("1064")
	_1064("1064"),
	@XmlEnumValue("1065")
	_1065("1065"),
	@XmlEnumValue("1066")
	_1066("1066"),
	@XmlEnumValue("1067")
	_1067("1067"),
	@XmlEnumValue("1068")
	_1068("1068"),
	@XmlEnumValue("1069")
	_1069("1069"),
	@XmlEnumValue("1070")
	_1070("1070"),
	@XmlEnumValue("1071")
	_1071("1071"),
	@XmlEnumValue("1072")
	_1072("1072"),
	@XmlEnumValue("1073")
	_1073("1073"),
	@XmlEnumValue("1074")
	_1074("1074"),
	@XmlEnumValue("1075")
	_1075("1075"),
	@XmlEnumValue("1076")
	_1076("1076"),
	@XmlEnumValue("1077")
	_1077("1077"),
	@XmlEnumValue("1078")
	_1078("1078"),
	@XmlEnumValue("1079")
	_1079("1079"),
	@XmlEnumValue("1080")
	_1080("1080"),
	@XmlEnumValue("1081")
	_1081("1081"),
	@XmlEnumValue("1082")
	_1082("1082");
	
	private final String value;

    ReligiousAffiliation(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static ReligiousAffiliation fromValue(String v) {
        for (ReligiousAffiliation c: ReligiousAffiliation.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}