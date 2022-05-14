import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.nio.file.*;
import java.util.regex.*;

public class dataArrayList {

  public static String readFileAsString(String fileName) throws Exception {
    String data = "";
    data = new String(Files.readAllBytes(Paths.get(fileName)));
    return data;
  }

  public static ArrayList<Course> getArray(String data) throws Exception {
    ArrayList<Course> output = new ArrayList<>()
    data = data.substring(1, data.length() - 1).toString();
    String[] splitData = data.split("},");
    try {
      for (String course : splitData) {
        String[] splitCourse = course.split("\",");
        ArrayList<String> args = new ArrayList<>();
        try {
          for (String attribute : splitCourse) {
            String[] isolateVal = attribute.split(":");
            attribute = isolateVal[1];
            if (attribute.charAt(0) == '"') {
              attribute = attribute.substring(1);
            }
            if (attribute.charAt(attribute.length() - 1) == '"') {
              attribute = attribute.substring(0, attribute.length() - 1);
            }

            args.add(attribute);
          }
        } catch (Exception e) {
          System.err.println(e.toString());
        }
        try {
          for (String arg : args) {
            output.add(Course(args.get(2), args.get(1), args.get(4)));
          }
        } catch (Exception e) {
          System.err.println(e.toString());
        }
      }
    } catch (Exception e) {
      System.err.println(e.toString());
    }
    return output;
  }

  public static void main(String[] args) throws Exception {
    String data = readFileAsString("data/data.json");
    String[] splitData = getArray(data);
  }

}
