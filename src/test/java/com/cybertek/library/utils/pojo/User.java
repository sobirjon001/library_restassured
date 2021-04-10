package com.cybertek.library.utils.pojo;

import com.cybertek.library.utils.Variables;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Variables {
  private String full_name;
  private String email;
  private String password;
  private String user_group_id;
  private String image;
  private String extra_data;
  private String status;
  private String is_admin;
  private String start_date;
  private String end_date;
  private String address;

  public static User student1;
  public static User librarian1;
  public static List<User> users = new ArrayList<>();

  public static User getRandomUser(String userType) {
    User result = new User();
    result.setFull_name(faker.name().fullName());
    result.setEmail(faker.internet().emailAddress());
    result.setPassword(faker.internet().password());
    result.setUser_group_id(userType);
    result.setStatus("ACTIVE");
    result.setStart_date(LocalDate.now().toString());
    result.setEnd_date(LocalDate.now().plusMonths(4L).toString());
    result.setAddress(faker.address().fullAddress());

    return result;
  }
}
