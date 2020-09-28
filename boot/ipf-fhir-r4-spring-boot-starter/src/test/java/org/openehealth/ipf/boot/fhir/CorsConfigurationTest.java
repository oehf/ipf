package org.openehealth.ipf.boot.fhir;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openehealth.ipf.commons.ihe.fhir.IpfFhirServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {TestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=test",
                "ipf.fhir.cors.allowed-headers=gablorg",
                "ipf.atna.audit-enabled=false"
        })
public class CorsConfigurationTest {

    @Autowired
    private IpfFhirConfigurationProperties config;

    @Test
    public void testCorsConfiguration() {
        assertThat(config, notNullValue()); // explicit config
        assertThat(config.getCors().getAllowedHeaders(), hasItem("gablorg"));
        assertThat(config.getCors().getExposedHeaders(), hasItem("ETag"));
    }
}
