package org.openehealth.ipf.boot;

import lombok.Getter;
import lombok.Setter;
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

    @Getter
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
         * Servlet init parameters to pass to the FHIR Servlet.
         */
        @Getter @Setter
        private Map<String, String> init = new HashMap<>();

        /**
         * Load on startup priority of the FHIR servlet.
         */
        @Getter @Setter
        private int loadOnStartup = 1;

        /**
         * Name of the servlet
         */
        @Getter @Setter
        private String name = "FhirServlet";

        /**
         * Number of paging results that can be stored at a given point in time
         */
        @Getter @Setter
        private int pagingProviderSize = 50;

        /**
         * Default number of result entries to be returned if no _count parameter is specified in a search
         */
        @Getter @Setter
        private int defaultPageSize = 50;

        /**
         * Maximum number of result entries to be returned even if the _count parameter of a search demands for more
         */
        @Getter @Setter
        private int maxPageSize = 100;

        /**
         * Enable server-side request logging
         */
        @Getter @Setter
        private boolean logging = false;

        /**
         * Enable pretty-printing responses
         */
        @Getter @Setter
        private boolean prettyPrint = true;

        /**
         * Enable color-coding responses queried from a Web Browser
         */
        @Getter @Setter
        private boolean responseHighlighting = true;
    }
}
