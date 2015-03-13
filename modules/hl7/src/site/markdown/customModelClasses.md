## Dynamic Custom Model Class Factories

Custom HL7 message structures are added by defining an `org.openehealth.ipf.modules.hl7.config.CustomModelClasses`
bean in a custom Spring application context file. This bean definition represents a mapping with a message structure
version as a mapping-key and the package name of custom model classes as a mapping-value.

These custom message structures have priority over the existing message structures.

```xml
    <bean id="customClasses"
          class="org.openehealth.ipf.modules.hl7.config.CustomModelClasses">
        <property name="modelClasses">
            <map>
                <entry key="2.5">
                    <list>
                        <value>org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25</value>
                    </list>
                </entry>
            </map>
        </property>
    </bean>

```

The custom model classes from the given package will be picked up by the `CustomModelClassFactoryConfigurer` and automatically
added to the shared `CustomModelClassFactory`.

It is also possible to use the `GroovyCustomModelClassFactory` and mix script-based with class-based model class definitions:

```xml
      <bean id="groovyCustomModelClassFactory"
            class="org.openehealth.ipf.modules.hl7.parser.GroovyCustomModelClassFactory">
          <constructor-arg ref="javaCustomModelClassFactory"/> <!-- fallback -->
      </bean>

      <bean id="javaCustomModelClassFactory"
            class="org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory" >
      </bean>

      <bean id="configurer"
            class="org.openehealth.ipf.modules.hl7.config.CustomModelClassFactoryConfigurer">
        <property name="customModelClassFactory" ref="groovyCustomModelClassFactory" />
      </bean>

```

Now the custom Spring context file can have custom model classes as both scripts and compiled classes:

```xml
      <bean id="customClasses"
        class="org.openehealth.ipf.modules.hl7.config.CustomModelClasses">
        <property name="modelClasses">
          <map>
            <entry key="2.5"> <!-- compiled classes -->
              <list>
                <value>org.openehealth.ipf.modules.hl7.parser.compiled.hl7v2.def.v25</value>
              </list>
            </entry>
            <entry key="2.4"> <!-- groovy scripts -->
              <list>
                <value>org.openehealth.ipf.modules.hl7.parser.notcompiled.hl7v2.def.v24</value>
              </list>
            </entry>
          </map>
        </property>
      </bean>

```
