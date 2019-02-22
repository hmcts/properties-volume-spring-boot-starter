package uk.gov.hmcts.reform.propertiesvolume;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Volumes {

    private Volumes() {
    }

    public static void cleanUp(String basePath) throws IOException {
        if (Paths.get(basePath).toFile().exists()) {
            Files.walk(Paths.get(basePath))
                .map(Path::toFile)
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .forEach(File::delete);
        }
    }

}
