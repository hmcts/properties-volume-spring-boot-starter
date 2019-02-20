package uk.gov.hmcts.reform.propertiesvolume;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import uk.gov.hmcts.reform.propertiesvolume.example.App;
import uk.gov.hmcts.reform.propertiesvolume.example.HelloController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = App.class,
    properties = {
        "spring.application.name=propertiesvolume-example",
        "spring.cloud.propertiesvolume.enabled=true",
        "spring.cloud.propertiesvolume.paths="
            + PropertiesvolumesSpringBootTest.BASE_PATH + "/kvcreds-test"
            + ","
            + PropertiesvolumesSpringBootTest.BASE_PATH + "/kvcreds-test-other",
        }
    )
@AutoConfigureWebTestClient
public class PropertiesvolumesSpringBootTest {

    static final String BASE_PATH = "/tmp/Ad1Bg6tfRf";

    @Autowired
    private transient WebTestClient webClient;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        cleanUp();
        Path testDir = Paths.get(BASE_PATH, "kvcreds-test");
        Files.createDirectories(testDir);
        Path testOtherDir = Paths.get(BASE_PATH, "kvcreds-test-other");
        Files.createDirectories(testOtherDir);
        Files.write(testDir.resolve("kv1"), "kv1-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testDir.resolve("kv2"), "kv2-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testOtherDir.resolve("kv3"), "kv3-content".getBytes(StandardCharsets.UTF_8));
    }

    @AfterClass
    public static void cleanUpAfter() throws IOException {
        cleanUp();
    }

    private static void cleanUp() throws IOException {
        if (Paths.get(BASE_PATH).toFile().exists()) {
            Files.walk(Paths.get(BASE_PATH))
                .map(Path::toFile)
                .sorted((o1, o2) -> -o1.compareTo(o2))
                .forEach(File::delete);
        }
    }

    @Test
    public void shouldFindAllExistingVolumeProperties() {
        this.webClient.get().uri("/api/hello?name=kv1").exchange().expectStatus().isOk()
            .expectBody().jsonPath(HelloController.HELLO_FIELD).isEqualTo("Hello, kv1-content!");
        this.webClient.get().uri("/api/hello?name=kv2").exchange().expectStatus().isOk()
            .expectBody().jsonPath(HelloController.HELLO_FIELD).isEqualTo("Hello, kv2-content!");
        this.webClient.get().uri("/api/hello?name=kv3").exchange().expectStatus().isOk()
            .expectBody().jsonPath(HelloController.HELLO_FIELD).isEqualTo("Hello, kv3-content!");
    }

    @Test
    public void shouldNotFindNonExistingVolumeProperties() {
        this.webClient.get().uri("/api/hello?name=kv4").exchange().expectStatus().isOk()
            .expectBody().jsonPath("content").isEqualTo("Hello, not found!");
    }

}
