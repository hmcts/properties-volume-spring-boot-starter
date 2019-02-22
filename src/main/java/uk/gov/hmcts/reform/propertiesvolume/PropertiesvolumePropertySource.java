package uk.gov.hmcts.reform.propertiesvolume;

import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Note: this class reuses code from spring-cloud-kubernetes
 * (https://github.com/spring-cloud/spring-cloud-kubernetes).
 */
public class PropertiesvolumePropertySource extends MapPropertySource {

    private static final String PREFIX = "propertiesvolume";
    private static final String PROPERTY_SOURCE_NAME_SEPARATOR = ".";
    private static final int MIN_NAMES_FOR_PREFIX = 2;

    public PropertiesvolumePropertySource(PropertiesvolumeConfigProperties config) {
        this(getName(config), getData(config));
    }

    @SuppressWarnings("unchecked")
    protected PropertiesvolumePropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }

    private static String getName(PropertiesvolumeConfigProperties config) {
        return new StringBuilder().append(PREFIX)
            .append(PROPERTY_SOURCE_NAME_SEPARATOR)
            .append(config.getName())
            .toString();
    }

    private static Map<String, Object> getData(PropertiesvolumeConfigProperties config) {
        Map<String, Object> result = new ConcurrentHashMap<>();
        putPath(result, config.getPaths(), config.isPrefixed());
        return result;
    }

    protected static void putPath(Map<String, ? super String> result, List<String> paths, boolean prefixed) {
        paths.stream()
            .map(Paths::get)
            .filter(Files::exists)
            .forEach(p -> putAll(p, result, prefixed));
    }

    private static void putAll(Path path, Map<String, ? super String> result, boolean prefixed) {
        try {
            Files.walk(path)
                .filter(Files::isRegularFile)
                .forEach(p -> readFile(p, result, prefixed));
        } catch (IOException e) {
            throw new PropertiesvolumeException("Error walking propertiesvolume files", e);
        }
    }

    private static void readFile(Path path, Map<String, ? super String> result, boolean prefixed) {
        String fileName;
        if (prefixed) {
            if (path.getNameCount() < MIN_NAMES_FOR_PREFIX) {
                throw new PropertiesvolumeException("UseDirPrefix requires at least 1 parent directory");
            }
            int count = path.getNameCount();
            fileName = String.format("%s%s%s",
                path.subpath(count - 2, count - 1).toString(),
                PROPERTY_SOURCE_NAME_SEPARATOR,
                path.getFileName().toString()
            );
        } else {
            fileName = path.getFileName().toString();
        }
        try {
            result.put(
                fileName,
                new String(Files.readAllBytes(path)).trim()
            );
        } catch (IOException e) {
            throw new PropertiesvolumeException("Propertiesvolume file " + path.getFileName() + " cannot be read", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {name='" + getName() + "'}";
    }
    
}
