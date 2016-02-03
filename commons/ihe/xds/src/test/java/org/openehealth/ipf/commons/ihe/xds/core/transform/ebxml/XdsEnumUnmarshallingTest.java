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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAllQuery;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Rud
 */
public class XdsEnumUnmarshallingTest {

    @Test
    public void testXdsEnumUnmarshalling() throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("GetAllQuery_with_EnumTypes.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(QueryRegistry.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        QueryRegistry queryRegistry = (QueryRegistry) unmarshaller.unmarshal(stream);
        GetAllQuery query = (GetAllQuery) queryRegistry.getQuery();
        stream.close();

        List<AvailabilityStatus> statuses = query.getStatusDocuments();
        assertEquals(9, statuses.size());

        // status 0 -- official, and marked as official -- shall be accepted as is
        assertEquals(AvailabilityStatus.APPROVED, statuses.get(0));

        // status 1 -- not official, but marked as official -- shall be recognized as user-defined
        assertEquals(XdsEnum.Type.USER_DEFINED, statuses.get(1).getType());
        assertEquals("UserDefined", statuses.get(1).getJaxbValue());

        // status 2 -- invalid, but marked as official -- shall be recognized as invalid
        assertEquals(XdsEnum.Type.INVALID, statuses.get(2).getType());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:StatusType:SuperDuper", statuses.get(2).getJaxbValue());

        // status 3 -- could have been official, but marked as user-defined -- shall be accepted as is
        assertEquals(XdsEnum.Type.USER_DEFINED, statuses.get(3).getType());
        assertEquals(AvailabilityStatus.APPROVED.getJaxbValue(), statuses.get(3).getJaxbValue());

        // status 4 -- user-defined, and marked as user-defined -- shall be accepted as is
        assertEquals(XdsEnum.Type.USER_DEFINED, statuses.get(4).getType());
        assertEquals("UserDefined", statuses.get(4).getJaxbValue());

        // status 5 -- invalid, but marked as user-defined -- shall be recognized as invalid
        assertEquals(XdsEnum.Type.INVALID, statuses.get(5).getType());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:StatusType:SuperDuper", statuses.get(5).getJaxbValue());

        // status 6 -- could have been official, but marked as invalid -- shall be accepted as is
        assertEquals(XdsEnum.Type.INVALID, statuses.get(6).getType());
        assertEquals(AvailabilityStatus.APPROVED.getJaxbValue(), statuses.get(6).getJaxbValue());

        // status 7 -- could have been user-defined, but marked as invalid -- shall be accepted as is
        assertEquals(XdsEnum.Type.INVALID, statuses.get(7).getType());
        assertEquals("UserDefined", statuses.get(7).getJaxbValue());

        // status 8 -- invalid and marked as invalid -- shall be accepted as is
        assertEquals(XdsEnum.Type.INVALID, statuses.get(8).getType());
        assertEquals("urn:oasis:names:tc:ebxml-regrep:StatusType:SuperDuper", statuses.get(8).getJaxbValue());
    }
}
