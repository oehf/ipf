
## MLLP transport-level encryption

TLS-related aspects of MLLP-based transactions are controlled by the following URI parameters:

### Parameters

| Parameter name   | Type       | Default value | Short description                                                                    |
|:-----------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `secure`         | boolean    | false         | whether transport-level encryption shall be applied by the given endpoint 
| `clientAuth`     | one of `NONE`, `WANT`, `MUST` | NONE | whether client authentication for mutual TLS is required (MUST), requested (WANT) or not requested (NONE) on the given endpoint 
| `sslContext`     | String     | n/a           | Spring bean name of a user-defined SSL context, if any, optionally with leading '#'. If not set, a default SSL context will be used
| `sslProtocols`   | String     | system defaults | comma-separated list of SSL protocols that should be supported by the given endpoint 
| `sslCiphers`     | String     | system defaults | comma-separated list of SSL cipher suites that should be supported by the given endpoint 
