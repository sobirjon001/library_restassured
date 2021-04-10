package com.cybertek.library.step_definitions;

import com.cybertek.library.page.Landing_Page;
import com.cybertek.library.page.Login_Page;
import com.cybertek.library.utils.ConfigurationReader;
import com.cybertek.library.utils.DB_API_Utility;
import com.cybertek.library.utils.Driver;
import com.cybertek.library.utils.Variables;
import com.cybertek.library.utils.pojo.User;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class All_stepDefinitions extends DB_API_Utility implements Variables {

  Login_Page login_page = new Login_Page();
  Landing_Page landing_page = new Landing_Page();

  @Given("new student is added using the add_user endpoint")
  public void new_student_is_added_using_the_add_user_endpoint() {
    // user group id fro student is "3"
    User.student1 = User.getRandomUser("3");
    given()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.URLENC)
            .formParam("full_name", User.student1.getFull_name())
            .formParam("email", User.student1.getEmail())
            .formParam("password", User.student1.getPassword())
            .formParam("user_group_id", User.student1.getUser_group_id())
            .formParam("status", User.student1.getStatus())
            .formParam("start_date", User.student1.getStart_date())
            .formParam("end_date", User.student1.getEnd_date())
            .formParam("address", User.student1.getAddress()).
    when()
            .post("/add_user").
    then()
            .statusCode(200)
            .body("message", is("The user has been created."))
    ;
  }

  @Given("I am on the {string} login page")
  public void i_am_on_the_login_page(String env_url) {
    Driver.getDriver().get(
            ConfigurationReader.getProperty(env_url)
    );
  }
  @When("I login as the new user created using add_user endpoint")
  public void i_login_as_the_new_user_created_using_add_user_endpoint() {
    login_page.inputEmail.sendKeys(User.student1.getEmail());
    login_page.inputPassword.sendKeys(User.student1.getPassword());
    login_page.buttonSignIn.click();
  }
  @Then("{string} page should be displayed")
  public void page_should_be_displayed(String expectedLandingPageExtension) {
    sleep(1);
    String actualPageUrl = Driver.getDriver().getCurrentUrl();
    Assert.assertTrue(actualPageUrl.contains(expectedLandingPageExtension));
  }

  @When("I navigate to {string} page")
  public void i_navigate_to_page(String module) {
    landing_page.moduleNavigation.findElement(By.xpath(
            ".//a[   .//span[.='" + module + "']    ]"
    )).click();
  }

  @When("I search for book {string} and create POJO")
  public void i_search_for_book(String searchKey) {
    landing_page.inputBookSearch.sendKeys(searchKey);
  }
  @Then("I verify book information")
  public void i_verify_book_information(Map<String, String> actualBookMap) {
    System.out.println("actualBookMap = " + actualBookMap);
    sleep(1);
    String expectedBookName = landing_page.firstRowBookTable.findElement(By.xpath(
            "./td[3]"
    )).getText();
    String expectedBookAuthor = landing_page.firstRowBookTable.findElement(By.xpath(
            "./td[4]"
    )).getText();
    String expectedBookYear = landing_page.firstRowBookTable.findElement(By.xpath(
            "./td[6]"
    )).getText();

    Assert.assertEquals(expectedBookName, actualBookMap.get("name"));
    Assert.assertEquals(expectedBookAuthor, actualBookMap.get("author"));
    Assert.assertEquals(expectedBookYear, actualBookMap.get("year"));
  }

  @When("I login as a {string}")
  public void i_login_as_a(String userType) {
    String email = "";
    String password = "";
    switch (userType) {
      case "librarian":
        email = ConfigurationReader.getProperty("librarian66");
        password = ConfigurationReader.getProperty("librarian66Password");
        break;
      case "student":
        email = ConfigurationReader.getProperty("student133");
        password = ConfigurationReader.getProperty("student133Password");
        break;
    }
    i_am_on_the_login_page("qa1_url");
    login_page.inputEmail.sendKeys(email);
    login_page.inputPassword.sendKeys(password);
    login_page.buttonSignIn.click();
    page_should_be_displayed("dashboard");
  }

  @Then("book information must match the api for book {string}")
  public void book_information_must_match_the_api_for_book(String searchBookName) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }
}
