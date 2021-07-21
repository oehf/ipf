package fhir;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.boot.fhir.IpfFhirConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
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
