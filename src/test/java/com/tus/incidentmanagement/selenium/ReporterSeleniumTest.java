package com.tus.incidentmanagement.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("data") // use your working profile

class ReporterSeleniumTest {
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {

        // Clean test data only
        jdbcTemplate.update(
                "DELETE FROM incidents WHERE title LIKE ?",
                "[TEST]%"
        );

        if (driver != null) {
            driver.quit();
        }
    }

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
    void reporter_shouldCreateIncident() {

        // login (must match your DB data)
        login("reporter1", "admin123");

        // wait for redirect OR failure message
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("reporter.html"),
                ExpectedConditions.visibilityOfElementLocated(By.id("errorBox"))
        ));

        // assert login success FIRST (this avoids silent failures)
        assertTrue(driver.getCurrentUrl().contains("reporter.html"),
                "Login failed - check username/password in DB");

        // fill form
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")))
                .sendKeys("[TEST] Selenium Incident");
        driver.findElement(By.id("description"))
                .sendKeys("Created by Selenium test");

        WebElement severity = driver.findElement(By.id("severity"));
        severity.sendKeys("HIGH");

        driver.findElement(By.id("createBtn")).click();

        // wait for success message
        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("message"))
        );

        // assert
        assertTrue(message.getText().contains("Incident"),
                "Incident creation failed");
    }

}