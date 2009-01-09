
package org.openehealth.ipf.platform.camel.lbs.process.cxf.types;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.platform.camel.lbs.process.cxf.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.platform.camel.lbs.process.cxf.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GreetMe }
     * 
     */
    public GreetMe createGreetMe() {
        return new GreetMe();
    }

    /**
     * Create an instance of {@link PostMe }
     * 
     */
    public PostMe createPostMe() {
        return new PostMe();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link PostMeResponse }
     * 
     */
    public PostMeResponse createPostMeResponse() {
        return new PostMeResponse();
    }

}
