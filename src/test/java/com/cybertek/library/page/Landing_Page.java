package com.cybertek.library.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Landing_Page extends Base_Page{

  @FindBy(id = "menu_item")
  public WebElement moduleNavigation;

  @FindBy(xpath = "//div[@id='tbl_books_filter']//input")
  public WebElement inputBookSearch;

  @FindBy(xpath = "//table[@id='tbl_books']/tbody/tr[1]")
  public WebElement firstRowBookTable;
}
