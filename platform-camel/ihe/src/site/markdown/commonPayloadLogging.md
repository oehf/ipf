
## File-based Logging of Message Payload

IPF provides the possibility to store payload of incoming or outgoing messages of eHealth transactions into files.

**Warning**:
This feature is intended for debug purposes and may lead to performance problems when used in highly loaded production environment without proper configuration.
Making saved messages available for non-authorized persons may cause data privacy risks.

This feature is implemented in form of MLLP and CXF interceptors, depending on the protocol. 

The configuration of these interceptors is uniform. Their constructors require a single String parameter that corresponds to the name pattern 
of the log files to be created. This pattern can contain absolute or relative path and is defined using the Spring Expression Language (SpEL), 
with square brackets as expressions placeholders. 

The following specific values can be used in the expressions:

| Value Name          | Value Type | Description
|:--------------------|:-----------|:----------------------------------------------------------------
| `processId`         | String     | Identifier of the current OS process, consisting from the process number and the host name, divided by a dash, e.g. "31415-ipfSuperMainframe"
| `sequenceId`        | String     | Internally generated sequence number
| `date('format')`    | String     | Current date and time, formatted using [SimpleDateFormat](http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html) according to the given specification 

The file name pattern configured for an interceptor can be changed at any time using the corresponding setter method. 
When the file denoted by the pattern does already exist, the new message payload will be appended at the end of the existing content.

The actual logging is delegated to subclasses of `org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase`. This base class provide
an additional set of properties that can be set via the logging interceptor instances:


| Property name       | Type            | Default Value   | Description                                                                    |
|:--------------------|:----------------|:----------------|:-------------------------------------------------------------------------------|
| `globallyEnabled`   | boolean (static)| true            | switch file-based logging on and off in the whole application. 
| `locallyEnabled`    | boolean         | true            | whether this particular interceptor instance is enabled or disabled 
| `errorCountLimit`   | int             | -1              | Maximal allowed count of messages this interceptor instance has sequentially failed to handle. When this value has been reached, the interceptor gets deactivated until `resetErrorCount()` is called. Negative values mean "no limit" 

Note that even the globally deactivated logging may lead to performance penalties because the corresponding interceptors remain still deployed and perform some non-avoidable activities on each message.