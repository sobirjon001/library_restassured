package com.cybertek.library.utils;

import io.restassured.http.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class DB_API_Utility {

  private static Connection con;
  private static Statement stm;
  private static ResultSet rs;
  private static ResultSetMetaData rsmd;
  public static String librarianToken;
  public static String studentToken;

  @BeforeClass
  public static void init() {
    // API configuration
    baseURI = ConfigurationReader.getProperty("library1.api.base-url");
    basePath = ConfigurationReader.getProperty("library1.api.base_path");
//    librarianToken = getToken(
//            ConfigurationReader.getProperty("librarian69"),
//            ConfigurationReader.getProperty("librarian69Passwd")
//    );
//    studentToken = getToken(
//            ConfigurationReader.getProperty("student133"),
//            ConfigurationReader.getProperty("student133Password")
//    );
    // DB connection
    createConnection();
  }

  @AfterClass
  public static void cleanup() {
    // close API
    reset();
    // close DB
    destroy();
  }

  public static String getToken(String email, String password) {
    return given()
            .contentType(ContentType.URLENC)
            .formParam("email", email)
            .formParam("password", password).
                    when()
            .post("/login").
                    then()
            .statusCode(200).
                    extract().path("token")
            ;
  }

  /**
   * Create connection method , just checking one connection successful or not
   */
  public static void createConnection() {

    String url = ConfigurationReader.getProperty("library1.database.url");
    String username = ConfigurationReader.getProperty("library1.database.username");
    String password = ConfigurationReader.getProperty("library1.database.password");
    createConnection(url, username, password);

  }

  /**
   * Create Connection by jdbc url and username , password provided
   *
   * @param url
   * @param username
   * @param password
   */
  public static void createConnection(String url, String username, String password) {


    try {
      con = DriverManager.getConnection(url, username, password);
      System.out.println("CONNECTION SUCCESSFUL");
    } catch (SQLException e) {
      System.out.println("CONNECTION HAS FAILED " + e.getMessage());
    }

  }

  /**
   * Run the sql query provided and return ResultSet object
   *
   * @param sql
   * @return ResultSet object  that contains data
   */
  public static ResultSet runQuery(String sql) {

    try {
      stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(sql); // setting the value of ResultSet object
      rsmd = rs.getMetaData();  // setting the value of ResultSetMetaData for reuse
    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE RUNNING QUERY " + e.getMessage());
    }

    return rs;

  }

  /**
   * destroy method to clean up all the resources after being used
   */
  public static void destroy() {
    // WE HAVE TO CHECK IF WE HAVE THE VALID OBJECT FIRST BEFORE CLOSING THE RESOURCE
    // BECAUSE WE CAN NOT TAKE ACTION ON AN OBJECT THAT DOES NOT EXIST
    try {
      if (rs != null) rs.close();
      if (stm != null) stm.close();
      if (con != null) con.close();
    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES " + e.getMessage());
    }

  }

  /**
   * This method will reset the cursor to before first location
   */
  private static void resetCursor() {

    try {
      rs.beforeFirst();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

  }

  // find out the row count

  /**
   * find out the row count
   *
   * @return row count of this ResultSet
   */
  public static int getRowCount() {

    int rowCount = 0;
    try {
      rs.last();
      rowCount = rs.getRow();
    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE GETTING ROW COUNT " + e.getMessage());
    } finally {
      resetCursor();
    }

    return rowCount;

  }


  /**
   * find out the column count
   *
   * @return column count of this ResultSet
   */
  public static int getColumnCount() {

    int columnCount = 0;

    try {
      columnCount = rsmd.getColumnCount();

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE GETTING COLUMN COUNT " + e.getMessage());
    }

    return columnCount;

  }

  // Get all the Column names into a list object

  public static List<String> getAllColumnNamesAsList() {

    List<String> columnNameLst = new ArrayList<>();

    try {
      for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
        String columnName = rsmd.getColumnName(colIndex);
        columnNameLst.add(columnName);
      }
    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getAllColumnNamesAsList " + e.getMessage());
    }

    return columnNameLst;

  }

  // get entire row of data according to row number

  /**
   * Getting entire row of data in a List of String
   *
   * @param rowNum
   * @return row data as List of String
   */
  public static List<String> getRowDataAsList(int rowNum) {

    List<String> rowDataAsLst = new ArrayList<>();
    int colCount = getColumnCount();

    try {
      rs.absolute(rowNum);

      for (int colIndex = 1; colIndex <= colCount; colIndex++) {

        String cellValue = rs.getString(colIndex);
        rowDataAsLst.add(cellValue);

      }


    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getRowDataAsList " + e.getMessage());
    } finally {
      resetCursor();
    }


    return rowDataAsLst;
  }


  /**
   * getting the cell value according to row num and column index
   *
   * @param rowNum
   * @param columnIndex
   * @return the value in String at that location
   */
  public static String getCellValue(int rowNum, int columnIndex) {

    String cellValue = "";

    try {
      rs.absolute(rowNum);
      cellValue = rs.getString(columnIndex);

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
    } finally {
      resetCursor();
    }
    return cellValue;

  }

  /**
   * getting the cell value according to row num and column name
   *
   * @param rowNum
   * @param columnName
   * @return the value in String at that location
   */
  public static String getCellValue(int rowNum, String columnName) {

    String cellValue = "";

    try {
      rs.absolute(rowNum);
      cellValue = rs.getString(columnName);

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
    } finally {
      resetCursor();
    }
    return cellValue;

  }


  /**
   * Get First Cell Value at First row First Column
   */
  public static String getFirstRowFirstColumn() {

    return getCellValue(1, 1);

  }

  //

  /**
   * getting entire column data as list according to column number
   *
   * @param columnNum
   * @return List object that contains all rows of that column
   */
  public static List<String> getColumnDataAsList(int columnNum) {

    List<String> columnDataLst = new ArrayList<>();

    try {
      rs.beforeFirst(); // make sure the cursor is at before first location
      while (rs.next()) {

        String cellValue = rs.getString(columnNum);
        columnDataLst.add(cellValue);
      }

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
    } finally {
      resetCursor();
    }


    return columnDataLst;

  }

  /**
   * getting entire column data as list according to column Name
   *
   * @param columnName
   * @return List object that contains all rows of that column
   */
  public static List<String> getColumnDataAsList(String columnName) {

    List<String> columnDataLst = new ArrayList<>();

    try {
      rs.beforeFirst(); // make sure the cursor is at before first location
      while (rs.next()) {

        String cellValue = rs.getString(columnName);
        columnDataLst.add(cellValue);
      }

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
    } finally {
      resetCursor();
    }


    return columnDataLst;

  }


  /**
   * display all data from the ResultSet Object
   */
  public static void displayAllData() {

    int columnCount = getColumnCount();
    resetCursor();
    try {

      while (rs.next()) {

        for (int colIndex = 1; colIndex <= columnCount; colIndex++) {

          //System.out.print( rs.getString(colIndex) + "\t" );
          System.out.printf("%-25s", rs.getString(colIndex));
        }
        System.out.println();

      }

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE displayAllData " + e.getMessage());
    } finally {
      resetCursor();
    }

  }

  /**
   * Save entire row data as Map<String,String>
   *
   * @param rowNum
   * @return Map object that contains key value pair
   * key     : column name
   * value   : cell value
   */
  public static Map<String, String> getRowMap(int rowNum) {

    Map<String, String> rowMap = new LinkedHashMap<>();
    int columnCount = getColumnCount();

    try {

      rs.absolute(rowNum);

      for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
        String columnName = rsmd.getColumnName(colIndex);
        String cellValue = rs.getString(colIndex);
        rowMap.put(columnName, cellValue);
      }

    } catch (SQLException e) {
      System.out.println("ERROR OCCURRED WHILE getRowMap " + e.getMessage());
    } finally {
      resetCursor();
    }


    return rowMap;
  }

  /**
   * We know how to store one row as map object
   * Now Store All rows as List of Map object
   *
   * @return List of List of Map object that contain each row data as Map<String,String>
   */
  public static List<Map<String, String>> getAllRowAsListOfMap() {

    List<Map<String, String>> allRowLstOfMap = new ArrayList<>();
    int rowCount = getRowCount();
    // move from first row till last row
    // get each row as map object and add it to the list

    for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {

      Map<String, String> rowMap = getRowMap(rowIndex);
      allRowLstOfMap.add(rowMap);

    }
    resetCursor();

    return allRowLstOfMap;

  }
}