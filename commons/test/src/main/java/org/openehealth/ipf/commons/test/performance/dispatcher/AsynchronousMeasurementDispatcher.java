/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.test.performance.dispatcher;

import static org.apache.commons.lang.Validate.notNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;

/**
 * Extends the <code>MeasurementDispatcher</code> functionality providing
 * asynchronous dispatching of measurement history objects. The measurement
 * history objects are stored in a <code>BlockingQueue</code>, which is consumed
 * by a single thread.
 * 
 * @see MeasurementDispatcher
 * @author Mitko Kolev
 */
public class AsynchronousMeasurementDispatcher extends MeasurementDispatcher
        implements Runnable {

    private final BlockingQueue<MeasurementHistory> queue;

    private final Thread consumerThread;

    private final static Log LOG = LogFactory
            .getLog(AsynchronousMeasurementDispatcher.class);

    /**
     * Creates a <code>AsynchronousMeasurementDispatcher</code> instance
     */
    public AsynchronousMeasurementDispatcher() {
        queue = new LinkedBlockingQueue<MeasurementHistory>();
        consumerThread = new Thread(this);
        consumerThread.setDaemon(true);
        consumerThread.setName("Measurement history dispatcher");
        consumerThread.start();

        LOG.info(AsynchronousMeasurementDispatcher.class.getSimpleName()
                + " started!");
    }

    /**
     * Fills the blocking queue with the given <code>measurementHistory</code>
     * 
     * @param data
     *            a <code>MeasurementHistory</code> instance
     */
    @Override
    public void dispatch(MeasurementHistory measurementHistory) {
        notNull(measurementHistory, "The measurementHistory must not be null!");
        try {
            queue.put(measurementHistory);

        } catch (InterruptedException e) {
            LOG.warn("Inserting a measurement history object in the "
                    + AsynchronousMeasurementDispatcher.class.getSimpleName()
                    + " queue interrupted!", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            while (true) {
                MeasurementHistory measurementHistory = queue.take();
                defaultDispatch(measurementHistory);
            }
        } catch (InterruptedException e) {
            LOG.warn("Consumer of "
                    + AsynchronousMeasurementDispatcher.class.getSimpleName()
                    + " queue interrupted!", e);
        }
    }
}
