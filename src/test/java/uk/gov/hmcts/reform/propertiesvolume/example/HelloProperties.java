package uk.gov.hmcts.reform.propertiesvolume.example;

import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloProperties {

    private String greeting = "Hello, %s!";

    public String getGreeting() {
        return this.greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

}
