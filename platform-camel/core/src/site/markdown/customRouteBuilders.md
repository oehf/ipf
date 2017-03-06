## Custom Route Builders / Interceptors / Exception Handlers

With the custom route builders it is possible to:

* extend the base application functionality by adding additional route builders to the existing camel context
* extend the existing route builder functionality by injecting additional [interceptors](https://camel.apache.org/intercept.html) to this route builder
* extend the existing route builder exception handling by injecting additional [exception handlers](https://camel.apache.org/exception-clause.html) to this route builder

The abstract class [`org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder`](../apidocs/org/openehealth/ipf/platform/camel/core/config/CustomRouteBuilder.html)  must be extended by all your
custom route builders (also the ones in the base application). If the `intercepted` property is set, it is assumed
that the custom route builder is an interceptor or an exception handler and the
[`CustomRouteBuilderConfigurer`](../apidocs/org/openehealth/ipf/platform/camel/core/config/CustomRouteBuilderConfigurer.html) will try
to inject it to the referenced intercepted route builder.

Otherwise if the `intercepted` property is *not* set, the `CustomRouteBuilderConfigurer` will inject this custom route builder
to the existing camel context.

The `CustomRouteBuilder` beans will be recognized by the `CustomRouteBuilderConfigurer` and added in desired order to the camel context respectively.


### Example

Here is a fragment of a base custom route builder class:

```groovy

    class BaseRoute extends CustomRouteBuilder {
        void configure() {
            ...
            from('seda:input')...
            ...
        }
    }

```

And the corresponding Spring application context shown below. Note that the custom route builder must not be explicitly associated to the Camel context.

 ```xml

  <camel:camelContext id="camelContext" />

  <!-- Required to pick up the BaseRoute and add it to the Camel Context -->

  <!-- Picking up custom route builders -->
  <bean id="customRouteBuilderConfigurer"
        class="org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer">
    <property name="camelContext" ref="camelContext" />
  </bean>

  <bean id="postProcessor"
        class="org.openehealth.ipf.commons.core.config.SpringConfigurationPostProcessor">
      <property name="springConfigurers" >
        <list>
          <ref bean="customRouteBuilderConfigurer" />
          <!-- potentially more configurers -->
        </list>
      </property>
  </bean>


  <!-- Usually this is defined in a different Spring context file -->
  <bean id="baseRoute" class="org.openehealth.ipf.tutorial.config.route.BaseRoute" />

 ```

A custom [interceptor](https://camel.apache.org/intercept.html) is defined in separate `CustomRouteBuilder` and extends
the functionality of the `BaseRoute` by intercepting the inputs from the `'seda:input'` endpoint:

```groovy

    class CustomInterceptingRoute extends CustomRouteBuilder{
        void configure() {

            onException(MyCustomTransmogrifierException)
               .handled(true)
               // do some more stuff to handle this exception

            interceptFrom('seda:input').transmogrify('customTransmogrifier')
        }
    }

```

The Spring beans definition is shown below, this time as Groovy script that is compiled as load time.
Note the `intercepted` property is set to the custom route builder to be intercept. If started in same application context,
this interceptor will intercept all incoming exchanges to the `"seda:input"` endpoint in the base route and translate
it with the logic implemented in `customTransmogrifier` bean (not shown in this sample). Also, the custom exception handler
is added to the `intercepted` route.

```xml

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:lang="http://www.springframework.org/schema/lang"
           xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
    http://www.springframework.org/schema/lang
    >

      <lang:groovy id="interceptorRoute"
          script-source="classpath:config/CustomInterceptingRoute.groovy" >
          <lang:property name="intercepted" ref="baseRoute" />
      </lang:groovy>

      <lang:groovy id="customTransmogrifier"
          script-source="classpath:config/CustomTransmogrifier.groovy" />

    </beans>

```

