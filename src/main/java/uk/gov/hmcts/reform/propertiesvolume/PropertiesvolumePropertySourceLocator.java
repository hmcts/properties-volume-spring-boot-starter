package uk.gov.hmcts.reform.propertiesvolume;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;

import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

/**
 * Properties volume {@link PropertySourceLocator} (e.g. keyvault flexvolume mount).
 *
 */
@Order(0)
public class PropertiesvolumePropertySourceLocator implements PropertySourceLocator {

    private final transient PropertiesvolumeConfigProperties properties;

    public PropertiesvolumePropertySourceLocator(PropertiesvolumeConfigProperties properties) {
        this.properties = properties;
    }

    @Override
    public MapPropertySource locate(Environment environment) {
        return environment instanceof ConfigurableEnvironment
            ? new PropertiesvolumePropertySource(this.properties)
            : null;
    }

}
