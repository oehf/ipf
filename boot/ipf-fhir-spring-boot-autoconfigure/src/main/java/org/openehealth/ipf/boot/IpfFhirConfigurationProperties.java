package org.openehealth.ipf.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ConfigurationProperties(prefix = "ipf.fhir")
public class IpfFhirConfigurationProperties {

    private final Servlet servlet = new Servlet();

    /**
     * Path that serves as the base URI for the FHIR services.
     */
    @NotNull
    @Pattern(regexp = "/[^?#]*", message = "Path must start with /")
    private String path = "/fhir";

    /**
     * Resource containing mappings from FHIR URIs to OIds and namespaces
     */
    @NotNull(message = "Must provide a NamingSystems resource")
    private Resource identifierNamingSystems;


    public Resource getIdentifierNamingSystems() {
        return identifierNamingSystems;
    }

    public void setIdentifierNamingSystems(Resource identifierNamingSystems) {
        this.identifierNamingSystems = identifierNamingSystems;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Servlet getServlet() {
        return this.servlet;
    }

    String getFhirMapping() {
        String path = getPath();
        return path.endsWith("/") ? path + "*" : path + "/*";
    }

    public static class Servlet {

        /**
         * Servlet init parameters to pass to Apache CXF.
         */
        private Map<String, String> init = new HashMap<>();

        /**
         * Load on startup priority of the FHIR servlet.
         */
        private int loadOnStartup = 1;

        /**
         * Name of the servlet
         */
        private String name = "FhirServlet";

        public Map<String, String> getInit() {
            return this.init;
        }

        public void setInit(Map<String, String> init) {
            this.init = init;
        }

        public int getLoadOnStartup() {
            return this.loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
