package org.openehealth.ipf.tutorials.config.base;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Christian Ohr
 * @author Boris Stanojevic
 */
public class Base {

    private static final Logger LOG = LoggerFactory.getLogger(Base.class);

    private static String descriptorList = "base-context.xml;extender-context.xml";

    public static void main(String... args) {
        var customContextFiles = new StringBuilder();
        for (var customContext : args) {
            if (Base.class.getClassLoader().getResource(customContext) != null) {
                customContextFiles.append(customContext).append(";");
            } else {
                LOG.warn("Did not find {} on the classpath.", customContext);
            }
        }
        descriptorList = customContextFiles + descriptorList;

        try {
            LOG.info("Starting base application with descriptor list:\n{}", descriptorList);
            Main.main("-ac", descriptorList);
        } catch (Exception e) {
            LOG.error("An error occurred", e);
        } finally {
            LOG.info("Shutdown");
        }
    }
}
