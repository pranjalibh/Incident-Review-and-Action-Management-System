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
class InvalidLoginSeleniumTest {

    @LocalServerPort
    int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void login_shouldFail_withWrongPassword() {

        driver.get("http://localhost:" + port + "/login.html");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
                .sendKeys("manager1");

        driver.findElement(By.id("password"))
                .sendKeys("wrongpassword"); // WRONG

        driver.findElement(By.id("loginBtn")).click();

        // wait for error message
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("errorBox"))
        );

        assertTrue(error.isDisplayed(), "Error message not shown");

        // also ensure NOT redirected
        assertTrue(driver.getCurrentUrl().contains("login.html"),
                "Should NOT redirect on failed login");
    }
}