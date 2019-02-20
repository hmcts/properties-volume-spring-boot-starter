package uk.gov.hmcts.reform.propertiesvolume.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class HelloController {

    private final transient HelloProperties properties;
    private final transient Environment env;

    public static final String HELLO_FIELD = "content";

    @Autowired
    public HelloController(HelloProperties properties, Environment env) {
        this.properties = properties;
        this.env = env;
    }

    @RequestMapping("/api/hello")
    public Map greeting(
        @RequestParam(value = "name", defaultValue = "World") String name) {
        return Collections.singletonMap(
            HELLO_FIELD,
            String.format(
                this.properties.getGreeting(),
                 env.getProperty(name, "not found")
            )
        );
    }

}
