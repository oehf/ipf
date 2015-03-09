## Model Class Factories

### CustomModelClassFactory

The factory implementation org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory can be configured to map a HL7 version
to a list of package names in which the HAPI model classes are looked up. If it fails to find the requested class, the call is delegated to HAPI's default implementation.

#### Example

In this Spring context, a factory instance is provided for custom HL7 v2.5 messages:

```xml

    <!-- A custom model for HL7 v2.5 message -->
    <bean id="myModelClassFactory" class="org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory">
        <property name="customModelClasses">
            <map>
                <entry key="2.5">
                    <list>
                        <value>com.mycompany.profile1.hl7def.v25</value>
                        <value>com.mycompany.profile2.hl7def.v25</value>
                    </list>
                </entry>
                ....
            </map>
        </property>
    </bean>

    <!-- A custom HAPI context -->
    <bean id="myHapiContext" class="ca.uhn.hl7v2.DefaultHapiContext">
       <property name="modelClassFactory" ref="myModelClassfactory"/>
    </bean>


```

Assume that a custom `ADT_A01` message containing a custom `ZBE` segment shall be defined. The code typically looks like this:

```java

package com.mycompany.profile1.hl7def.v25.message;

import com.mycompany.profile1.hl7def.v25.segment.ZBE;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.ModelClassFactory;

public class ADT_A01 extends ca.uhn.hl7v2.model.v25.message.ADT_A01 {

    public ADT_T01() {
        super();
    }

    public ADT_T01(ModelClassFactory factory) {
        super(factory);
        init(factory);
    }

    /**
     * Add the ZBE segment at the end of the structure
     *
     * @param factory
     */
    private void init(ModelClassFactory factory) {
        try {
            add(ZBE.class, false, false);
        } catch (HL7Exception e) {
            // log some error
        }
    }

    public ZBE getZBE() {
        try {
            return (ZBE) get("ZBE");
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }

}

```

And here's an example for the custom ZBE segment class:

```java

package com.mycompany.profile1.hl7def.v25.segment;

import java.util.Collection;

import org.openehealth.ipf.modules.hl7.model.AbstractSegment;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * The ZBE segment is intended to be used for information that details ADT
 * movement information. Each ADT event (i.e. admission, discharge, transfer,
 * visit) has a unique identifier to allow for updates at a later point in time.
 * Furthermore, other medical information like diagnoses or documents can refer
 * to this movement using the identifier as reference.
 */
public class ZBE extends AbstractSegment {

    /**
     * @param parent
     * @param factory
     */
    public ZBE(Group parent, ModelClassFactory factory) {
        super(parent, factory);
        Message message = getMessage();
        try {
            add(EI.class, true, 0, 999, new Object[] { message }, null);
            add(TS.class, true, 1, 26, new Object[] { message }, null);
            add(TS.class, false, 1, 26, new Object[] { message }, null);
            add(ST.class, true, 1, 10, new Object[] { message }, null);
        } catch (HL7Exception he) {
            // log some error
        }
    }

    /**
     * Returns movement ID (ZBE-1).
     *
     * @param rep index of repeating field
     * @return movement ID
     */
    public EI getMovementID(int rep) {
        return getTypedField(1, rep);
    }

    /**
     * Returns movement IDs (ZBE-1).
     *
     * @return movement IDs
     */
    public EI[] getMovementID() {
        Collection<EI> result = getTypedField(1);
        return (EI[]) result.toArray(new EI[result.size()]);
    }

    /**
     * Returns movement start date (ZBE-2).
     *
     * @return movement start date (required)
     */
    public TS getStartMovementDateTime() {
        return getTypedField(2, 0);
    }

    /**
     * Returns movement end date (ZBE-3).
     *
     * @return movement end date (optional)
     */
    public TS getStartMovementEndTime() {
        return getTypedField(3, 0);
    }

    /**
     * Returns movement action (ZBE-4).
     *
     * @return movement action (required, one of INSERT, DELETE, UPDATE, REFERENCE)
     */
    public ST getAction() {
        return getTypedField(4, 0);
    }

}

```

Now if you parse a `ADT_A01` message using the configured `HapiContext`, the custom classes will be instantiated:

```java

   // ...
   Parser p = myHapiContext.getPipeParser();
   Message m = p.parse(adta01);
   assert (m instanceof com.mycompany.profile1.hl7def.v25.message.ADT_A01);

```

### GroovyCustomModelClassFactory

This is a variant of the `CustomModelClassFactory` that scans the classpath for Groovy scripts rather than for compiled
classes. `GroovyCustomModelClassFactory` is configured exactly like CustomModelClassFactory (see above).

Assume again that a custom `ADT_A01` message containing a custom `ZBE` segment is defined. While `CustomModelClassFactory` looks for a
`com.mycompany.profile1.hl7def.v25.message.ADT_A01.class` file in the classpath,  `GroovyCustomModelClassFactory` looks for
`com.mycompany.profile1.hl7def.v25.message.ADT_A01.groovy` - a plain Groovy script, which is compiled while it is loaded
at runtime.


In general, HL7 custom structures loaded with the `GroovyCustomModelClassFactory` are meant to be used in
dynamically typed environment (e.g. Groovy classes/scripts), and particularly in conjunction with the
[IPF extension mechanism].


[IPF extension mechanism]: customModelClasses.html