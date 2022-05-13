import java.util.ArrayList;
import java.nio.file.*;
import java.util.regex.*;

public class dataArrayList {

  public static String readFileAsString(String fileName) throws Exception {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)));
    return data;
  }

  public static String[] getArray(String data) throws Exception {
    data = data.substring(1, data.length() - 1).toString();
    String[] splitData = data.split("},");

    for (String course : splitData) {
      String[] splitCourse = course.split("\",");
      for (String attribute : splitCourse) {
        System.out.println(attribute);
      }
    }
    return splitData;
  }

  public static void main(String[] args) throws Exception {
    String data = readFileAsString("data/data.json");
    // System.out.println(data);
    String[] splitData = getArray(data);
    
  }

}
