
## HL7v2 Codec

Some parameters defined in [camel-mina2] have constant values in MLLP-based IPF IHE components. 
This means that these parameters are actually not configurable by the user any more; values provided via endpoint URIs will be silently ignored. 
These parameters are:

### MINA Parameters

| Parameter name        | Type       | Constant value | 
|:----------------------|:-----------|:---------------|
| `sync`                | boolean    | true           |
| `lazySessionCreation` | boolean    | true           |
| `transferExchange`    | boolean    | false          | 
| `encoding`            | String     | corresponds to the charset name configured for the HL7 codec factory, as described below |

All other URI parameters defined in [camel-mina2] remain fully functional and configurable by the user.

### HL7 Codec Parameters

[camel-mina2] defines a parameter named `codec`, which is expected to contain the name of a bean that corresponds to an codec factory, that translates the
network stream into a suitable application protocol and vice versa. 
[camel-hl7] comes with an implementation of an HL7 codec factory. MLLP-based IPF IHE components set `#hl7codec` as a default value for this parameter. 
The corresponding bean must always be defined:

```xml
    <bean id="hl7codec" class="org.apache.camel.component.hl7.HL7MLLPCodec">
        <property name="charset" value="iso-8859-1"/>
    </bean>
```

In case you need to set an `HapiContext` on the codec, you need to use the IPF implementation of the HL7 codec factory:

```xml
    <bean id="hl7codec" class="org.apache.camel.component.hl7.CustomHL7MLLPCodec">
        <property name="charset" value="iso-8859-1"/>
        <property name="hapiContext" ref="hapiContext"/>
    </bean>
```

The character set name set up for the HL7 codec factory will be automatically

* propagated to the Camel component (see parameter encoding in the table above)
* stored in the Exchange.CHARSET_NAME property of each Camel exchange
* used in all data transformation activities


[camel-mina2]: http://camel.apache.org/mina2.html
[camel-hl7]: http://camel.apache.org/hl7.html