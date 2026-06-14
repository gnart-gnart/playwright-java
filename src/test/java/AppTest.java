import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppTest {
    private Playwright playwright;
    private Browser browser;

    @BeforeAll
    void setupSuite() {
        playwright = Playwright.create();
        // Run headless for speed, or set false to watch execution
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @Test
    void testHybridPipeline() {
        // --- PHASE 1: POSTMAN REST API VALIDATION ---
        APIRequestContext apiContext = playwright.request().newContext();
        
        Map<String, String> payload = new HashMap<>();
        payload.put("environment", "Staging");
        payload.put("buildToken", "XYZ9876");

        // Execute API Post against Postman's echo sandboxed endpoint
        APIResponse apiResponse = apiContext.post("https://postman-echo.com/post", 
                RequestOptions.create().setData(payload));
        
        assertEquals(200, apiResponse.status());
        assertTrue(apiResponse.text().contains("XYZ9876"));
        apiContext.dispose();

        // --- PHASE 2: LOCAL UI AUTOMATION LAYER ---
        BrowserContext uiContext = browser.newContext();
        Page page = uiContext.newPage();

        // get htmlResource
        java.net.URL htmlResource = getClass().getClassLoader().getResource("index.html");
        if (htmlResource == null) {
            throw new RuntimeException("index.html missing from src/main/resources/");
        }
        page.navigate(htmlResource.toExternalForm());

        // Direct Playwright to mount the disk resource safely via the file:// protocol
        page.navigate("file://" + localUrl);

        // UI Assertions
        assertEquals("Enterprise Quality Portal", page.title());
        
        // Target elements using strict, modern CSS text locators
        Locator statusText = page.locator("#status-text");
        assertTrue(statusText.innerText().contains("Offline"));

        // Trigger dynamic layout changes on the page natively
        page.locator("#sync-btn").click();
        assertEquals("Status: Ready", statusText.innerText());

        uiContext.close();
    }

    @AfterAll
    void teardownSuite() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}