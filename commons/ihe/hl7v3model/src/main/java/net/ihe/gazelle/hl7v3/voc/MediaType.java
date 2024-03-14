/**
 * MediaType.java
 *
 * File generated from the voc::MediaType uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration MediaType.
 *
 */

@XmlType(name = "MediaType")
@XmlEnum
@XmlRootElement(name = "MediaType")
public enum MediaType {
	@XmlEnumValue("application/dicom")
	APPLICATIONDICOM("application/dicom"),
	@XmlEnumValue("application/msword")
	APPLICATIONMSWORD("application/msword"),
	@XmlEnumValue("application/pdf")
	APPLICATIONPDF("application/pdf"),
	@XmlEnumValue("audio/basic")
	AUDIOBASIC("audio/basic"),
	@XmlEnumValue("audio/k32adpcm")
	AUDIOK32ADPCM("audio/k32adpcm"),
	@XmlEnumValue("audio/mpeg")
	AUDIOMPEG("audio/mpeg"),
	@XmlEnumValue("image/g3fax")
	IMAGEG3FAX("image/g3fax"),
	@XmlEnumValue("image/gif")
	IMAGEGIF("image/gif"),
	@XmlEnumValue("image/jpeg")
	IMAGEJPEG("image/jpeg"),
	@XmlEnumValue("image/png")
	IMAGEPNG("image/png"),
	@XmlEnumValue("image/tiff")
	IMAGETIFF("image/tiff"),
	@XmlEnumValue("model/vrml")
	MODELVRML("model/vrml"),
	@XmlEnumValue("multipart/x-hl7-cda-level-one")
	MULTIPARTXHL7CDALEVELONE("multipart/x-hl7-cda-level-one"),
	@XmlEnumValue("text/html")
	TEXTHTML("text/html"),
	@XmlEnumValue("text/plain")
	TEXTPLAIN("text/plain"),
	@XmlEnumValue("text/rtf")
	TEXTRTF("text/rtf"),
	@XmlEnumValue("text/sgml")
	TEXTSGML("text/sgml"),
	@XmlEnumValue("text/x-hl7-ft")
	TEXTXHL7FT("text/x-hl7-ft"),
	@XmlEnumValue("text/xml")
	TEXTXML("text/xml"),
	@XmlEnumValue("video/mpeg")
	VIDEOMPEG("video/mpeg"),
	@XmlEnumValue("video/x-avi")
	VIDEOXAVI("video/x-avi");
	
	private final String value;

    MediaType(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static MediaType fromValue(String v) {
        for (MediaType c: MediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}