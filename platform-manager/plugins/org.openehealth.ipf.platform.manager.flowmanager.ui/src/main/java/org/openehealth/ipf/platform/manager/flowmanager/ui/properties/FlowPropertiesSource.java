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
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowInfo;
import org.openehealth.ipf.platform.manager.flowmanager.IFlowPartInfo;
import org.openehealth.ipf.platform.manager.flowmanager.ui.util.Messages;

/**
 * IPropertySource2 for both flows and aggregated flow parts (contained in the flows). 
 * 
 * @see org.eclipse.ui.views.properties.IPropertySource2
 * @see org.eclipse.ui.views.properties.IPropertyDescriptor
 * 
 * @author Mitko Kolev
 */
public class FlowPropertiesSource implements IPropertySource2 {

    protected final static Log log = LogFactory
            .getLog(FlowPropertiesSource.class);

    protected final static String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";

    // flow

    protected static final String PARTS_COUNT_ID = "flow.parts.count";

    protected static final String SELECTED_FLOW_ID = "selected.flow";

    protected static final String REPLY_COUNT_ID = "flow.info.reply.count";

    protected static final String REPLYABLE_ID = "flow.info.replyable";

    protected static final String ACK_COUNT_ID = "flow.info.ack.count";

    protected static final String ACK_EXPECTED_COUNT_ID = "flow.info.ack.expected.count";

    protected static final String REPLAY_TIME_ID = "flow.info.reply.time";

    protected static final String NAK_COUNT_ID = "flow.info.nak.count";

    protected static final String CREATION_TIME_ID = "flow.info.creation.time";

    protected static final String ID_ID = "flow.info.id";

    protected static final String STATUS_ID = "flow.info.status";

    protected static final String APPLICATION_ID = "flow.info.application";

    // flow parts
    protected static final String FLOW_PART_ID = "flow.part";

    protected static final String FLOW_PART_PATH_ID = "flow.part.path";

    protected static final String FLOW_PART_STATUS_ID = "flow.part.status";

    protected static final String FLOW_PART_CONTRIBUTION_COUNT_ID = "flow.part.contribution.count";

    protected static final String FLOW_PART_FILTER_TIME_ID = "flow.part.filter.time";

    protected static final String FLOW_PART_CONTRIBUTION_TIME_ID = "flow.part.contribution.time";

    protected static final String FLOW_PART_DURATION_MS_ID = "flow.part.duration.ms";

    // descriptions

    protected static final String PARTS_COUNT_DESCRIPTION = "flow.parts.count.description";

    protected static final String REPLY_COUNT_DESCRIPTION = "flow.info.reply.count.description";

    protected static final String REPLYABLE_DESCRIPTION = "flow.info.replyable.description";

    protected static final String ACK_COUNT_DESCRIPTION = "flow.info.ack.count.description";

    protected static final String ACK_EXPECTED_COUNT_DESCRIPTION = "flow.info.ack.expected.count.description";

    protected static final String REPLY_TIME_DESCRIPTION = "flow.info.reply.time.description";

    protected static final String NAK_COUNT_DESCRIPTION = "flow.info.nak.count.description";

    protected static final String CREATION_TIME_DESCRIPTION = "flow.info.creation.time.description";

    protected static final String ID_DESCRIPTION = "flow.info.id.description";

    protected static final String STATUS_DESCRIPTION = "flow.info.status.description";

    protected static final String APPLICATION_DESCRIPTION = "flow.info.application.description";

    // part descriptions
    protected static final String FLOW_PART_STATUS_DESCRIPTION = "flow.part.status.description";

    protected static final String FLOW_PART_CONTRIBUTION_COUNT_DESCRIPTION = "flow.part.contribution.count.description";

    protected static final String FLOW_PART_FILTER_TIME_DESCRIPTION = "flow.part.filter.time.description";

    protected static final String FLOW_PART_CONTRIBUTION_TIME_DESCRIPTION = "flow.part.contribution.time.description";

    protected static final String FLOW_PART_DURATION_MS_DESCRIPTION = "flow.part.duration.ms.description";

    private final IPropertyDescriptor[] propertyDescriptors;

    private final IFlowInfo flowInfo;

    public FlowPropertiesSource(IFlowInfo info) {
        this.flowInfo = info;
        propertyDescriptors = initializePropertyDescriptors();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource2#isPropertyResettable
     * (java.lang.Object)
     */
    @Override
    public boolean isPropertyResettable(Object id) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource2#isPropertySet(java.lang
     * .Object)
     */
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    @Override
    public Object getEditableValue() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return this.propertyDescriptors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
     * .lang.Object)
     */
    @Override
    public Object getPropertyValue(Object id) {
        String representationStringOfId = id.toString();
        try {
            if (id.equals(REPLY_COUNT_ID)) {
                return this.flowInfo.getReplayCount();
            } else if (id.equals(PARTS_COUNT_ID)) {
                return String.valueOf(flowInfo.getPartInfos().size());
            } else if (id.equals(REPLYABLE_ID)) {
                return flowInfo.isReplayable();
            } else if (id.equals(ACK_COUNT_ID)) {
                return flowInfo.getAckCount();
            } else if (id.equals(ACK_EXPECTED_COUNT_ID)) {
                return flowInfo.getAckCountExpected();
            } else if (id.equals(REPLAY_TIME_ID)) {
                Date date = flowInfo.getReplayTime();
                if (date == null) {
                    return "";
                }
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                return format.format(date);
            } else if (id.equals(NAK_COUNT_ID)) {
                return flowInfo.getNakCount();
            } else if (id.equals(APPLICATION_ID)) {
                return flowInfo.getApplication();
            } else if (id.equals(STATUS_ID)) {
                return flowInfo.getStatus();
            } else if (id.equals(CREATION_TIME_ID)) {
                Date date = flowInfo.getCreationTime();
                if (date == null) {
                    return "";
                }
                SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                return format.format(date);
            } else if (id.equals(ID_ID)) {
                return flowInfo.getIdentifier();
            } else if (representationStringOfId.indexOf(FLOW_PART_STATUS_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                return i.getStatus();

            } else if (representationStringOfId
                    .indexOf(FLOW_PART_CONTRIBUTION_COUNT_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                return i.getContributionCount();

            } else if (representationStringOfId
                    .indexOf(FLOW_PART_FILTER_TIME_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                Date d = i.getFilterTime();
                if (d != null) {
                    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                    return format.format(i.getFilterTime());
                } else
                    return "";

            } else if (representationStringOfId
                    .indexOf(FLOW_PART_CONTRIBUTION_TIME_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                Date d = i.getContributionTime();
                if (d != null) {
                    SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                    return format.format(i.getContributionTime());
                } else
                    return "";
            } else if (representationStringOfId
                    .indexOf(FLOW_PART_DURATION_MS_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                long pathDuration = i.getPathDuration();
                String result = String.valueOf(pathDuration);
                return result;
            } else if (representationStringOfId.indexOf(FLOW_PART_PATH_ID) >= 0) {
                IFlowPartInfo i = getFlowPartById(id);
                String path = i.getPath();
                return path;
            }
        } catch (Exception e) {
            log.error("Error in the property: " + id, e);
        }
        return "";
    }

    protected IFlowPartInfo getFlowPartById(Object id) {
        String flowPath = getPathFromFlowPartPropertyId(id.toString());
        for (IFlowPartInfo partInfo : this.flowInfo.getPartInfos()) {
            if (partInfo.getPath().equals(flowPath)) {
                return partInfo;
            }
        }
        throw new RuntimeException("Cannot parse the flowPath " + flowPath);
    }

    /**
     * builds a property id for the flow part with path <code>flowPath</code>.
     * 
     * @param propertyId
     * @param flowPath
     * @return
     */
    protected String budildFlowPartPropertyId(String propertyId, String flowPath) {
        return propertyId + "@@" + flowPath;
    }

    /**
     * Returns null if the id is not correct
     * 
     * @param id
     * @return
     */
    protected String getPathFromFlowPartPropertyId(String id) {
        if (id == null)
            return null;

        int delimeterIndex = id.indexOf("@@");
        String path = id.substring(delimeterIndex + "@@".length());
        return path;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
     * .lang.Object)
     */
    @Override
    public void resetPropertyValue(Object id) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void setPropertyValue(Object id, Object value) {

    }

    /**
     * Returns the descriptors of the properties for the connection
     * 
     * @return
     */
    private IPropertyDescriptor[] initializePropertyDescriptors() {
        // the flow properties
        String flowCategory = Messages.getLabelString(SELECTED_FLOW_ID) + " { "
                + flowInfo.getIdentifier() + " }";
        ArrayList<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

        PropertyDescriptor descriptor = new PropertyDescriptor(ID_ID, Messages
                .getLabelString(ID_ID));
        descriptor.setDescription(Messages.getLabelString(ID_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(STATUS_ID, Messages
                .getLabelString(STATUS_ID));
        descriptor.setDescription(Messages.getLabelString(STATUS_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(CREATION_TIME_ID, Messages
                .getLabelString(CREATION_TIME_ID));
        descriptor.setDescription(Messages
                .getLabelString(CREATION_TIME_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(REPLYABLE_ID, Messages
                .getLabelString(REPLYABLE_ID));
        descriptor.setDescription(Messages
                .getLabelString(REPLYABLE_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(ACK_COUNT_ID, Messages
                .getLabelString(ACK_COUNT_ID));
        descriptor.setDescription(Messages
                .getLabelString(ACK_COUNT_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(ACK_EXPECTED_COUNT_ID, Messages
                .getLabelString(ACK_EXPECTED_COUNT_ID));
        descriptor.setDescription(Messages
                .getLabelString(ACK_EXPECTED_COUNT_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(REPLAY_TIME_ID, Messages
                .getLabelString(REPLAY_TIME_ID));
        descriptor.setDescription(Messages
                .getLabelString(REPLY_TIME_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(NAK_COUNT_ID, Messages
                .getLabelString(NAK_COUNT_ID));
        descriptor.setDescription(Messages
                .getLabelString(NAK_COUNT_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(REPLY_COUNT_ID, Messages
                .getLabelString(REPLY_COUNT_ID));
        descriptor.setDescription(Messages
                .getLabelString(REPLY_COUNT_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(APPLICATION_ID, Messages
                .getLabelString(APPLICATION_ID));
        descriptor.setDescription(Messages
                .getLabelString(APPLICATION_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        descriptor = new PropertyDescriptor(PARTS_COUNT_ID, Messages
                .getLabelString(PARTS_COUNT_ID));
        descriptor.setDescription(Messages
                .getLabelString(PARTS_COUNT_DESCRIPTION));
        descriptor.setCategory(flowCategory);
        descriptors.add(descriptor);

        // The part properties
        for (IFlowPartInfo info : flowInfo.getPartInfos()) {
            String path = info.getPath();

            String thisPathCategory = Messages.getLabelString(FLOW_PART_ID)
                    + " { " + info.getPath() + " }";

            String pid = budildFlowPartPropertyId(FLOW_PART_STATUS_ID, path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_STATUS_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_STATUS_DESCRIPTION));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

            pid = budildFlowPartPropertyId(FLOW_PART_CONTRIBUTION_COUNT_ID,
                    path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_CONTRIBUTION_COUNT_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_CONTRIBUTION_COUNT_DESCRIPTION));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

            pid = budildFlowPartPropertyId(FLOW_PART_PATH_ID, path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_PATH_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_PATH_ID));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

            pid = budildFlowPartPropertyId(FLOW_PART_FILTER_TIME_ID, path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_FILTER_TIME_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_FILTER_TIME_DESCRIPTION));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

            pid = budildFlowPartPropertyId(FLOW_PART_CONTRIBUTION_TIME_ID, path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_CONTRIBUTION_TIME_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_CONTRIBUTION_TIME_DESCRIPTION));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

            pid = budildFlowPartPropertyId(FLOW_PART_DURATION_MS_ID, path);
            descriptor = new PropertyDescriptor(pid, Messages
                    .getLabelString(FLOW_PART_DURATION_MS_ID));
            descriptor.setDescription(Messages
                    .getLabelString(FLOW_PART_DURATION_MS_DESCRIPTION));
            descriptor.setCategory(thisPathCategory);
            descriptor.setAlwaysIncompatible(true);
            descriptors.add(descriptor);

        }

        return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
    }

}
