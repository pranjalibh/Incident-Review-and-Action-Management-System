package com.tus.incidentmanagement.karate;

import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KarateRunnerTestIT {

    @LocalServerPort
    int randomServerPort;

    @Karate.Test
    Karate runAll() {
        System.setProperty("local.server.port", String.valueOf(randomServerPort));
        return Karate.run("classpath:com/tus/incidentmanagement");    }

}


