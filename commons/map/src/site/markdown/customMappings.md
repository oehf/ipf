## Dynamic Custom Mappings

Custom mapping scripts can be added to a global mapping service instance.
Define a `org.openehealth.ipf.commons.spring.map.config.CustomMappings` bean in a custom spring application context file which references
one or more mapping scripts that shall be picked up.

```
<bean id="customMapping1"
      class="org.openehealth.ipf.commons.spring.map.config.CustomMappings">
    <property name="mappingResources">
        <list>
            <value>classpath:config1.map</value>
            <value>classpath:config2.map</value>
        </list>
    </property>
</bean>

<bean id="customMapping2"
      class="org.openehealth.ipf.commons.spring.map.config.CustomMappings">
    <property name="mappingResource" value="classpath:config3.map" />
</bean>


```

These mapping definitions will be picked up by the `CustomMappingsConfigurer` and automatically added
to the shared MappingService.