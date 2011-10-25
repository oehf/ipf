/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various XML utilities.
 * @author Dmytro Rud
 */
abstract public class XmlUtils {

    private static final Pattern ROOT_ELEMENT_PATTERN = Pattern.compile(
        "(?:<\\?xml.+?\\?>(?:\\s*<\\!--.*?-->)*)?" +           // optional prolog and comments after it
        "\\s*<(?:[\\w\\.-]+?:)?([\\w\\.-]+)(?:\\s|(?:/?>))",   // open tag of the root element
        Pattern.DOTALL
    );


    private XmlUtils() {
        throw new IllegalStateException("Cannot instantiate helper class");
    }


    /**
     * Creates an XML Source from the given XML String.
     * @param s
     *      XML String.
     * @return
     *      XML Source.
     */
    public static Source source(String s) {
        return new StreamSource(new StringReader(s));
    }


   /**
    * Returns local name of the root element of the XML document represented
    * by the given string, or <code>null</code>, when the given string does
    * not contain valid XML.
    * @param s
    *       XML string.
    * @return
    *       root element local name, or <code>null</code> when it could not be determined.
    */
   public static String rootElementName(String s) {
       if (s == null) {
           return null;
       }
       Matcher matcher = ROOT_ELEMENT_PATTERN.matcher(s);
       return (matcher.find() && (matcher.start() == 0)) ? matcher.group(1) : null;
   }

}
