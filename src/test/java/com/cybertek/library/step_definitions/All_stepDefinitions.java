package com.cybertek.library.step_definitions;

import com.cybertek.library.page.Landing_Page;
import com.cybertek.library.page.Login_Page;
import com.cybertek.library.utils.ConfigurationReader;
import com.cybertek.library.utils.DB_API_Utility;
import com.cybertek.library.utils.Driver;
import com.cybertek.library.utils.Variables;
import com.cybertek.library.utils.pojo.Book;
import com.cybertek.library.utils.pojo.BookWithId;
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
            .contentType(ContentType.URLENC)
            .header("x-library-token", librarianToken)
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
    sleep(1);
    BookWithId b1 = new BookWithId();
    b1.setIsbn(landing_page.firstRowBookTable.findElement(By.xpath("./td[2]")).getText());
    b1.setName(landing_page.firstRowBookTable.findElement(By.xpath("./td[3]")).getText());
    b1.setAuthor(landing_page.firstRowBookTable.findElement(By.xpath("./td[4]")).getText());
    b1.setCategory(landing_page.firstRowBookTable.findElement(By.xpath("./td[5]")).getText());
    b1.setYear(landing_page.firstRowBookTable.findElement(By.xpath("./td[6]")).getText());

    BookWithId.bookWithId1 = b1;
  }
  @Then("I verify book information")
  public void i_verify_book_information(Map<String, String> actualBookMap) {
    BookWithId b1 = BookWithId.bookWithId1;

    Assert.assertEquals(b1.getName(), actualBookMap.get("name"));
    Assert.assertEquals(b1.getAuthor(), actualBookMap.get("author"));
    Assert.assertEquals(b1.getYear(), actualBookMap.get("year"));
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

  @When("I find book id from DB by info from book POJO")
  public void i_find_book_id_from_DB_by_info_from_book_POJO() {
    runQuery("select * from books where author = '" +
            BookWithId.bookWithId1.getAuthor() + "' and name = '" +
            BookWithId.bookWithId1.getName() + "' and year = '" +
            BookWithId.bookWithId1.getYear() + "'");
    displayAllData();
    BookWithId.bookWithId1.setId(getFirstRowFirstColumn());
  }

  @Then("book information must match the api for book {string}")
  public void book_information_must_match_the_api_for_book(String searchBookName) {
    given()
            .log().uri()
            .contentType(ContentType.URLENC)
            .header("x-library-token", librarianToken)
            .formParam("")
            .pathParam("id", BookWithId.bookWithId1.getId()).
    when()
            .get("/get_book_by_id/{id}").
    then()
            .statusCode(200)
            .log().body()
            .body("id", is(BookWithId.bookWithId1.getId()))
            .body("name", is(BookWithId.bookWithId1.getName()))
            .body("isbn", is(BookWithId.bookWithId1.getIsbn()))
            .body("year", is(BookWithId.bookWithId1.getYear()))
            .body("author", is(BookWithId.bookWithId1.getAuthor()))
    ;
  }
}
