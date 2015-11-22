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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;


/**
 * Thrown if XDS meta data did not match the expectations.
 * @author Jens Riemschneider
 */
public class XDSMetaDataException extends ValidationException {
    private static final long serialVersionUID = -394009702858390335L;
    
    private final ValidationMessage validationMessage;

    /**
     * Constructs the exception.
     * @param validationMessage
     *          the validation message.
     * @param details
     *          objects required by the message text formatting.
     */
    public XDSMetaDataException(ValidationMessage validationMessage, Object... details) {
        super(String.format(validationMessage.getText(), unwrapQueryParameterNames(details)));
        this.validationMessage = validationMessage;
    }

    /**
     * @return the validation message.
     */
    public ValidationMessage getValidationMessage() {
        return validationMessage;
    }

    /**
     * Recursively replaces all
     * {@link org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter}
     * elements found in the given array with their corresponding ebXML slot names.
     * @param array
     *      array potentially containing
     *      {@link org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter}
     *      elements
     * @return the same array instance, possibly modified in-place.
     */
    private static Object[] unwrapQueryParameterNames(Object[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] instanceof QueryParameter) {
                QueryParameter param = (QueryParameter) array[i];
                array[i] = param.getSlotName();
            }
            else if (array[i] instanceof QueryParameter[]) {
                QueryParameter[] params = (QueryParameter[]) array[i];
                if (params.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (QueryParameter param : params) {
                        sb.append(", ").append(param.getSlotName());
                    }
                    array[i] = sb.replace(0, 2, "[").append(']').toString();
                }
            }
        }
        return array;
    }

}
