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
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.*;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.util.*;

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
     * Returns a map of additional Camel headers for the given ebXML object.
     * <p>
     * NB: not declared <tt>synchronized</tt>, because no ebXML object
     * is supposed to be marshaled/unmarshaled from multiple threads.
     *
     * @param ebXml key object.
     * @return additional Camel headers as a map.
     */
    public static Map<String, Object> getCamelHeaders(Object ebXml) {
        return DATA.computeIfAbsent(ebXml, k -> new HashMap<>());
    }


    public XdsJaxbDataBinding() {
        super();
        setUnmarshallerListener(new UnmarshallerListener());
        setMarshallerListener(new MarshallerListener());
    }


    public static boolean isExtraMetadataSlotName(String name) {
        return ((name != null) && name.startsWith("urn:") && (!name.startsWith("urn:ihe:")));
    }


    private static class UnmarshallerListener extends Unmarshaller.Listener {
        static final ThreadLocal<Boolean> RESULTS = new ThreadLocal<>();

        @Override
        public void afterUnmarshal(Object target, Object parent) {
            if (target instanceof ExtrinsicObjectType) {
                var ebXml = (ExtrinsicObjectType) target;
                findExtraMetadata(ebXml.getSlot(), ebXml);
            } else if (target instanceof RegistryPackageType) {
                var ebXml = (RegistryPackageType) target;
                findExtraMetadata(ebXml.getSlot(), ebXml);
            } else if (target instanceof AssociationType1) {
                var ebXml = (AssociationType1) target;
                findExtraMetadata(ebXml.getSlot(), ebXml);
            } else if ((target instanceof SubmitObjectsRequest) && Boolean.TRUE.equals(RESULTS.get())) {
                getCamelHeaders(target).put(SUBMISSION_SET_HAS_EXTRA_METADATA, Boolean.TRUE);
                RESULTS.remove();
            }
        }

        private static void findExtraMetadata(List<SlotType1> slots, ExtraMetadataHolder holder) {
            if (slots != null) {
                for (var slot : slots) {
                    var name = slot.getName();
                    if (isExtraMetadataSlotName(name)) {
                        var extraMetadata = holder.getExtraMetadata();
                        if (extraMetadata == null) {
                            extraMetadata = new HashMap<>();
                            holder.setExtraMetadata(extraMetadata);
                        }
                        extraMetadata.put(name, new ArrayList<>(slot.getValueList().getValue()));
                        RESULTS.set(Boolean.TRUE);
                    }
                }
            }
        }
    }


    private static class MarshallerListener extends Marshaller.Listener {
        @Override
        public void beforeMarshal(Object source) {
            if (source instanceof ExtrinsicObjectType) {
                var ebXml = (ExtrinsicObjectType) source;
                injectExtraMetadata(ebXml.getSlot(), ebXml);
            } else if (source instanceof RegistryPackageType) {
                var ebXml = (RegistryPackageType) source;
                injectExtraMetadata(ebXml.getSlot(), ebXml);
            } else if (source instanceof AssociationType1) {
                var ebXml = (AssociationType1) source;
                injectExtraMetadata(ebXml.getSlot(), ebXml);
            }
        }

        private static void injectExtraMetadata(List<SlotType1> slots, ExtraMetadataHolder holder) {
            if (holder.getExtraMetadata() != null) {
                holder.getExtraMetadata().entrySet().stream()
                        .filter(entry -> isExtraMetadataSlotName(entry.getKey()))
                        .forEach(entry -> {
                            var slot = new SlotType1();
                            slot.setName(entry.getKey());
                            var valueList = new ValueListType();
                            valueList.getValue().addAll(entry.getValue());
                            slot.setValueList(valueList);
                            slots.add(slot);
                        });
            }
        }
    }

}
