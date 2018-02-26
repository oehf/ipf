/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit.codes;


import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EnumeratedValueSet;
import org.openehealth.ipf.commons.audit.types.MediaType;

/**
 * Media Type Code as specified in
 * http://dicom.nema.org/medical/dicom/current/output/html/part16.html#sect_CID_405
 * 1.2.840.10008.6.1.908
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum MediaTypeCode implements MediaType, EnumeratedCodedValue<MediaType> {

    Usb("110030", "USB Disk Emulation"),
    Email("110031", "Email"),
    CD("110032", "CD"),
    DVD("110033", "DVD"),
    CompactFlash("110034", "Compact Flash"),
    MMC("110035", "Multi-media Card"),
    SD("110036", "Secure Digital Card"),
    URI("110037", "URI"),
    Film("110010", "Film"),
    PaperDocument("110038", "Paper Document");

    @Getter
    private MediaType value;

    MediaTypeCode(String code, String displayName) {
        this.value = MediaType.of(code, "DCM", displayName);
    }

    public static MediaTypeCode enumForCode(String code) {
        return EnumeratedValueSet.enumForCode(MediaTypeCode.class, code);
    }
}