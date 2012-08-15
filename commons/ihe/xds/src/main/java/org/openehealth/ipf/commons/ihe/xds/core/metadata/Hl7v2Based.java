/*
 * Copyright 2012 the original author or authors.
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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;
import ca.uhn.hl7v2.model.v25.datatype.HD;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * An XDS model object backed up by an HL7 v2 element.
 * @param <T>
 *     HAPI composite type which corresponds to the HL7 v2 element.
 *
 * @author Dmytro Rud
 */
@XmlTransient
abstract public class Hl7v2Based<T extends Composite> implements Serializable {
    private static final long serialVersionUID = 5463666004063275303L;

    protected static final Message MESSAGE;
    static {
        MESSAGE = new ACK();
        MESSAGE.setValidationContext(null);
    }


    private final T hapiObject;


    /**
     * Default constructor, for serialization purposes only.
     */
    protected Hl7v2Based() {
        throw new IllegalStateException("this default constructor is defined only to satisfy JAXB requirements");
    }


    /**
     * Constructor.
     * @param hapiObject
     *      HAPI composite object.
     */
    protected Hl7v2Based(T hapiObject) {
        this.hapiObject = Validate.notNull(hapiObject, "HAPI object");
    }


    /**
     * Parses the given HL7 v2 element info an XDS simplified model object.
     * @param hl7String
     *      HL7 v2 element as a String.
     * @param xdsModelClass
     *      class of the XDS model object to be generates.
     * @param <T>
     *      class of HAPI composite object which should hold the HL7 element.
     * @param <C>
     *      class of XDS model object.
     * @return
     *      generated XDS model object or <code>null</code> when the given
     *      HL7 v2 element is <code>null</code> or empty.
     */
    public static <T extends Composite, C extends Hl7v2Based<T>> C parse(
            String hl7String,
            Class<C> xdsModelClass)
    {
        if (StringUtils.isEmpty(hl7String)) {
            return null;
        }

        try {
            C xdsModelObject = xdsModelClass.newInstance();
            MESSAGE.getParser().parse(xdsModelObject.getHapiObject(), hl7String, XdsHl7v2Renderer.ENCODING_CHARACTERS);
            // TODO: can the xdsModelObject be empty when the String is not empty?
            return xdsModelObject.isEmpty() ? null : xdsModelObject;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Renders this XDS model object using the XDS-specific
     * {@link XdsHl7v2Renderer#encode(ca.uhn.hl7v2.model.Composite) HL7 v2 renderer},
     * i.e. with applying IHE TF rules regarding unwanted components.
     * @return
     *      HL7 v2 representation of this XDS model object, may be an empty String.
     */
    protected String render() {
        return XdsHl7v2Renderer.encode(hapiObject);
    }


    /**
     * Renders the given XDS model object as an HL7 v2 element according to the
     * XDS specification, i.e. with applying IHE TF rules regarding unwanted components.
     *
     * @param xdsModelObject
     *      XDS model object.
     * @return
     *      HL7 v2 representation of the given object, or <code>null</code>
     *      when the given object is <code>null</code> or empty.
     */
    public static String render(Hl7v2Based xdsModelObject) {
        return (xdsModelObject != null) ? StringUtils.trimToNull(xdsModelObject.render()) : null;
    }


    /**
     * Renders the given XDS model object as an HL7 v2 element
     * without applying IHE TF rules regarding unwanted components.
     *
     * @param xdsModelObject
     *      XDS model object.
     * @return
     *      HL7 v2 representation of the given object, or an empty string
     *      when the given object is <code>null</code> or empty.
     */
    public static String rawRender(Hl7v2Based xdsModelObject) {
        return (xdsModelObject != null)
                ? PipeParser.encode(xdsModelObject.getHapiObject(), XdsHl7v2Renderer.ENCODING_CHARACTERS)
                : "";
    }


    /**
     * @return
     *      <code>true</code> iff this XDS model object does not contain any usable data.
     */
    public boolean isEmpty() {
        return render().isEmpty();
    }


    /**
     * @return
     *      HAPI composite holding the HL7 v2 element
     *      which corresponds to this XDS model object.
     */
    public T getHapiObject() {
        return hapiObject;
    }


    protected static void setValue(Primitive p, String value) {
        try {
            p.setValue(value);
        } catch (DataTypeException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Helper method used for copying data elements of an assigning authority.
     * @param assigningAuthority
     *      source assigning authority.
     * @param target
     *      target HL7 v2 HD element.
     */
    protected static void setAssigningAuthority(AssigningAuthority assigningAuthority, HD target) {
        if (assigningAuthority != null) {
            HD source = assigningAuthority.getHapiObject().getInternal();
            setValue(target.getHd1_NamespaceID(), source.getHd1_NamespaceID().getValue());
            setValue(target.getHd2_UniversalID(), source.getHd2_UniversalID().getValue());
            setValue(target.getHd3_UniversalIDType(), source.getHd3_UniversalIDType().getValue());
        } else {
            target.clear();
        }
    }


    /**
     * Fake enclosing element for an HL7 v2 sub-component, necessary for correct rendering and parsing.
     */
    static class Holder<T extends Type> extends AbstractType implements Composite {
        private static final long serialVersionUID = -9084300955263787034L;

        private Type[] data = new Type[1];

        public Holder(T t) {
            super(t.getMessage());
            data[0] = t;
        }

        public T getInternal() {
            return (T) data[0];
        }

        @Override
        public Type[] getComponents() {
            return this.data;
        }

        @Override
        public Type getComponent(int number) throws DataTypeException {
            try {
                return data[number];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DataTypeException("please do not use this class");
            }
        }
    }

}
