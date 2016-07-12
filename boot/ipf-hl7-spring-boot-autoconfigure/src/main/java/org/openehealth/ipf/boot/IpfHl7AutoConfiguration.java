package org.openehealth.ipf.boot;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.conf.store.ClasspathProfileStore;
import ca.uhn.hl7v2.validation.ValidationContext;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.apache.camel.component.hl7.CustomHL7MLLPCodec;
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.openehealth.ipf.commons.ihe.core.atna.custom.CustomPixAuditor;
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory;
import org.openhealthtools.ihe.atna.auditor.PAMSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.PDQConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXConsumerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXManagerAuditor;
import org.openhealthtools.ihe.atna.auditor.PIXSourceAuditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configure a basic IPF setup, mostly configuring HL7v2 and Mapping stuff
 */
@Configuration
@EnableConfigurationProperties(IpfHl7ConfigurationProperties.class)
public class IpfHl7AutoConfiguration {

    private static final String IPF_HL7_DEFINITIONS_PREFIX = "org.openehealth.ipf.commons.ihe.hl7v2.definitions";

    @Bean
    @ConditionalOnMissingBean(HL7MLLPCodec.class)
    HL7MLLPCodec hl7codec(IpfHl7ConfigurationProperties config) {
        HL7MLLPCodec hl7MLLPCodec = new CustomHL7MLLPCodec();
        if (config.getCharset() != null) {
            hl7MLLPCodec.setCharset(config.getCharset());
        }
        hl7MLLPCodec.setConvertLFtoCR(config.getConvertLFToCR());
        return hl7MLLPCodec;
    }

    @Bean
    @ConditionalOnMissingBean(CustomModelClassFactory.class)
    public CustomModelClassFactory mllpModelClassFactory() {
        Map<String, String[]> eventMap = new HashMap<>();
        eventMap.put("2.3.1", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v231"
        });
        eventMap.put("2.5", new String[] {
                IPF_HL7_DEFINITIONS_PREFIX + "pdq.v25",
                IPF_HL7_DEFINITIONS_PREFIX + "pix.v25"
        });
        CustomModelClassFactory modelClassFactory = new CustomModelClassFactory(eventMap);
        modelClassFactory.setEventMapDirectory("org/openehealth/ipf/commons/ihe/hl7v2/");
        return modelClassFactory;
    }

    @Bean
    @ConditionalOnMissingBean(HapiContext.class)
    public HapiContext hapiContext(CustomModelClassFactory modelClassFactory) {
        HapiContext context = new DefaultHapiContext();
        context.setModelClassFactory(modelClassFactory);
        context.setValidationContext((ValidationContext) ValidationContextFactory.noValidation());
        context.setProfileStore(new ClasspathProfileStore("/org/openehealth/ipf/gazelle/validation/profile/v2"));
        return context;
    }

    // Some ATNA auditors

    @Bean
    @ConditionalOnMissingBean(PIXManagerAuditor.class)
    public PIXManagerAuditor pixManagerAuditor(AuditorModuleConfig config) {
        PIXManagerAuditor auditor = PIXManagerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PIXConsumerAuditor.class)
    public PIXConsumerAuditor pixConsumerAuditor(AuditorModuleConfig config) {
        PIXConsumerAuditor auditor = PIXConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PIXSourceAuditor.class)
    public PIXSourceAuditor pixSourceAuditor(AuditorModuleConfig config) {
        PIXSourceAuditor auditor = PIXSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PDQConsumerAuditor.class)
    public PDQConsumerAuditor pdqConsumerAuditor(AuditorModuleConfig config) {
        PDQConsumerAuditor auditor = PDQConsumerAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(PAMSourceAuditor.class)
    public PAMSourceAuditor pamSourceAuditor(AuditorModuleConfig config) {
        PAMSourceAuditor auditor = PAMSourceAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }

    @Bean
    @ConditionalOnMissingBean(CustomPixAuditor.class)
    public CustomPixAuditor customPixAuditor(AuditorModuleConfig config) {
        CustomPixAuditor auditor = CustomPixAuditor.getAuditor();
        auditor.setConfig(config);
        return auditor;
    }
}
