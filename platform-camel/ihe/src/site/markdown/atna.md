
## ATNA Auditing in IPF eHealth Components

ATNA auditing functionality is fully integrated into the corresponding IPF IHE components and controlled by the `audit` URI parameter.
If the parameter is set to `true` (which is also the default), ATNA auditing is performed on the corresponding endpoint.

### Auditor Configuration

In order to let the endpoint know *how* auditing should be performed, auditor beans have to be defined.
Each of the currently supported IHE actor types has a corresponding singleton auditor, i.e. the following set is available:


* `XDSRegistryAuditor`
* `XDSRepositoryAuditor`
* `XDSSourceAuditor`
* `XDSConsumerAuditor`
* `PIXManagerAuditor` (serves the Patient Demographic Supplier actor as well)
* `PIXSourceAuditor`
* `PIXConsumerAuditor`
* `PDQConsumerAuditor`
* `Hl7v3Auditor` (for all actors of PIXv3, PDQv3, XCPD and PCC (QED) profiles)
* `CustomPixAuditor` (for ITI-64)
* `CustomXdsAuditor` (for ITI-51,ITI-57,ITI-61,ITI-62,ITI-63,RAD-69,RAD-75)

Auditors can be configured both individually and as group. 
As a minimum, the audit repository host and port must be set. It is furthermore recommended to set the audit source ID and
audit enterprise site ID. See [Section 5.4 of RFC 3881](http://tools.ietf.org/html/rfc3881#section-5.4) for details.

#### Individual Configuration

Configuring a particular auditor (for example, the XDS Registry-related one) can be done in plain Java:

```java
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.openhealthtools.ihe.atna.auditor.XDSRegistryAuditor;
...
 
AuditorModuleConfig config = new AuditorModuleConfig();
config.setAuditRepositoryHost("my.syslog.server");
config.setAuditRepositoryPort(514);
config.setAuditSourceId(...);
config.setAuditEnterpriseSiteId(...);
XDSRegistryAuditor.getAuditor().setConfig(config);
```

To perform the same configuration using Spring, the corresponding bean definitions are:

```xml
<bean id="config"
      class="org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig">
   <property name="auditRepositoryHost" value="my.syslog.server" />
   <property name="auditRepositoryPort" value="514" />
   <property name="auditSourceId" value="..." />
   <property name="auditEnterpriseSiteId" value="..." />
</bean>
 
<bean id="registryAuditor"
      class="org.openhealthtools.ihe.atna.auditor.XDSRegistryAuditor"
      factory-method="getAuditor">
   <property name="config" ref="config" />
</bean>
```

#### Group Configuration

Configuring a group of auditors can be done in plain Java:

```java
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
...
 
AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("my.syslog.server");
AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);
```

To perform the same configuration using Spring, the corresponding bean definitions are:

```xml
<bean id="iheAuditorContext"
      class="org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext"
      factory-method="getContext" />
</bean>
 
<bean id="iheAuditorConfig"
      factory-bean="iheAuditorContext"
      factory-method="getConfig">
   <property name="auditRepositoryHost" value="my.syslog.server" />
   <property name="auditRepositoryPort" value="514" />
</bean>

```

### Configure reliable audit transport

The default transport protocol ATNA auditing is based on unreliable UDP communication ([Syslog protocol](http://tools.ietf.org/html/rfc5424)), cf. IHE IT TF, Vol. 2, Section 3.20.6.1. This choice is also explained in the [ATNA FAQ](http://wiki.ihe.net/index.php?title=ATNA_Profile_FAQ#Why_does_ATNA_use_UDP_SYSLOG.3F).

In order to change this setting, the TLS implementation of `org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender` must be provided and registered as follows:

```java
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.openhealthtools.ihe.atna.auditor.sender.TLSSyslogSenderImpl;
...

// the following is equivalent to AuditorModuleContext.getContext().getConfig().setAuditRepositoryTransport("TLS")
AuditMessageSender sender = new TLSSyslogSenderImpl(); 
AuditorModuleContext.getContext().setSender(sender);
```

This will affect all auditors, because the auditor module context is a singleton.

### Configure custom audit event queueing

The delivery queue that is used as channel for the audit sender can be customized in a similar way, i.e. by using a different implementation of the interface `org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue`. There are implementations for synchronous and asynchronous delivery

```java
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue;
...

AuditMessageQueue queue = new ThreadedAuditQueue();
AuditorModuleContext.getContext().setQueue(queue);
```

Again, this will affect all auditors, because the auditor module context is a singleton.


### Routing audit messages to Camel endpoints

This section is basically built upon be configuration possibilities described in the previous section. Instead of sending audit messages to a syslog server, 
they can also be sent to Camel endpoints. For that purpose, add the following dependency to the `pom.xml`:

```xml
<dependencies>
   <dependency>
      <groupId>org.openehealth.ipf.platform-camel</groupId>
      <artifactId>ipf-platform-camel-ihe-atna</artifactId>
      <version>${ipf-version}</version>
   </dependency>
</dependencies>
```

Now an instance of `CamelEndpointSender` can be configured in the application context:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:camel="http://camel.apache.org/schema/spring"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://camel.apache.org/schema/spring
>
 
<camel:camelContext id="camelContext">
   <camel:routeBuilder ref="..."/>
</camel:camelContext>

<!-- Set dummy host and port --> 
<bean id="auditModuleConfig" factory-method="getConfig" factory-bean="auditModuleContext">
   <property name="auditRepositoryHost" value="0.0.0.0" />
   <property name="auditRepositoryPort" value="0" />
</bean>
 
<bean id="auditModuleContext" factory-method="getContext"
      class="org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext">
   <property name="sender" ref="camelEndpointSender" />
</bean>
 
<bean id="camelEndpointSender"
      class="org.openehealth.ipf.platform.camel.ihe.atna.util.CamelEndpointSender">
   <!-- autowired if not explicitly configured -->
   <property name="camelContext" ref="camelContext" />
   <!-- endpoint registered in the Camel context -->
   <property name="endpointUri" value="direct:input" />
</bean>
...
</beans>
```

The `CamelEndpointSender` instance is configured to send audit messages to the `direct:input` endpoint that is registered in the Camel context. 
The `AuditorModuleContext` singleton is configured to use the custom audit message sender. Although ignored by the sender, OpenHealthTools require 
us to set the audit repository host and port. Hence, they can be set to arbitrary values, 0.0.0.0 and 0, respectively. 
These values are overwritten by the message sender. The sender derives them from the Camel endpoint URI.

The audit message is sent as In-Only exchange to the endpoint where the message body contains a string representation of the audit XML message. 
Additionally the following Camel message headers are populated:

* `org.openehealth.ipf.platform.camel.ihe.atna.datetime`: date and time when the audit event was generated
* `org.openehealth.ipf.platform.camel.ihe.atna.destination.address`: the audit repository IP address derived from the configured endpoint URI
* `org.openehealth.ipf.platform.camel.ihe.atna.destination.port`: the audit repository port derived from the configured endpoint URI


### XUA Support

ATNA audit routines of Web Service-based eHealth components expect to find a prepared XUA user name (see the IHE ITI-40 transaction) in the 
CXF request message context property defined in `org.openehealth.ipf.commons.ihe.ws.cxf.audit.AbstractAuditInterceptor#DATASET_CONTEXT_KEY`, 
which is supposed to be set by project-specific mechanisms (not defined in IPF).

When no such property exists, IPF tries to determine the XUA user name by means of processing the SAML2 assertion contained in the WS-Security SOAP header of the request message.

Note that when the XUA user name cannot be determined IPF does neither throw any exceptions nor performs any validation of SAML2 assertions. 
In other words, the support for XUA is restricted to filling the corresponding field in ATNA audit records.


### Complete ATNA configuration example

The following example shows a more sophisticated configuration of ATNA auditing — in particular, there is the possibility to send audit records over TLS.
First of all, all ATNA-related parameters are concentrated in a separate properties file:

```
# Properties for ATNA Auditing
 
# Sender class -- preconfigured are UDP and TCP-over-SSL
#auditor.class           = org.openhealthtools.ihe.atna.auditor.sender.UDPSyslogSenderImpl
auditor.class           = org.openhealthtools.ihe.atna.auditor.sender.TLSSyslogSenderImpl
 
# Sender queue -- should be threaded for TCP transport, for UDP does not matter
#audit.queue.class = org.openhealthtools.ihe.atna.auditor.queue.SynchronousAuditQueue
audit.queue.class = org.openhealthtools.ihe.atna.auditor.queue.ThreadedAuditQueue
 
# address of the ATNA Audit repository
audit.repository.host   = 10.205.115.42
audit.repository.port   = 3201
 
# these parameters will be evaluated only when
# the TCP-over-TLS auditor is selected
javax.net.ssl.keyStore           = c:/PIX_X_REF_MGR_ICW.jks
javax.net.ssl.keyStorePassword   = abcd
javax.net.ssl.trustStore         = c:/PIX_X_REF_MGR_ICW.jks
javax.net.ssl.trustStorePassword = abcd
https.ciphersuites               = SSL_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA
https.protocols                  = TLSv1
```

These parameters are used to configure the beans defined in the separate Spring context file:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
">
 
    <!-- ============================================ -->
    <!--       Configuration of ATNA auditing         -->
    <!-- ============================================ -->
 
    <bean id="atnaAuditProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
        <property name="location" value="classpath:/atna-audit.properties"/>
    </bean>
 
    <bean id="atnaAuditAuditorModuleContext"
          class="org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext"
          factory-method="getContext">
 
        <property name="sender">
            <bean class="${auditor.class}" />
        </property>
        <property name="queue">
            <bean class="${audit.queue.class}" />
        </property>
    </bean>
 
    <bean id="atnaAuditAuditorConfig"
          factory-bean="atnaAuditAuditorModuleContext"
          factory-method="getConfig">
        <property name="auditRepositoryHost" value="${audit.repository.host}" />
        <property name="auditRepositoryPort" value="${audit.repository.port}" />
    </bean>
 
    <bean id="atnaAuditSecurityDomainName" class="java.lang.String">
        <constructor-arg value="ipf-atna-tls" />
    </bean>
 
    <bean id="atnaAuditSecurityDomain"
          class="org.openhealthtools.ihe.atna.nodeauth.SecurityDomain">
        <constructor-arg index="0" ref="atnaAuditSecurityDomainName" />
        <constructor-arg index="1" ref="atnaAuditProperties" />
    </bean>
 
    <bean id="atnaAuditNodeAuthModuleContext"
          class="org.openhealthtools.ihe.atna.nodeauth.context.NodeAuthModuleContext"
          factory-method="getContext" />
 
    <bean id="atnaAuditSecurityDomainManager"
          class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
        <property name="targetObject" ref="atnaAuditNodeAuthModuleContext" />
        <property name="targetMethod" value="getSecurityDomainManager" />
    </bean>
 
    <bean class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
        <property name="targetObject" ref="atnaAuditSecurityDomainManager" />
        <property name="targetMethod" value="registerSecurityDomain" />
        <property name="arguments">
            <list>
                <ref local="atnaAuditSecurityDomain" />
            </list>
        </property>
    </bean>
 
    <bean class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
        <property name="targetObject" ref="atnaAuditSecurityDomainManager" />
        <property name="targetMethod" value="registerURItoSecurityDomain" />
        <property name="arguments">
            <list>
                <bean class="java.net.URI">
                    <constructor-arg value="atna://${audit.repository.host}:${audit.repository.port}" />
                </bean>
                <ref local="atnaAuditSecurityDomainName" />
            </list>
        </property>
    </bean>
 
</beans>
```

Both the properties file and the Spring descriptor must be referenced by the main Spring context:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:ctx="http://www.springframework.org/schema/context"
       xmlns:camel="http://camel.apache.org/schema/spring"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd
http://camel.apache.org/schema/spring
http://camel.apache.org/schema/spring/camel-spring.xsd
">
 
    <ctx:property-placeholder location="classpath:/atna-audit.properties" />
 
    <import resource="classpath:/atna-audit-context.xml" />
 
    .....
</beans>
```