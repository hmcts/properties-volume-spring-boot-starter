package uk.gov.hmcts.reform.propertiesvolume;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;

/**
 * Configuration values for properties (e.g. secrets) loaded from a keyvault flexvolume.
 *
 */
@ConfigurationProperties("spring.cloud.propertiesvolume")
public class PropertiesvolumeConfigProperties {

    protected boolean enabled = true;

    protected String name;

    private List<String> paths = new LinkedList<>();

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPaths() {
        return this.paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

}
