## Spring Boot ATNA support

`ipf-atna-spring-boot-starter` sets up the infrastructure for ATNA auditing.
 
The dependency on the IPF [Spring Boot] ATNA starter module is:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.boot</groupId>
        <artifactId>ipf-atna-spring-boot-starter</artifactId>
    </dependency>
```

Note: all IHE-related Spring boot starter modules depend on this starter module, so you normally do not have to
explicitly depend on `ipf-atna-spring-boot-starter`.

`ipf-atna-spring-boot-starter` auto-configures:

* `org.openehealth.ipf.commons.audit.AuditContext` bean
* a basic listeners that write ATNA audit events upon application startup and shutdown, and authentication events

`ipf-atna-spring-boot-starter` provides the following application properties:

| Property (`ipf.atna.`)         | Default               | Description                                         |
|--------------------------------|-----------------------|-----------------------------------------------------|
| `audit-enabled`                | false                 | Whether auditing is enabled
| `audit-repository-host`        | localhost             | Host of the ATNA repository to send the events to
| `audit-repository-port`        | 514                   | Port of the ATNA repository to send the events to
| `audit-repository-transport`   | UDP                   | Wire transport format (UDP, TLS)
| `audit-source-id`              | `${spring.application.name}` | Source ID for ATNA events
| `audit-enterprise-site-id`     |                       | Enterprise Site ID for ATNA events
| `audit-source-type`            | 4 (ApplicationServerProcess) | Type of Audit Source
| `audit-queue-class`            | `org.openehealth.ipf.commons.audit.queue.SynchronousAuditMessageQueue` | Queue implementation for auditing
| `audit-sender-class`           | as indidcated by `audit-repository-transport` | ATNA sender implementation
| `audit-exception-handler-class`| `org.openehealth.ipf.commons.audit.handler.LoggingAuditExceptionHandler`| Exception handler impleemntation
| `include-participants-from-response`| false            | Whether to include (patient) participants from responses as well


[Spring Boot]: https://projects.spring.io/spring-boot/
