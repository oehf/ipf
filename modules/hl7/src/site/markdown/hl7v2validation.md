## HL7v2 Message Validation


### Overview

HL7v2 is a complex flat-file structure that, despite being considered a data standard, is also highly flexible.
It is often expected of an HL7 integration engine that non-standard compliant data be accepted and processed without
notification to the receiving system of non-compliance.

However, to achieve real interoperability, the HL7 standard should be constrained to reduce the degree of freedom,
e.g. how to use certain fields or whether to populate optional fields or not. This happens either based on a written
specification or in addition as machine-readble conformance profile.

In order to check whether HL7 messages actually conform to the defined constraints, message validation is essential.

**Note**: the [HAPI] library already offers support for validating HL7 messages by definition of rules that check against constraints
on type level, message level, and encoded message level. Recent versions of [HAPI] provide much easier ways of
specifying validation rules than before, so IPF 3 has removed or deprecated a lot of its custom validation rule builders.
For details refer e.g. to the [HAPI Examples].


IPF 3 continues to support the definition of custom validation rules that exploiting features of the Groovy language already
being used in other parts of IPF.


### Abstract Syntax message rule

HL7 v2 defines an Abstract Message Syntax (see HL7v2.5 specification, Chapter 2.13), that determines how groups and segments
are expected for a specific message type. Cardinality is indicated by using

 * brackets (`[...]`) for optional groups or segments, i.e. `[0..1]`
 * braces (`{...}`) for repeatable groups or segments, i.e. `[1..*]`
 * a combination of both (`{[...]}` or `[{...}]`) for optional and repeatable groups or segments, i.e. `[0..*]`

IPF provides support for checking a message instance against such an Abstract Message Syntax definition with the
`abstractSyntax` builder expression. The corresponding rule is almost a copy of the definition, with only a few differences:

 * segment names are enclosed in single quotes
 * group names are specified like function calls inside the cardinality indicators as described above
 * a choice of one segment from a group of segments is currently not supported.

Note that fields inside segments can neither be specified nor validated with the Abstract Message Syntax.

#### Example

HL7 Abstract Message Syntax Definition:

```
MSH
[  {  SFT  }  ]
                              PATIENT_RESULT
                              PATIENT
{  [  PID
      [  PD1  ]
      [  {  NTE  }  ]
      [  {  NK1  }  ]
                              VISIT
      [  PV1
         [  PV2  ]
      ]
                              VISIT
   ]
                              PATIENT
                              ORDER_OBSERVATION
   {  [  ORC  ]
      OBR
      [  {  NTE  }  ]
                              TIMING_QTY
      [{  TQ1
         [  {  TQ2  }  ]
      }]
                              TIMING_QTY
      [  CTD  ]
                              OBSERVATION
      [{  OBX
         [  {  NTE  } ]
      }]
                              OBSERVATION
      [  {  FT1  }  ]
      [  {  CTI  }  ]
                              SPECIMEN
      [{  SPM
         [  {  OBX  }  ]
      }]
                              SPECIMEN
   }
                              ORDER_OBSERVATION
}
                              PATIENT_RESULT
[  DSC  ]
```


Corresponding IPF Validation Rule:

```groovy

import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder

public class SampleRulesBuilder extends NoValidationBuilder {

    @Override
    protected void configure() {
        super.configure()

       forVersion(Version.V25)
          .message('ORU', 'R01').abstractSyntax(
              'MSH',
              [  {  'SFT'  }  ],
              {PATIENT_RESULT(
                 [PATIENT(
                    'PID',
                    [  'PD1'  ],
                    [  {  'NTE'  }  ],
                    [  {  'NK1'  }  ],
                    [VISIT(
                       'PV1',
                       [  'PV2'  ]
                   )]
                 )],
                 {ORDER_OBSERVATION(
                    [  'ORC'  ],
                     'OBR',
                     [{  'NTE'  }],
                     [{TIMING_QTY(
                        'TQ1',
                        [{  'TQ2'  }]
                     )}],

                     [  'CTD'  ],
                     [{OBSERVATION(
                        'OBX',
                        [  {  'NTE'  }  ]
                     )}],

                     [{  'FT1'  }],
                     [{  'CTI'  }],
                     [{SPECIMEN(
                        'SPM',
                        [{  'OBX'  }]
                     )}]
                  )}
               )},
               [ 'DSC' ]
            )
    }
}
```

### Closure rules

You can use rules also to program you own custom constraints on one or more trigger events.
All there is to do is to write a `checkIf` closure that returns an array of [HAPI] `ValidationException`s.
If the array is empty, validation is considered passed.

#### Example

The following example defines a few closure-based rules for primitive types, pasred and encoded
messages.

```groovy

import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder

public class ClosureRulesBuilder extends NoValidationBuilder {

    @Override
    protected void configure() {
        super.configure()

       forVersion().asOf(Version.V23)
          .primitive("ID", "IS").checkIf { String value ->
              value.size() < 200 ? passed() : failed("too long!")
          }
          .message('ADT', '*').checkIf { Message msg ->
              // validate the message
          }
    }
}

```



[HAPI]: http://hl7api.sourceforge.net
[HAPI Validation Examples]: http://hl7api.sourceforge.net/devbyexample.html
[Mapping Service]: ../ipf-commons-map/index.html