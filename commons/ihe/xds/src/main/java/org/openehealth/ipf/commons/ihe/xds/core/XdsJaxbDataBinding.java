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
package org.openehealth.ipf.commons.ihe.xds.core;

import org.apache.cxf.jaxb.JAXBDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.SlotType1;

import javax.xml.bind.Unmarshaller;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Data binding specific for the XDS data model --- gathers some additional
 * information pieces for elements of request messages while they are being
 * unmarshalled from XML to ebXML POJOs.  These information pieces are
 * intended to be propagated into Camel route as message headers.
 *
 * @author Dmytro Rud
 */
public class XdsJaxbDataBinding extends JAXBDataBinding {

    public static final String SUBMISSION_SET_HAS_EXTRA_METADATA =
            XdsJaxbDataBinding.class.getName() + ".submission.set.has.extra.metadata";

    private static final Map<Object, Map<String, Object>> DATA =
            Collections.synchronizedMap(new WeakHashMap<Object, Map<String, Object>>());


    /**
     * Returns an information holder for the given object.
     * <p>
     * NB: not declared <tt>synchronized</tt>, because no object
     * is supposed to be processed from multiple threads.
     * @param x
     *      key object.
     * @return
     *      an existing ot a freshly created information holder.
     */
    public static Map<String, Object> getMap(Object x) {
        Map<String, Object> map = DATA.get(x);
        if (map == null) {
            map = new HashMap<String, Object>();
            DATA.put(x, map);
        }
        return map;
    }


    public XdsJaxbDataBinding() {
        super();
        setUnmarshallerListener(new UnmarshallerListener());
    }


    private static class UnmarshallerListener extends Unmarshaller.Listener {
        static final ThreadLocal<Boolean> RESULTS = new ThreadLocal<Boolean>();

        @Override
        public void afterUnmarshal(Object target, Object parent) {
            if (target instanceof SlotType1) {
                SlotType1 slot = (SlotType1) target;
                String name = slot.getName();
                if ((name != null) && name.startsWith("urn:") && (! name.startsWith("urn:ihe:"))) {
                    RESULTS.set(Boolean.TRUE);
                }
            }
            else if ((target instanceof SubmitObjectsRequest) && Boolean.TRUE.equals(RESULTS.get())) {
                getMap(target).put(SUBMISSION_SET_HAS_EXTRA_METADATA, Boolean.TRUE);
            }
        }
    }

}
