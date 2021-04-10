package com.cybertek.library.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Login_Page extends Base_Page {

  @FindBy(id = "inputEmail")
  public WebElement inputEmail;

  @FindBy(id = "inputPassword")
  public WebElement inputPassword;

  @FindBy(xpath = "//button[@class='btn btn-lg btn-primary btn-block']")
  public WebElement buttonSignIn;

}
