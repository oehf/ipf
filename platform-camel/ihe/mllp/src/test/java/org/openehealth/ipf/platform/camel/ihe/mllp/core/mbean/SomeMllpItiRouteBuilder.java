package org.openehealth.ipf.platform.camel.ihe.mllp.core.mbean;

import org.apache.camel.spring.SpringRouteBuilder;

public class SomeMllpItiRouteBuilder extends SpringRouteBuilder {
    
    @Override
    public void configure() throws Exception {
        from("some-mllp-iti://0.0.0.0:18081?audit=true&" +
                "secure=true&" +
                "sslContext=#sslContext&" +
                "sslProtocols=SSLv3,TLSv1&" +
                "sslCiphers=SSL_RSA_WITH_NULL_SHA,TLS_RSA_WITH_AES_128_CBC_SHA" +
                "&interceptors=#dummyInterceptor1,#dummyInterceptor2")
            .to("mock:result");
        
    }

}
