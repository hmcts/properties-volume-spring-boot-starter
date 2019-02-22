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

    private boolean enabled = true;

    // Use parent dir as namespace generating a key like 'parentdir.file'
    private boolean prefixed = true;

    private String name;

    private List<String> paths = new LinkedList<>();

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPrefixed() {
        return prefixed;
    }

    public void setPrefixed(boolean prefixed) {
        this.prefixed = prefixed;
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
