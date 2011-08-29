package org.openehealth.ipf.platform.camel.core.management;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.management.DefaultManagementNamingStrategy;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.ProcessorDefinitionHelper;
import org.apache.camel.model.RouteDefinition;

public class ProcessorManagementNamingStrategy extends
		DefaultManagementNamingStrategy {

	public static final String KEY_ROUTE = "route";

	public ObjectName getObjectNameForProcessor(CamelContext context,
			Processor processor, ProcessorDefinition<?> definition)
			throws MalformedObjectNameException {

		StringBuilder buffer = new StringBuilder();
		buffer.append(domainName).append(":");
		buffer.append(KEY_CONTEXT + "=").append(getContextId(context)).append(",");
		buffer.append(KEY_TYPE + "=").append(TYPE_PROCESSOR).append(",");

		RouteDefinition route = ProcessorDefinitionHelper.getRoute(definition);

		if (route != null) {
			buffer.append(KEY_ROUTE + "=").append(route.getId()).append(",");
		}

		buffer.append(KEY_NAME + "=").append(ObjectName.quote(definition.getId()));
		return createObjectName(buffer);
	}

}
