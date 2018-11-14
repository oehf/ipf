
## Connection-related FHIR endpoint parameters

A couple of FHIR endpoints parameters are available to set client-side connection properties.
If you set a custom FHIR context for this endpoint, these parameters will have no effect. 

#### Parameters

| Parameter name            | Type       | Default value | Short description                                                                    |
|:--------------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `connectionTimeout`       | Integer    | 10000         | initial connection timeout in milliseconds
| `timeout`                 | Integer    | 10000         | socket timeout for read/write operations in milliseconds
| `disableServerValidation` | Boolean    | false         | skip checking the server's capability statement for compatibility