package org.openehealth.ipf.platform.camel.event;

import org.openehealth.ipf.commons.event.EventChannelAdapter;
import org.openehealth.ipf.commons.event.EventEngine;

public class MyAdapter extends EventChannelAdapter {
    /**
     * Constructs the channel adapter 
     * @param engine
     *          the event engine that is called to distribute event objects
     */
    public MyAdapter(EventEngine engine) {
        super(engine);
    }
}
