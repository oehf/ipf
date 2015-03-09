## Custom DSL extensions definition

Custom DSL extensions implement the `org.openehealth.ipf.commons.core.extend.config.DynamicExtension` marker interface
in order to be picked up and activated from its corresponding extension configurer.

The following Groovy script `CustomModelExtension.groovy` defines a simple extension to the Camel DSL:

```groovy

package org.openehealth.ipf.tutorial.config.extend

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.core.extend.config.DynamicExtension

class CustomModelExtensionModule implements DynamicExtension {

    static RouteBuilder direct(RouteBuilder delegate, String endpointName) {
        delegate.from('direct:' + endpointName)
    }

}

```

In addition a bean needs to be defined in thecustom spring context file, running in the same Spring application context as the base application:

```xml

...
<lang:groovy id="customExtension"
     script-source="classpath:org/openehealth/ipf/tutorial/config/extend/CustomModelExtension.groovy" />
...

```

Note that in this case the Groovy meta class extension is loaded *after* the complete Spring application context has been
initialized.
Thus, the extensions are not available during that initialization process, so static Camel route builders - i.e.
those that are directly configured in the `CamelContext` bean - are not able to depend on them. Instead, these Camel route builders
must be initialized as [Custom Route Builders] as well.


[Custom Route Builders]: ../ipf-platform-camel-core/customRouteBuilders.html
