package com.cybertek.library.step_definitions;

import com.cybertek.library.utils.ConfigurationReader;
import com.cybertek.library.utils.DB_API_Utility;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class Hooks {

  @Before(value = "@api")
  public void connect_API() {
    // update token everytime before API request
    DB_API_Utility.librarianToken = DB_API_Utility.getToken(
            ConfigurationReader.getProperty("librarian69"),
            ConfigurationReader.getProperty("librarian69Passwd")
    );
  }

}
