@smoke @api
Feature: 1 Add user end point test

#this test case specifically for testing the add user service
  @add_user  @librarian
  Scenario: add student using add user service
    Given new student is added using the add_user endpoint
    And I am on the "qa1_url" login page
    When I login as the new user created using add_user endpoint
    Then "books" page should be displayed

    #in this test case we test the books table
    # for this test we want to use API to generate new user information
    # so we are using api for test data generation

  Scenario: books table
    Given new student is added using the add_user endpoint
    And I am on the "qa1_url" login page
    When I login as the new user created using add_user endpoint
    And I navigate to "Books" page
    When I search for book "Loralee" and create POJO
    Then I verify book information
      | name   | Loralee      |
      | author | Ellis Kemmer |
      | year   | 2014         |



# UI: login as some one
# UI:  open any book may be this one: The kite runner
# DB: get book id from books DB with given book info
# UI, API:   verify that book information matches the response from  /get_book_by_id/{id}  endpoint
  @db @wip
  Scenario: verify book information using get_book_by_id endpoint
    Given I am on the "qa1_url" login page
    When I login as a "librarian"
    And I navigate to "Books" page
    When I search for book "Loralee" and create POJO
    And I find book id from DB by info from book POJO
    Then book information must match the api for book "Loralee"
