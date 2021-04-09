package com.cybertek.library.page;

import com.cybertek.library.utils.Driver;
import org.openqa.selenium.support.PageFactory;

public class Base_Page {
  public Base_Page() {
    PageFactory.initElements(Driver.getDriver(), this);
  }
}
