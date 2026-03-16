import com.tus.incidentmanagement.IncidentManagementApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = IncidentManagementApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ApiTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void contextLoads() {
        assertTrue(true);
    }
}