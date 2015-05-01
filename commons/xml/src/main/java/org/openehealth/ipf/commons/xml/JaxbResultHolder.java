/*
 * Copyright 2015 the original author or authors.
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

import org.openehealth.ipf.commons.xml.svrl.SchematronOutput;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Result;

/**
 * @author Dmytro Rud
 */
abstract public class JaxbResultHolder<T>  implements ResultHolder<T> {
    private final JAXBContext jaxbContext;

    private JAXBResult result;

    /**
     * @param clazz
     *      Target class for unmarshalling via JAXB.
     */
    public JaxbResultHolder(Class<T> clazz) {
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T getResult() {
        try {
            return (T) result.getResult();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result createResult() {
        try {
            result = new JAXBResult(jaxbContext.createUnmarshaller());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
