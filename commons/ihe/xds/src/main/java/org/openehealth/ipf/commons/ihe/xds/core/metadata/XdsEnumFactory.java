/*
 * Copyright 2016 the original author or authors.
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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum.Type;

/**
 * Factory for converting codes between ebXML and the simplified IPF XDS data model.
 * @author Dmytro Rud
 */
@Slf4j
abstract public class XdsEnumFactory<T extends XdsEnum> {

    /**
     * @return list of official codes values for the given enumeration type.
     */
    abstract public T[] getOfficialValues();

    /**
     * @return a code with the given type ({@link Type#USER_DEFINED} or {@link Type#INVALID})
     *      and the given string as value.
     */
    abstract protected T createCode(XdsEnum.Type type, String ebXML);

    /**
     * @return <code>true</code> if the given string value can serve
     *      as a user-defined code, <code>false</code> otherwise.
     *      <p>
     *      <code>false</code> per default, shall be overwritten when necessary.
     */
    protected boolean canBeUserDefined(String ebXML) {
        return false;
    }

    /**
     * @return an ebXML string value for the given code, specific to the ebXML version (2.1 or 3.0).
     */
    abstract public String getEbXML(T code);

    /**
     * @param code the code, can be <code>null</code>.
     * @return ebXML representation of the label,
     *      or <code>null</code> if the parameter was <code>null</code>.
     */
    public String toEbXML(T code) {
        if (code == null) {
            return null;
        }

        String ebXML = getEbXML(code);

        if (code.getType() == Type.INVALID) {
            log.info("Attempt to use an invalid {} value '{}'", code.getClass().getSimpleName(), ebXML);
        }

        return ebXML;
    }

    /**
     * @param ebXML the ebXML string to look for. Can be <code>null</code>.
     * @return
     * <ul>
     *   <li><code>null</code> if the input was <code>null</code>,</li>
     *   <li>code with type {@link Type#OFFICIAL} if an official code could be matched,</li>
     *   <li>code with type {@link Type#USER_DEFINED} if the coding system allows to have
     *       user-defined codes, and the provided string can represent one,</li>
     *   <li>code with type {@link Type#INVALID} the given string represents neither
     *       an official nor a user-defined code.</li>
     * </ul>
     */
    public T fromEbXML(String ebXML) {
        if (StringUtils.isEmpty(ebXML)) {
            return null;
        }

        for (T code : getOfficialValues()) {
            if (ebXML.equals(getEbXML(code))) {
                return code;
            }
        }

        if (canBeUserDefined(ebXML)) {
            return createCode(Type.USER_DEFINED, ebXML);
        }

        T code = createCode(Type.INVALID, ebXML);
        log.info("'{}' is an invalid ebXML value for {}", ebXML, code.getClass().getSimpleName());
        return code;
    }

}
