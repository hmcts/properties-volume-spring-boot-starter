package uk.gov.hmcts.reform.propertiesvolume;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configuration that uses keyvault flexvolume  mounts as property sources.
 *
 */
@Configuration
@ConditionalOnProperty(value = "spring.cloud.propertiesvolume.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ PropertiesvolumeConfigProperties.class })
public class BootstrapConfiguration {

    @Bean
    public PropertiesvolumePropertySourceLocator propertiesvolumePropertySourceLocator(
        PropertiesvolumeConfigProperties properties
    ) {
        return new PropertiesvolumePropertySourceLocator(properties);
    }

}
