/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions;

import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV;
import org.herasaf.xacml.core.function.AbstractFunction;
import org.herasaf.xacml.core.function.FunctionProcessingException;

/**
 * @author Dmytro Rud
 */
public class CvEqualFunction extends AbstractFunction {

    public static final String ID = "urn:hl7-org:v3:function:CV-equal";

    @Override
    public String getFunctionId() {
        return ID;
    }

    @Override
    public Object handle(Object... args) throws FunctionProcessingException {
        if (args.length != 2) {
            throw new FunctionProcessingException("Invalid number of parameters.");
        }
        try {
            CV arg0 = (CV) args[0];
            CV arg1 = (CV) args[1];
            return arg0.getCode().equals(arg1.getCode()) && arg0.getCodeSystem().equals(arg1.getCodeSystem());

        } catch (ClassCastException e) {
            throw new FunctionProcessingException("The arguments are of the wrong datatype", e);
        } catch (Exception e) {
            throw new FunctionProcessingException(e);
        }
    }
}
