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
package org.openehealth.ipf.commons.test.performance.handler;

import static org.apache.commons.lang.Validate.notNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openehealth.ipf.commons.test.performance.Measurement;
import org.openehealth.ipf.commons.test.performance.MeasurementHistory;
import org.openehealth.ipf.commons.test.performance.Statistics;
import org.openehealth.ipf.commons.test.performance.StatisticsManager;
import org.openehealth.ipf.commons.test.performance.Timestamp;

/**
 * @see StatisticsManager
 * @author Mitko Kolev
 */
public class PerformanceRequestHandler {
    //TODO provide methods for rendering individual statistics
    
    private StatisticsManager statisticsManager;
    public String onResetStatistics() {
        statisticsManager.resetStatistics();
        return "Statistics are now reset";
    }

    
    public String onRenderHTMLStatisticalReports() {
        StringBuffer buffer = new StringBuffer("<html><body>");
        for (Statistics s : statisticsManager.getStatistics()) {
            String report = statisticsManager.getRenderer(s).render(s);
            buffer.append(report);
        }
        buffer.append("</body></html>");
        return buffer.toString();
    }

    public void onRequestAtTime(Date requestTime, String name) {
        notNull(requestTime,
                "The server time in method onRequestAtTime can not be null");
        Measurement m = new Measurement(
                new Timestamp(0L, TimeUnit.MILLISECONDS), name);
        MeasurementHistory measurementHistory = new MeasurementHistory(
                requestTime);
        measurementHistory.add(m);
        statisticsManager.updateStatistics(measurementHistory);
    }

    public String onRequestAtTime(Date time) {
        onRequestAtTime(time, "");
        return "Measurement registered on " + time;
    }

    /**
     * @return the target <code>StatisticsManager</code>
     */
    public StatisticsManager getStatisticsManager() {
        return statisticsManager;
    }

    public void setStatisticsManager(StatisticsManager statisticsManager) {
        notNull(statisticsManager, "The statisticsManager must not be null!");

        this.statisticsManager = statisticsManager;
    }

}
