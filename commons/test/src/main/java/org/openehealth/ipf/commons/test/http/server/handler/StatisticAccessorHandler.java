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
package org.openehealth.ipf.commons.test.http.server.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.StatisticsHandler;

/**
 * @author Martin Krasser
 */
public class StatisticAccessorHandler extends AbstractHandler {
	
	private final static String STATISTIC_CONTEXT_NAME = "/statistics";


	@Override
	public void handle(String target,
			Request baseRequest,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		baseRequest.setHandled(true);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("text/html");
		response.getWriter().println("<h1>Servers Statistic</h1>");
				
		response.getWriter().println("<table border=\"1\">");
		response.getWriter().println("<tr>");
		response.getWriter().println("<th>Application Context</th>");
		response.getWriter().println("<th>Number Requests</th>");
		response.getWriter().println("<th>Curr Active Requests</th>");
		response.getWriter().println("<th>MAX Active Requests</th>");
		response.getWriter().println("<th>Total Duration (ms)</th>");
		response.getWriter().println("<th>Average Duration (ms)</th>");
		response.getWriter().println("<th>MIN Duration (ms)</th>");
		response.getWriter().println("<th>MAX Duration (ms)</th>");
		response.getWriter().println("<th>Informal Responses (1xx)</th>");
		response.getWriter().println("<th>Success Responses (2xx)</th>");
		response.getWriter().println("<th>Success Responses (3xx)</th>");
		response.getWriter().println("<th>Success Responses (4xx)</th>");
		response.getWriter().println("<th>Success Responses (5xx)</th>");
		response.getWriter().println("</tr>");
		
		Handler[] handlersList = getServer().getHandlers();
        for (Handler thisHandler : handlersList) {
            if (thisHandler instanceof ContextHandler) {
                if (!STATISTIC_CONTEXT_NAME.equals(((ContextHandler) thisHandler).getContextPath())) {
                    response.getWriter().println("<tr>");
                    response.getWriter().println("<td>" + ((ContextHandler) thisHandler).getContextPath() + "</td>");
                    //nested list
                    StatisticsHandler contextStatisticsHandler = (StatisticsHandler) ((ContextHandler) thisHandler).getHandler();
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequests() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestsActive() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestsActiveMax() + "</td>");
                    //
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestTimeTotal() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestTimeAverage() + "</td>");
//                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestTimeMin() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getRequestTimeMax() + "</td>");
                    //
                    response.getWriter().println("<td>" + contextStatisticsHandler.getResponses1xx() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getResponses2xx() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getResponses3xx() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getResponses4xx() + "</td>");
                    response.getWriter().println("<td>" + contextStatisticsHandler.getResponses5xx() + "</td>");
                    //
                    response.getWriter().println("</tr>");
                }
            }
        }
		response.getWriter().println("</table>");
		
	}
}
