package com.cybertek.library.utils.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookWithId extends Book{
  private String id;
  //private int book_category_id;

  public static BookWithId bookWithId1;
  public static BookWithId bookWithId2;
  public static List<BookWithId> booksWithIdList;
}
