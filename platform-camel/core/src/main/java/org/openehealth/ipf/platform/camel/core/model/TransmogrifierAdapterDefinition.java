/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.model;

import org.apache.camel.spi.Metadata;
import org.openehealth.ipf.commons.core.modules.api.Transmogrifier;
import org.openehealth.ipf.commons.xml.SchematronTransmogrifier;
import org.openehealth.ipf.commons.xml.XqjTransmogrifier;
import org.openehealth.ipf.commons.xml.XsltTransmogrifier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.stream.StreamSource;

import static org.apache.camel.builder.Builder.bodyAs;

/**
 * @author Martin Krasser
 */
@Metadata(label = "ipf,eip,transformation")
@XmlRootElement(name = "transmogrify")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransmogrifierAdapterDefinition extends ProcessorAdapterDefinition {

    @XmlTransient
    private Transmogrifier<?, ?> transmogrifier;
    @XmlAttribute
    private String transmogrifierBean;

    public TransmogrifierAdapterDefinition() {
    }

    public TransmogrifierAdapterDefinition(Transmogrifier<?, ?> transmogrifier) {
        this.transmogrifier = transmogrifier;
        params().headers();
    }

    public TransmogrifierAdapterDefinition(String transmogrifierBean) {
        this.transmogrifierBean = transmogrifierBean;
        params().headers();
    }

    @Override
    public String toString() {
        return "TransmogrifierAdapter[" + getOutputs() + "]";
    }

    @Override
    public String getShortName() {
        return "transmogrifierAdapter";
    }
    
    /**
     * Specifies that the transformation is done via XSLT
     */
    public TransmogrifierAdapterDefinition xslt() {
        transmogrifier = new XsltTransmogrifier<>(String.class);
        return (TransmogrifierAdapterDefinition)input(bodyAs(StreamSource.class));
    }
    
    /**
     * Specifies that the transformation is done via XSLT
     * 
     * @param clazz
     *            the resulting type of the message body after the
     *            transformation
     */
    public <T> TransmogrifierAdapterDefinition xslt(Class<T> clazz) {
        transmogrifier = new XsltTransmogrifier<>(clazz);
        return (TransmogrifierAdapterDefinition)input(bodyAs(StreamSource.class));
    }
    
    /**
     * Specifies that the transformation is done via XQuery
     * 
     */
    public TransmogrifierAdapterDefinition xquery() {
        transmogrifier = new XqjTransmogrifier<>(String.class);
        return (TransmogrifierAdapterDefinition) input(bodyAs(StreamSource.class));
    }

    /**
     * Specifies that the transformation is done via XQuery
     * 
     * @param clazz
     *            the resulting type of the message body after the
     *            transformation
     */
    public <T> TransmogrifierAdapterDefinition xquery(Class<T> clazz) {
        transmogrifier = new XqjTransmogrifier<>(clazz);
        return (TransmogrifierAdapterDefinition) input(bodyAs(StreamSource.class));
    }

    /**
     * Specifies that a schematron validation report is generated
     */
    public TransmogrifierAdapterDefinition schematron() {
        transmogrifier = new SchematronTransmogrifier<>(String.class);
        return (TransmogrifierAdapterDefinition)input(bodyAs(StreamSource.class));
    }

    /**
     * Specifies that a schematron validation report is generated
     * @param clazz
     *          the resulting type of the message bodyf after the transformation
     */
    public <T> TransmogrifierAdapterDefinition schematron(Class<T> clazz) {
        transmogrifier = new SchematronTransmogrifier<>(clazz);
        return (TransmogrifierAdapterDefinition)input(bodyAs(StreamSource.class));
    }

    public Transmogrifier<?, ?> getTransmogrifier() {
        return transmogrifier;
    }

    public String getTransmogrifierBean() {
        return transmogrifierBean;
    }
}
