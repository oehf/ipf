/**
 * VaccineType.java
 * <p>
 * File generated from the voc::VaccineType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration VaccineType.
 *
 */

@XmlType(name = "VaccineType")
@XmlEnum
@XmlRootElement(name = "VaccineType")
public enum VaccineType {
	@XmlEnumValue("1")
	_1("1"),
	@XmlEnumValue("10")
	_10("10"),
	@XmlEnumValue("100")
	_100("100"),
	@XmlEnumValue("101")
	_101("101"),
	@XmlEnumValue("11")
	_11("11"),
	@XmlEnumValue("12")
	_12("12"),
	@XmlEnumValue("13")
	_13("13"),
	@XmlEnumValue("14")
	_14("14"),
	@XmlEnumValue("15")
	_15("15"),
	@XmlEnumValue("16")
	_16("16"),
	@XmlEnumValue("17")
	_17("17"),
	@XmlEnumValue("18")
	_18("18"),
	@XmlEnumValue("19")
	_19("19"),
	@XmlEnumValue("2")
	_2("2"),
	@XmlEnumValue("20")
	_20("20"),
	@XmlEnumValue("21")
	_21("21"),
	@XmlEnumValue("22")
	_22("22"),
	@XmlEnumValue("23")
	_23("23"),
	@XmlEnumValue("24")
	_24("24"),
	@XmlEnumValue("25")
	_25("25"),
	@XmlEnumValue("26")
	_26("26"),
	@XmlEnumValue("27")
	_27("27"),
	@XmlEnumValue("28")
	_28("28"),
	@XmlEnumValue("29")
	_29("29"),
	@XmlEnumValue("3")
	_3("3"),
	@XmlEnumValue("30")
	_30("30"),
	@XmlEnumValue("31")
	_31("31"),
	@XmlEnumValue("32")
	_32("32"),
	@XmlEnumValue("33")
	_33("33"),
	@XmlEnumValue("34")
	_34("34"),
	@XmlEnumValue("35")
	_35("35"),
	@XmlEnumValue("36")
	_36("36"),
	@XmlEnumValue("37")
	_37("37"),
	@XmlEnumValue("38")
	_38("38"),
	@XmlEnumValue("39")
	_39("39"),
	@XmlEnumValue("4")
	_4("4"),
	@XmlEnumValue("40")
	_40("40"),
	@XmlEnumValue("41")
	_41("41"),
	@XmlEnumValue("42")
	_42("42"),
	@XmlEnumValue("43")
	_43("43"),
	@XmlEnumValue("44")
	_44("44"),
	@XmlEnumValue("45")
	_45("45"),
	@XmlEnumValue("46")
	_46("46"),
	@XmlEnumValue("47")
	_47("47"),
	@XmlEnumValue("48")
	_48("48"),
	@XmlEnumValue("49")
	_49("49"),
	@XmlEnumValue("5")
	_5("5"),
	@XmlEnumValue("50")
	_50("50"),
	@XmlEnumValue("51")
	_51("51"),
	@XmlEnumValue("52")
	_52("52"),
	@XmlEnumValue("53")
	_53("53"),
	@XmlEnumValue("54")
	_54("54"),
	@XmlEnumValue("55")
	_55("55"),
	@XmlEnumValue("56")
	_56("56"),
	@XmlEnumValue("57")
	_57("57"),
	@XmlEnumValue("58")
	_58("58"),
	@XmlEnumValue("59")
	_59("59"),
	@XmlEnumValue("6")
	_6("6"),
	@XmlEnumValue("60")
	_60("60"),
	@XmlEnumValue("61")
	_61("61"),
	@XmlEnumValue("62")
	_62("62"),
	@XmlEnumValue("63")
	_63("63"),
	@XmlEnumValue("64")
	_64("64"),
	@XmlEnumValue("65")
	_65("65"),
	@XmlEnumValue("66")
	_66("66"),
	@XmlEnumValue("67")
	_67("67"),
	@XmlEnumValue("68")
	_68("68"),
	@XmlEnumValue("69")
	_69("69"),
	@XmlEnumValue("7")
	_7("7"),
	@XmlEnumValue("70")
	_70("70"),
	@XmlEnumValue("71")
	_71("71"),
	@XmlEnumValue("72")
	_72("72"),
	@XmlEnumValue("73")
	_73("73"),
	@XmlEnumValue("74")
	_74("74"),
	@XmlEnumValue("75")
	_75("75"),
	@XmlEnumValue("76")
	_76("76"),
	@XmlEnumValue("77")
	_77("77"),
	@XmlEnumValue("78")
	_78("78"),
	@XmlEnumValue("79")
	_79("79"),
	@XmlEnumValue("8")
	_8("8"),
	@XmlEnumValue("80")
	_80("80"),
	@XmlEnumValue("81")
	_81("81"),
	@XmlEnumValue("82")
	_82("82"),
	@XmlEnumValue("83")
	_83("83"),
	@XmlEnumValue("84")
	_84("84"),
	@XmlEnumValue("85")
	_85("85"),
	@XmlEnumValue("86")
	_86("86"),
	@XmlEnumValue("87")
	_87("87"),
	@XmlEnumValue("88")
	_88("88"),
	@XmlEnumValue("89")
	_89("89"),
	@XmlEnumValue("9")
	_9("9"),
	@XmlEnumValue("90")
	_90("90"),
	@XmlEnumValue("91")
	_91("91"),
	@XmlEnumValue("92")
	_92("92"),
	@XmlEnumValue("93")
	_93("93"),
	@XmlEnumValue("94")
	_94("94"),
	@XmlEnumValue("95")
	_95("95"),
	@XmlEnumValue("96")
	_96("96"),
	@XmlEnumValue("97")
	_97("97"),
	@XmlEnumValue("98")
	_98("98");
	
	private final String value;

    VaccineType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static VaccineType fromValue(String v) {
        for (VaccineType c: VaccineType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}