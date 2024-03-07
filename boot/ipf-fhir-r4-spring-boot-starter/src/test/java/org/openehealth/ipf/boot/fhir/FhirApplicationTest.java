package org.openehealth.ipf.boot.fhir;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.boot.IpfAutoConfiguration;
import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.map.MappingService;
import org.openehealth.ipf.commons.spring.map.config.CustomMappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {TestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.application.name=mappingstest",
                "ipf.fhir.cors.allowed-headers=gablorg",
                "ipf.fhir.mappings=classpath:customer-fhir-hl7v2-translation.map",
                "ipf.atna.audit-enabled=false"
        })
public class FhirApplicationTest {

    @Autowired
    private IpfFhirConfigurationProperties properties;

    @Autowired
    @Qualifier("translationFhirHl7v2Mappings")
    private CustomMappings translationFhirHl7v2Mappings;

    @Test
    public void testCorsConfiguration() {
        assertThat(properties, notNullValue()); // explicit config
        assertThat(properties.getCors().getAllowedHeaders(), hasItem("gablorg"));
        assertThat(properties.getCors().getExposedHeaders(), hasItem("ETag"));
    }

    @Test
    public void testCustomizeFhirMapping() {
        assertThat(properties.getMappings(), not(empty()));
        assertThat(translationFhirHl7v2Mappings.getMappingResources(), hasSize(2));

        var mappingService = ContextFacade.getBean(MappingService.class);
        assertThat(mappingService.get("hl7v2fhir-patient-genderIdentity", "CUSTOM"), equalTo("non-binary"));
    }
}
