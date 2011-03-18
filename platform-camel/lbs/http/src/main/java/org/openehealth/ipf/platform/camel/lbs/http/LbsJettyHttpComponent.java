package org.openehealth.ipf.platform.camel.lbs.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.Endpoint;
import org.apache.camel.component.http.CamelServlet;
import org.apache.camel.component.http.DefaultHttpBinding;
import org.apache.camel.component.http.HttpMessage;
import org.apache.camel.component.jetty.JettyHttpComponent;
import org.apache.camel.component.jetty.JettyHttpEndpoint;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class LbsJettyHttpComponent extends JettyHttpComponent {
    @Override
    protected CamelServlet createServletForConnector(Server server, Connector connector, List<Handler> handlers) throws Exception {
        ServletContextHandler context = new ServletContextHandler(server, "/", false, false);
        context.setConnectorNames(new String[] {connector.getName()});

        if (handlers != null && !handlers.isEmpty()) {
            for (Handler handler : handlers) {
                if (handler instanceof HandlerWrapper) {
                    ((HandlerWrapper) handler).setHandler(server.getHandler());
                    server.setHandler(handler);
                } else {
                    HandlerCollection handlerCollection = new HandlerCollection();
                    handlerCollection.addHandler(server.getHandler());
                    handlerCollection.addHandler(handler);
                    server.setHandler(handlerCollection);
                }
            }
        }

        CamelServlet camelServlet = new CamelServlet();
        ServletHolder holder = new ServletHolder();
        holder.setServlet(camelServlet);
        context.addServlet(holder, "/*");

        return camelServlet;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        JettyHttpEndpoint endpoint = (JettyHttpEndpoint) super.createEndpoint(uri, remaining, parameters);
        endpoint.setBinding(new LbsHttpBinding());
		endpoint.setEnableMultipartFilter(Boolean.FALSE);
        return endpoint;
    }

    private static class LbsHttpBinding extends DefaultHttpBinding {
        @Override
        public Object parseBody(HttpMessage httpMessage) throws IOException {
            HttpServletRequest request = httpMessage.getRequest();
            if ("GET".equals(request.getMethod())) {
                return null;
            }
            return request.getInputStream();
        }
    }
}
