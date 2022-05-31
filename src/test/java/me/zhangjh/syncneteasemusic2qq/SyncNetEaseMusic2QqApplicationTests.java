package me.zhangjh.syncneteasemusic2qq;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class SyncNetEaseMusic2QqApplicationTests {


    @Value("${chrome.driver.path:/Users/zhangjh/Downloads/chromedriver}")
    private String chromeDriverPath;         // 注意下载与本地浏览器适配的webdriver

    private static final boolean SET_COOKIE_FLAG = false;

    @Value("${qq.cookies}")
    private String qqCookies;

    @Test
    @SneakyThrows
    void contextLoads() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.addArguments("disable-infobars"); // disabling infobars
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("--disable-gpu"); // applicable to windows os only
        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--no-sandbox"); // Bypass OS security model

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://y.qq.com/n/ryqq/search");

        try {
            if(!SET_COOKIE_FLAG) {
                String[] cookies = qqCookies.split(";");
                for (String cookie : cookies) {
                    String[] split = cookie.split("=");
                    if(split.length != 2) {
                        continue;
                    }
                    String key = split[0];
                    String value = split[1];
                    // 注意不能有空格，可从网页header部分复制cookie
                    driver.manage().addCookie(new Cookie(key.trim(), value.trim(), ".qq.com", "/",
                            new Date(System.currentTimeMillis() + 3600 * 1000)));
                }
            }
            // do something
            Thread.sleep(2000);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

}
