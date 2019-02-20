package uk.gov.hmcts.reform.propertiesvolume.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings({"PMD", "checkstyle:hideutilityclassconstructor"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
