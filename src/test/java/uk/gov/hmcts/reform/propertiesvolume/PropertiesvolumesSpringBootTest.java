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
import java.util.Arrays;


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
            + PropertiesvolumesSpringBootTest.BASE_PATH + "/kvcreds-test-other"
            + ","
            + PropertiesvolumesSpringBootTest.BASE_PATH + "/kvcreds-test-single/kv5",
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
        Path testSingleDir = Paths.get(BASE_PATH, "kvcreds-test-single");
        Files.createDirectories(testSingleDir);
        Files.write(testDir.resolve("kv1"), "kv1-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testDir.resolve("kv2"), "kv2-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testOtherDir.resolve("kv3"), "kv3-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testSingleDir.resolve("kv4"), "kv4-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testSingleDir.resolve("kv5"), "kv5-content".getBytes(StandardCharsets.UTF_8));
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
    public void shouldFindAllDeclaredVolumeProperties() {
        final String[] declaredKeys = {"kv1", "kv2", "kv3", "kv5"};
        Arrays.stream(declaredKeys).forEach(k ->
            this.webClient.get().uri("/api/hello?name=" + k).exchange().expectStatus().isOk()
                .expectBody().jsonPath(HelloController.HELLO_FIELD).isEqualTo(String.format("Hello, %s-content!", k))
        );
    }

    @Test
    public void shouldNotFindNonDeclaredVolumeProperties() {
        this.webClient.get().uri("/api/hello?name=kv4").exchange().expectStatus().isOk()
            .expectBody().jsonPath("content").isEqualTo("Hello, not found!");
    }

}
