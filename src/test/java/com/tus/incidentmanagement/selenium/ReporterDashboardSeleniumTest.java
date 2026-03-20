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
class ReporterDashboardSeleniumTest {

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

    void login() {
        driver.get("http://localhost:" + port + "/login.html");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                .sendKeys("reporter1");

        driver.findElement(By.id("password"))
                .sendKeys("admin123");

        driver.findElement(By.id("loginBtn")).click();
    }

    @Test
    void reporter_dashboard_shouldLoadElements() {

        login();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("reporter.html"),
                ExpectedConditions.visibilityOfElementLocated(By.id("errorBox"))
        ));

        assertTrue(driver.getCurrentUrl().contains("reporter.html"),
                "Reporter login failed");

        // Validate important UI elements exist
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("title"))
        );

        WebElement description = driver.findElement(By.id("description"));
        WebElement createBtn = driver.findElement(By.id("createBtn"));

        assertTrue(title.isDisplayed(), "Title field missing");
        assertTrue(description.isDisplayed(), "Description field missing");
        assertTrue(createBtn.isDisplayed(), "Create button missing");
    }
}