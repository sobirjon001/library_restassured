package com.cybertek.library.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Driver {
  private static WebDriver driver;

  public static WebDriver getDriver() {
    if (driver == null) {

      String browser = ConfigurationReader.getProperty("browser");
      switch (browser) {
        case "chrome":
          WebDriverManager.chromedriver().setup();
          driver = new ChromeDriver();
          break;
        case "firefox":
          WebDriverManager.firefoxdriver().setup();
          driver = new FirefoxDriver();
          break;
      }

      driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
      driver.manage().timeouts().pageLoadTimeout(2, TimeUnit.SECONDS);
    }
    return driver;
  }

  public static void close(){
    if(driver != null) {
      driver.close();
      driver = null;
    }
  }
}