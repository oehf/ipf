package org.openehealth.ipf.platform.camel.core.management;

import static org.junit.Assert.assertNotNull;

import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.camel.CamelContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProcessorManagementNamingStrategyTest {

	private static final String CONTEXT = "context-core-management.xml";

	private static CamelContext camelContext;

	private static ClassPathXmlApplicationContext appContext;

	@BeforeClass
	public static void setUpContext() {

		appContext = new ClassPathXmlApplicationContext(CONTEXT);
		camelContext = appContext.getBean("camelContext", CamelContext.class);
	}

	@Test
	public void testProcessorManagementNamingStrategy() throws Exception {

		ObjectName on = queryForNamedObjects("org.apache.camel:context=*/camelContext,type=processors,route=namingStrategyRoute,name=\"namingStrategyProcessor\"");

		ObjectInstance oi = getMBeanServer().getObjectInstance(on);
		assertNotNull(oi);
	}

	@AfterClass
	public static void tearDownAfterClass() {

		appContext.destroy();
	}

	private ObjectName queryForNamedObjects(String query) throws Exception {

		MBeanServer mbeanServer = getMBeanServer();
		Set<ObjectName> s = mbeanServer.queryNames(new ObjectName(query), null);
		return (ObjectName) s.toArray()[0];
	}

	private MBeanServer getMBeanServer() {

		return camelContext.getManagementStrategy().getManagementAgent().getMBeanServer();
	}
}
