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
package org.openehealth.ipf.platform.manager.flowmanager.ui.properties;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.junit.Test;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;
import org.openehealth.ipf.platform.manager.flowmanager.mock.FlowInfoMock;
import org.openehealth.ipf.platform.manager.flowmanager.mock.FlowPartInfoMock;

/**
 * Tests if the property descriptor returns correct values.
 * 
 * @author Mitko Kolev
 */
public class FlowPropertiesSourceTest extends TestCase {

    @Test
    public void testPropertiesOfFlowWithDefaultValues() {
        FlowInfoMock flow = new FlowInfoMock();
        FlowPartInfoMock part = new FlowPartInfoMock("0");
        flow.getPartInfos().add(part);
        FlowPropertiesSource properties = new FlowPropertiesSource(flow);
        assertFlowPropertiesAreCorrect(properties, flow);
    }

    @Test
    public void testPropertiesOfFlowPartWithDefaultValues() {
        FlowInfoMock flow = new FlowInfoMock();
        FlowPartInfoMock part = new FlowPartInfoMock("0");
        flow.getPartInfos().add(part);
        FlowPropertiesSource properties = new FlowPropertiesSource(flow);
        assertFlowPartsPropertiesAreCorrect(properties, flow);
    }

    @Test
    public void testPropertiesOfFlowWithDCustomValues() {
        FlowInfoMock flow = new FlowInfoMock();

        flow.setAckCountExpected(3);
        flow.setReplayTime(new Date());
        flow.setReplayCount(5);
        flow.setNakCount(1);
        flow.setAckCount(12);
        flow.setCreationTime(new Date());
        flow.setReplayable(false);

        FlowPartInfoMock part = new FlowPartInfoMock("0");
        flow.getPartInfos().add(part);
        FlowPropertiesSource properties = new FlowPropertiesSource(flow);
        assertFlowPropertiesAreCorrect(properties, flow);
    }

    @Test
    public void testPropertiesOfFlowPartWithCustomValues() {
        FlowInfoMock flow = new FlowInfoMock();

        FlowPartInfoMock part = new FlowPartInfoMock("0");
        part.setContributionCount(2);
        part.setContributionDate(null);
        part.setFilterTime(new Date());
        part.setStatus(false);
        part.setFilterCount(4);
        part.setPathDuration(123123L);

        flow.getPartInfos().add(part);
        FlowPropertiesSource properties = new FlowPropertiesSource(flow);
        assertFlowPartsPropertiesAreCorrect(properties, flow);
    }

    private void assertFlowPropertiesAreCorrect(
            FlowPropertiesSource properties, IFlowInfo info) {
        IPropertyDescriptor[] descriptors = properties.getPropertyDescriptors();
        for (int t = 0; t < descriptors.length; t++) {
            IPropertyDescriptor descriptor = descriptors[t];

            assertDescriptorIsValid(descriptor);

            assertTrue(descriptor.getId() != null);
            Object id = descriptor.getId();
            final Object value = String
                    .valueOf(properties.getPropertyValue(id));

            if (id.equals(FlowPropertiesSource.REPLY_COUNT_ID)) {
                assertTrue("replay count should be valid", String.valueOf(
                        info.getReplayCount()).equals(value));
            } else if (id.equals(FlowPropertiesSource.PARTS_COUNT_ID)) {
                assertTrue("part count should be valid", String.valueOf(
                        String.valueOf(info.getPartInfos().size())).equals(
                        value));

            } else if (id.equals(FlowPropertiesSource.REPLYABLE_ID)) {
                assertTrue("replayable should be valid", value.equals(String
                        .valueOf(info.isReplayable())));
            } else if (id.equals(FlowPropertiesSource.ACK_COUNT_ID)) {
                assertTrue("ack count should be valid", value.equals(String
                        .valueOf(info.getAckCount())));
            } else if (id.equals(FlowPropertiesSource.ACK_EXPECTED_COUNT_ID)) {
                assertTrue("expected ack count should be valid", value
                        .equals(String.valueOf(info.getAckCountExpected())));
            } else if (id.equals(FlowPropertiesSource.REPLAY_TIME_ID)) {
                Date date = info.getReplayTime();
                compareDateWithNullCheck("replay date is valid", value, date);
            } else if (id.equals(FlowPropertiesSource.NAK_COUNT_ID)) {
                assertTrue("nak ack count should be valid", value.equals(String
                        .valueOf(info.getNakCount())));
            } else if (id.equals(FlowPropertiesSource.APPLICATION_ID)) {
                assertTrue("application should be valid", value.equals(info
                        .getApplication()));
            } else if (id.equals(FlowPropertiesSource.STATUS_ID)) {
                assertTrue("status should be valid", value.equals(info
                        .getStatus()));
            } else if (id.equals(FlowPropertiesSource.CREATION_TIME_ID)) {
                Date date = info.getCreationTime();
                compareDateWithNullCheck("creation date is valid", value, date);
            } else if (id.equals(FlowPropertiesSource.ID_ID)) {
                assertTrue("Id should be valid", value.equals(String
                        .valueOf(info.getIdentifier())));
            }
        }
    }

    private void compareDateWithNullCheck(String msg, Object value, Date date) {
        if (date == null) {
            assertTrue(value.equals(""));
            return;
        }
        final SimpleDateFormat format = new SimpleDateFormat(
                FlowPropertiesSource.DATE_FORMAT);
        String formatted = format.format(date);
        assertTrue(msg, value.equals(formatted));
    }

    private void assertDescriptorIsValid(IPropertyDescriptor descriptor) {
        assertTrue(descriptor.getDisplayName() != null);
        assertTrue(
                "The property should be localized with the default language",
                !descriptor.getDisplayName().startsWith("!"));
        assertTrue(descriptor.getDescription() != null);
        assertTrue(
                "The property should be localized with the default language",
                !descriptor.getDescription().startsWith("!"));
    }

    private void assertFlowPartsPropertiesAreCorrect(
            FlowPropertiesSource properties, IFlowInfo info) {
        IPropertyDescriptor[] descriptors = properties.getPropertyDescriptors();
        for (int t = 0; t < descriptors.length; t++) {
            final IPropertyDescriptor descriptor = descriptors[t];
            final String id = descriptor.getId().toString();
            final Object value = String
                    .valueOf(properties.getPropertyValue(id));

            assertDescriptorIsValid(descriptor);

            if (id.indexOf(FlowPropertiesSource.FLOW_PART_STATUS_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                assertTrue("status should be valid", value
                        .equals(i.getStatus()));
            } else if (id
                    .indexOf(FlowPropertiesSource.FLOW_PART_CONTRIBUTION_COUNT_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                assertTrue("contribution count should be valid", value
                        .equals(String.valueOf(i.getContributionCount())));
            } else if (id
                    .indexOf(FlowPropertiesSource.FLOW_PART_FILTER_TIME_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                Date d = i.getFilterTime();
                compareDateWithNullCheck("flowPartFilter time should be valid",
                        value, d);
            } else if (id
                    .indexOf(FlowPropertiesSource.FLOW_PART_CONTRIBUTION_TIME_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                Date d = i.getContributionTime();
                compareDateWithNullCheck("contribution time should be valid",
                        value, d);
            } else if (id
                    .indexOf(FlowPropertiesSource.FLOW_PART_DURATION_MS_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                assertTrue("path should be valid", value.equals(String
                        .valueOf(i.getPathDuration())));
            } else if (id.indexOf(FlowPropertiesSource.FLOW_PART_PATH_ID) >= 0) {
                IFlowPartInfo i = properties.getFlowPartById(id);
                assertTrue("path should be valid", value.equals(i.getPath()));
            }
        }
    }
}
