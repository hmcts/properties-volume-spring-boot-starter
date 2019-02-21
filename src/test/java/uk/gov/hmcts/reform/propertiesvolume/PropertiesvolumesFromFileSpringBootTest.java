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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = App.class
    )
@AutoConfigureWebTestClient
public class PropertiesvolumesFromFileSpringBootTest {

    static final String BASE_PATH = "/tmp/Nhdt625dhwUyt";

    @Autowired
    private transient WebTestClient webClient;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        Volumes.cleanUp(BASE_PATH);
        Path testDir = Paths.get(BASE_PATH, "kvcreds-test");
        Files.createDirectories(testDir);
        Files.write(testDir.resolve("kv1"), "kv1-content".getBytes(StandardCharsets.UTF_8));
        Files.write(testDir.resolve("kv2"), "kv2-content".getBytes(StandardCharsets.UTF_8));
    }

    @AfterClass
    public static void cleanUpAfter() throws IOException {
        Volumes.cleanUp(BASE_PATH);
    }

    @Test
    public void shouldFindAllDeclaredVolumeProperties() {
        final String[] declaredKeys = {"kv1", "kv2"};
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
