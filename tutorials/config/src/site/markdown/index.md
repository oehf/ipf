## IPF extension tutorial

This tutorial is targeted at developers who want to learn more about the dynamic IPF extension mechanism.
Before going through this tutorial we recommend you first to read the [IPF extension mechanism](../dynamic.html) documentation.

Please also work through the [HL7 Tutorial](../ipf-tutorials-hl7/index.html) for the basic steps of creating
IPF applications.

### Source Code

The latest sources of the this tutorial are located at [https://github.com/oehf/ipf/tree/master/tutorials/config](https://github.com/oehf/ipf/tree/master/tutorials/config).
To check out the sources, clone the ipf git repository:

```
git clone git://github.com/oehf/ipf.git
```

Then change into the `tutorials/config` directory.


### Base Application

The concept of this tutorial is to present how the IPF extension mechanism can be applied to extend the functionality of
one "empty" IPF based application (a base application) with some custom extensions. As a part of base application
you can find the following resources inside the tutorial project:

| file | description
|------|-------------
|`Base.java` | the executable Main class of the base application
|`SampleRouteBuilder.groovy` | a route definition of the base application
|`SampleModelExtensionModule.groovy` | a DSL extension of the base application
|`config/base-context.xml` | spring beans definition of the base module
|`config/extender-context.xml` | spring beans definition of the extension mechanism (configurers and post processor)

#### Base Route

The `SampleRouteBuilder.groovy` route builder defines five routes. Note that in order to be ready for customizations
the SampleRouteBuilder extends the `org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder`:

```groovy

    package org.openehealth.ipf.tutorials.config.base.route

    import org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder

    class SampleRouteBuilder extends CustomRouteBuilder {

        void configure() {

            // Receive from HTTP endpoint and forward to two direct endpoints
            from('jetty:http://0.0.0.0:8800/reverse')
                .convertBodyTo(String.class)
                .multicast().to('direct:file-save','direct:reverse-response')

            // Response: revert input string
            from('direct:reverse-response')
                .transmogrify{'reversed response: ' + it.reverse()}

            // Receive HL7 string from other HTTP endpoint, parse, validate
            from('jetty:http://0.0.0.0:8800/map')
                .convertBodyTo(String.class)
                .unmarshal().hl7()
                .verify().hl7()
                .to('direct:map')

            // Marshal mapped message and write into file
            from('direct:map')
                .marshal().hl7()
                .to('direct:file-save')
                .transmogrify{'map response ok!'}

            // Write message into destination directory
            from('direct:file-save')
                .setFileHeaderFrom('destination')
                .to('file:target/output')
        }

    }

```

The corresponding EIP diagram for the "reverse" route is shown below:

![EIP-reverse](images/eip-reverse.png)


#### Base Context

The spring beans definition of the base application is separated in two context xml files.

* `base-context.xml` - bean definitions required for the base route
* `extender-context.xml` - defines all configurers and post processor as a part of the exension mechanism

First let's take a look at `base-context.xml`. Note that you're not required to add the `baseRoute` bean to the `camelContext`.
Since it extends `CustomRouteBuilder`, the IPF extension mechanism will do that for you.

```xml

    ...
      <camel:camelContext id="camelContext" />

      <bean id="baseRoute"
            class="org.openehealth.ipf.tutorials.config.base.route.SampleRouteBuilder" />

      <bean id="bidiMappingService"
          class="org.openehealth.ipf.commons.map.BidiMappingService" />

    ...

```

`extender-context.xml` defines all configurers potentially needed to extend the base application.
There are three configurers, one for activating custom mappings, one for the custom DSL extensions and last one
for the custom route builders. The `postProcessor` processes all defined configurers:

```xml

  <!-- Extension mechanism configurers and post processor -->
  <bean id="customMappingsConfigurer"
      class="org.openehealth.ipf.commons.map.config.CustomMappingsConfigurer">
      <property name="mappingService" ref="bidiMappingService" />
  </bean>

  <bean id="customRouteBuilderConfigurer"
        class="org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilderConfigurer">
    <property name="camelContext" ref="camelContext" />
  </bean>

  <bean id="customExtensionModuleConfigurer"
        class="org.openehealth.ipf.commons.core.extend.config.DynamicExtensionConfigurer" />

  <bean id="postProcessor"
        class="org.openehealth.ipf.commons.core.config.SpringConfigurationPostProcessor">
    <property name="springConfigurers" >
      <list>
        <ref bean="customMappingsConfigurer" />
        <ref bean="customRouteBuilderConfigurer" />
        <ref bean="customExtensionModuleConfigurer" />
      </list>
    </property>
  </bean>

```

#### Assembly and Installation of the Base Application

To create a distribution package from this tutorial project run `mvn clean install` from the command line.

Unpack the resulting zip archive into an empty directory and run the startup script. This will start two routes accepting
the requests on the port 8800.

Use a [REST client](http://code.fosshub.com/WizToolsorg-RESTClient/downloads) of your choice.

* POSTing a string against `http://localhost:8800/reverse` will return the reverted string.
* POSTing a [HL7 Message](messages/message.hl7) against `http://localhost:8800/map` should return a "map response ok!" with the 200 http response code. The original message content will be additionally saved in the `target/output/default.txt` file.


### Adding extensions

Now let's can extend/customize the functionalities of both routes with usage of the IPF extension mechanism.
The reverse-route will be extended by adding an additional transmogrifier, which transforms the content in html-format and
saves it in the `target/output/transmogrified-<timestamp>.html` file. See the extension part on the EIP diagram below:

![EIP-reverse](images/eip-reverse-extended.png)

This custom logic is written in a separate route builder (also extending `CustomRouteBuilder`), by *intercepting* the
message flow of the original route without modifying its route builder class.

This `CustomInterceptor` intercepts all incoming exchanges to the `direct:file-save` endpoint and additionally processes
it by means of the `htmlTransmogrifier`. `CustomInterceptor.groovy´ can be found under `conf/config` folder of the unzipped assembly archive.

```groovy

    class CustomInterceptor extends CustomRouteBuilder {

        void configure() {
            interceptFrom('direct:file-save')
                .transmogrify('htmlTransmogrifier')
                .setDestinationHeader()
            ...
       }
    }

```

The `setDestinationHeader()` Camel DSL extension is automatically activated by the IPF extension mechanism:

```groovy

    class CustomModelExtensionModule implements DynamicExtension {

        static ProcessorDefinition setDestinationHeader(ProcessorDefinition delegate) {
            delegate.setHeader('destination') { exchange ->
                "transmogrified-${System.currentTimeMillis()}.html"
            }
        }

        @Override
        String getModuleName() {
            'CustomModelExtensionModule'
        }

        @Override
        String getModuleVersion() {
            '3.0'
        }

        @Override
        boolean isStatic() {
            false
        }
    }

```

The Spring context definition for these extensions can be found in `conf/extension-context.xml`.
Here is the snippet of the `customInterceptor`, `htmlTransmogrifier` and `customExtensionModule` bean definitions:

```xml

    ...
      <lang:groovy id="interceptorRoute"
         script-source="classpath:config/CustomInterceptor.groovy" >
        <lang:property name="intercepted" ref="baseRoute" />
      </lang:groovy>

      <lang:groovy id="customExtensionModule"
         script-source="classpath:config/CustomModelExtensionModule.groovy" />

      <lang:groovy id="htmlTransmogrifier"
         script-source="classpath:config/HtmlTransmogrifier.groovy" />
    ...
```

The `intercepted` property of the `interceptorRoute` bean tells the IPF extension mechanism to inject this
custom route builder in the existing `baseRoute`.

If you don't want to inject your custom route builder in any of existing route builders but only to add it to the existing CamelContext,
you don't need define this property at all.

Also note that these extension are implemented as Groovy scripts. They are compiled once *at runtime*.


#### Running the extensions

Run the startup script with an additional parameter:

```
startup.bat extension-context.xml
```

The `org.openehealth.ipf.tutorials.config.base.Base` class expects that first argument is the name of additional extensions
Spring context file and it tries to start this context along with `base-context.xml` and `extender-context.xml`.

After submitting a POST request you should get the reversed content with the 200 http response code, and additionally the
original message content saved in the `target/output/transmogrified-<timestamp>.html` file similar to the screenshot below:

![html-output](images/html-output.png)


### Adding more extensions

Next we will extend the mapping route by adding a *custom mapping* and a *custom transmogrifier*, which uses this mapping
definition to transform the incoming HL7v2 message.

Also, a custom exception handler is defined, which handles the exceptions
of type `ca.uhn.hl7v2.HL7Exception`. The exception handler customizes the response to the client (response 400) and saves the
exception message to the `target/hl7-error/error-<timestamp>.txt` file.

`CustomInterceptor.groovy` intercepts all incoming exchanges to the 'direct:map´ endpoint and processes it with the `genderTransmogrifier`.

```groovy

    class CustomInterceptor extends CustomRouteBuilder {

        void configure() {
            ...
            interceptFrom('direct:map')
                .transmogrify('genderTransmogrifier')
        }
    }

```

The `genderTransmogrifier` makes use of custom mapping definition (`mapGender`) to perform the transformation.

```groovy

    class GenderTransmogrifier implements Transmogrifier {

        Object zap(Object msg, Object... params) {
            msg.PID[8] = msg.PID[8].mapGender()
            msg
        }
    }

```

The custom mapping is defined in `conf/gender.map`:

```groovy

    mappings = {
        gender(
            F      : 'W',
            (ELSE) : { it }
        )
    }

```

You may have noticed when running the base application that if you tried to send some not-hl7v2 content to the
"http://localhost:8800/map" endpoint, you receive a 500 http response code with the complete exception trace as a response content.

Let's customize this behavior by adding the custom exception handler for the `ca.uhn.hl7v2.HL7Exception`
exception type, and do some custom handling when such an exception occurs.

This exception handler is again defined in a separate route builder `CustomExceptionHandler.groovy`, which also extends `CustomRouteBuilder`:

```groovy

    class CustomExceptionHandler extends CustomRouteBuilder {

        void configure() {

            onException(ca.uhn.hl7v2.HL7Exception)
              .maximumRedeliveries(0)
              .handled(true)
              .transform().exceptionMessage()
              .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
              .setHeader(Exchange.FILE_NAME) { exhg ->
                  "error-${System.currentTimeMillis()}.txt"
               }
              .to('file:target/hl7-error')
        }
    }

```

We basically extract the exception message from the exchange and return it back to the client with the 400 http response code.
Additionally the exception message content will be saved in the `/target/hl7-error/error-<timestamp>.txt` file.


The Spring context definition for these extensions can be found in `conf/extension-context.xml` as well:

```xml

  <lang:groovy id="interceptorRoute"
     script-source="classpath:config/CustomInterceptor.groovy" >
    <lang:property name="intercepted" ref="baseRoute" />
  </lang:groovy>

  <lang:groovy id="genderTransmogrifier"
     script-source="classpath:config/GenderTransmogrifier.groovy" />

  <lang:groovy id="exceptionHandler"
     script-source="classpath:config/CustomExceptionHandler.groovy" >
    <lang:property name="intercepted" ref="baseRoute" />
  </lang:groovy>

  <bean id="genderMapping"
        class="org.openehealth.ipf.commons.map.config.CustomMappings">
      <property name="mappingScript" value="classpath:config/gender.map" />
  </bean>

```

#### Running the extensions

Run the startup script with an additional parameter:

```
startup.bat extension-context.xml
```

The `org.openehealth.ipf.tutorials.config.base.Base` class expects that first argument is the name of additional extensions
Spring context file and it tries to start this context along with `base-context.xml` and `extender-context.xml`.

After submitting a POST request of the [HL7 Message](messages/message.hl7) you should get the "map response ok!" with the 200 http response code.
Message content transformed over the genderTransmogrifier will be additionally saved in the `target/output/transmogrified-<timestamp>.html` file.
Note that the transformation was successful if you have the value of "W" in the message at the position marked in the screenshot:

![html-map-output](images/html-map-output.png)

If you send in a plain (i.e. non-HL7v2) string to the same HTTP endpoint, the HL7Exception is now handled and you should
get the exception message as response content together with the 400 http response code.
The exception message should be also saved in a `target/hl7-error/error-<timestamp>.txt` file.


### Summary

In this tutorial, we went through some examples on how to enhance or modify an existing base application with additional
routes, extensions, exception handlers, interceptors, or mappings. Even doing so, no part of the base application was actually touched.

In practice, this extension mechanism can be used to write modular applications using the following pattern:

* a base module providing a potentially empty Camel context or commonly used routes, mappings etc.
* use-case specific modules contributing routes, extensions, mappings etc.

As all extensions can be provided as plain Groovy scripts that are only compiled when they are loaded by the base application,
this mechanism is also helpful for implementing *customizations* for off-the-shelf applications.

The source code for this tutorial is located in the [ipf-tutorials-config](https://github.com/oehf/ipf/tree/master/tutorials/config) module.