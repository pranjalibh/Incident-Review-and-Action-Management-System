package com.tus.incidentmanagement.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("data")
class ManagerSeleniumTest {

    @LocalServerPort
    int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();


        String chromePath = System.getenv("CHROME_BIN");
        if (chromePath != null && !chromePath.isEmpty()) {
            options.setBinary(chromePath);
        }


        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 🔁 reusable login
    void login(String username, String password) {

        driver.get("http://localhost:" + port + "/login.html");

        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );

        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("password"))
        );

        WebElement loginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("loginBtn"))
        );

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginBtn.click();
    }

    @Test
    void manager_shouldSeeDashboardContent() {

        // login
        login("manager1", "admin123");

        // wait for redirect OR failure
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("manager.html"),
                ExpectedConditions.visibilityOfElementLocated(By.id("errorBox"))
        ));

        // assert login success
        assertTrue(driver.getCurrentUrl().contains("manager.html"),
                "Manager login failed");

        //page content exists
        WebElement body = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.tagName("body"))
        );

        assertTrue(body.getText().length() > 0,
                "Manager dashboard did not load properly");
    }
}