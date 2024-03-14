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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import java.io.Serializable;

/**
 * Representation of a localized string.<p>
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalizedString")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class LocalizedString implements Serializable {
    private static final long serialVersionUID = 4876325465142358849L;
    
    @XmlAttribute(name = "language")
    private String lang;
    @XmlTransient
    private String charset;
    @XmlValue
    private String value;
    
    /**
     * Constructs a localized string.
     */
    public LocalizedString() {
        this(null);
    }

    /**
     * Constructs a localized string.
     * @param value
     *          the value of the string.
     * @param lang
     *          the language that the string is in.
     * @param charset
     *          the charset used in the string.
     */
    public LocalizedString(String value, String lang, String charset) {
        setValue(value);
        setLang(lang);
        setCharset(charset);
    }

    /**
     * Constructs a localized string.
     * @param value
     *          the value of the string.
     */
    public LocalizedString(String value) {
        this(value, "en-US", "UTF-8");
    }

    /**
     * @return the language that the string is in.
     */
    public String getLang() {
        return lang;
    }
    
    /**
     * @param lang
     *          the language that the string is in.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    /**
     * @return the charset used in the string.
     */
    public String getCharset() {
        return charset;
    }
    
    /**
     * @param charset
     *          the charset used in the string.
     */
    public void setCharset(String charset) {
        this.charset = "UTF8".equalsIgnoreCase(charset) ? "UTF-8" : charset;
    }
    
    /**
     * @return the value of the string.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @param value
     *          the value of the string.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
