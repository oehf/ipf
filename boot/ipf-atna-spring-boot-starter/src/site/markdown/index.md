## Spring Boot ATNA support

`ipf-atna-spring-boot-starter` sets up the infrastructure for ATNA auditing.
 
The dependency on the IPF [Spring Boot] ATNA starter module is:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.boot</groupId>
        <artifactId>ipf-atna-spring-boot-starter</artifactId>
    </dependency>
```

Note: all IHE Spring boot starter modules depend on this starter module, so you normally do not have to
explicitly depend on `ipf-atna-spring-boot-starter`.

`ipf-atna-spring-boot-starter` auto-configures:

* `org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig` and `org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext` beans
* the TLS config
* a basic auditor bean and listeners that write ATNA audit events upon application startup and shutdown

`ipf-atna-spring-boot-starter` provides the following application properties:

| Property (`ipf.atna.`)       | Default               | Description                                         |
|------------------------------|-----------------------|-----------------------------------------------------|
| `auditor.enabled`            | false                 | Whether auditing is enabled
| `security-domain-name`       | bootSecurityDomain    | ATNA domain name for the application
| `repository-host`            |                       | Host of the ATNA repository to send the events to
| `repository-port`            |                       | Port of the ATNA repository to send the events to
| `audit-source-id`            | `${spring.application.name}` | Source ID for ATNA events
| `audit-enterprise-site-id`   |                       | Enterprise Site ID for ATNA events
| `audit-queue-class`          | `org.openhealthtools.ihe.atna.auditor.queue.SynchronousAuditQueue` | Queue implementation for auditing
| `audit-sender-class`         | `org.openhealthtools.ihe.atna.auditor.sender.UDPSyslogSenderImpl` | ATNA sender implementation


[Spring Boot]: https://projects.spring.io/spring-boot/
