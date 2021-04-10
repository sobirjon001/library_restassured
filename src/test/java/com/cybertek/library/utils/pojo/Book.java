package com.cybertek.library.utils.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
  private String name;
  private String isbn;
  private String year;
  private String author;
  private String category;
  //private String description;

  public static Book book1;
  public static Book book2;
  public static List<Book> books = new ArrayList<>();
}
