package com.cybertek.library.utils.pojo;

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
public class UserWithId extends User{
  private String id;

  public static UserWithId student1 = new UserWithId();
  public static UserWithId librarian1 = new UserWithId();
  public static List<UserWithId> users = new ArrayList<>();

  public static User getRandomUser() {
    UserWithId result = new UserWithId();
    result.setFull_name(faker.name().fullName());
    result.setEmail(faker.internet().emailAddress());
    result.setPassword(faker.internet().password());
    result.setUser_group_id(2);
    result.setStatus("ACTIVE");
    result.setStart_date(LocalDate.now().toString());
    result.setEnd_date(LocalDate.now().plusMonths(4L).toString());
    result.setAddress(faker.address().fullAddress());

    return result;
  }
}
