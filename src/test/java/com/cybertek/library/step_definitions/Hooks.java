package com.cybertek.library.step_definitions;

import com.cybertek.library.utils.ConfigurationReader;
import com.cybertek.library.utils.DB_API_Utility;
import com.cybertek.library.utils.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import static io.restassured.RestAssured.*;

public class Hooks {

  @Before(value = "@api")
  public void connect_API() {
    // API configuration
    baseURI = ConfigurationReader.getProperty("library1.api.base-url");
    basePath = ConfigurationReader.getProperty("library1.api.base_path");
    // update token everytime before API request
    DB_API_Utility.librarianToken = DB_API_Utility.getToken(
            ConfigurationReader.getProperty("librarian66"),
            ConfigurationReader.getProperty("librarian66Password")
    );
  }

  @After(value = "@api", order = 1)
  public void destroyAPI(){
    // close API
    reset();
  }

  @After(order = 2)
  public void closeWebDriver(){
    // close WebDriver after test complete
    Driver.close();
  }

}
