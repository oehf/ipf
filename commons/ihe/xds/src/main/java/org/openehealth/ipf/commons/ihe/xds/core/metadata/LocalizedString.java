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

import javax.xml.bind.annotation.*;
import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Representation of a localized string.<p>
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalizedString")
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
        lang = "en-US";
        charset = "UTF-8";
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
        this.value = value;
        this.lang = lang;
        this.charset = charset;
    }

    /**
     * Constructs a localized string.
     * @param value
     *          the value of the string.
     */
    public LocalizedString(String value) {
        this.value = value;
        lang = "en-US";
        charset = "UTF-8";
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
        this.charset = charset;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((charset == null) ? 0 : charset.hashCode());
        result = prime * result + ((lang == null) ? 0 : lang.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocalizedString other = (LocalizedString) obj;
        if (charset == null) {
            if (other.charset != null)
                return false;
        } else if (!charset.equals(other.charset))
            return false;
        if (lang == null) {
            if (other.lang != null)
                return false;
        } else if (!lang.equals(other.lang))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
