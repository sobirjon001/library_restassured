package com.cybertek.library.step_definitions;

import com.cybertek.library.utils.ConfigurationReader;
import com.cybertek.library.utils.DB_API_Utility;
import com.cybertek.library.utils.Driver;
import com.cybertek.library.utils.Variables;
import com.cybertek.library.utils.pojo.User;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class All_stepDefinitions extends DB_API_Utility implements Variables {
  @Given("new student is added using the add_user endpoint")
  public void new_student_is_added_using_the_add_user_endpoint() {
    // user group id fro student is "3"
    User.student1 = User.getRandomUser("3");
    given()
            .log().all()
            .header("x-library-token", librarianToken)
            .contentType(ContentType.URLENC)
            .formParam(User.student1.getFull_name())
            .formParam(User.student1.getEmail())
            .formParam(User.student1.getPassword())
            .formParam(User.student1.getUser_group_id())
            .formParam(User.student1.getStatus())
            .formParam(User.student1.getStart_date())
            .formParam(User.student1.getEnd_date())
            .formParam(User.student1.getAddress()).
    when()
            .post("/add_user").
    then()
            .statusCode(200)
            .body("message", is("The user has been created"))
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

  }
  @Then("{string} page should be displayed")
  public void page_should_be_displayed(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }
}
