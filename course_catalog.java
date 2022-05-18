import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.nio.file.*;
import java.util.regex.*;

public class course_catalog extends Application {
    private TableView<Course> table = new TableView<Course>();
    static ArrayList<Course> splitData;
    private final ObservableList<Course> cData = FXCollections.observableArrayList(splitData);

    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage stage) {
        // Creating the the Columns for the ListView
        Scene scene = new Scene(new Group());
        stage.setTitle("College Course Catalog");
        stage.setWidth(500);
        stage.setHeight(500);
        final Label label = new Label("University of North Carolina - Chapel Hill Courses");
        table.setEditable(true);
        TableColumn courseNameCol = new TableColumn("Course Name");
        TableColumn courseCodeCol = new TableColumn("Course Code");
        TableColumn descripCol = new TableColumn("Description");

        // Creating a filtered list with the data
        FilteredList<Course> flCourse = new FilteredList(cData, p -> true);
        table.setItems(flCourse);
        table.getColumns().addAll(courseNameCol, courseCodeCol, descripCol);
        // Adding Choice Box and Search Bar here
        // snagged my code from here,
        // https://stackoverflow.com/questions/47559491/making-a-search-bar-in-javafx
        // there are certain things that need to be changed before it's functionable
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Course Name", "Course Code", "Description");
        choiceBox.setValue("Course Name");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            switch (choiceBox.getValue())// Switch choiceBox value
            {
                case "Course Name":
                    flCourse.setPredicate(p -> p.getCourseName().toLowerCase().contains(newValue.toLowerCase().trim()));// filter table by Course Name
                    break;
                case "Course Code":
                    flCourse.setPredicate(p -> p.getCourseCode().toLowerCase().contains(newValue.toLowerCase().trim()));// filter table by Course Code
                    break;
                case "Description":
                    flCourse.setPredicate(p -> p.getDescrip().toLowerCase().contains(newValue.toLowerCase().trim()));// filter table by description
                    break;
            }
        });

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {// this resets table and text whenever new choice is selected
            if (newVal != null) {
                searchBar.setText("");
            }
        });

        HBox hBox = new HBox(choiceBox, searchBar);// Add choiceBox and searchBar to hBox
        hBox.setAlignment(Pos.CENTER);// Center HBox
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

        stage.show();
    }

    public static class Course {
        private final SimpleStringProperty courseName = new SimpleStringProperty();
        private final SimpleStringProperty courseCode = new SimpleStringProperty();
        private final SimpleStringProperty descrip = new SimpleStringProperty();

        private Course(String cName, String cCode, String cDescrip) {
            this.courseName.setValue(cName);
            this.courseCode.setValue(cCode);
            this.descrip.setValue(cDescrip);
        }

        public String getCourseName() {
            return courseName.get();
        }

        public void setCourseName(String cName) {
            courseName.set(cName);
        }

        public SimpleStringProperty getCourseNameProperty() {
            return courseName;
        }

        public String getCourseCode() {
            return courseCode.get();
        }

        public void setCourseCode(String cCode) {
            courseCode.set(cCode);
        }

        public SimpleStringProperty getCourseCodeProperty() {
            return courseCode;
        }

        public String getDescrip() {
            return descrip.get();
        }

        public void setDescrip(String cDescrip) {
            descrip.set(cDescrip);
        }

        public SimpleStringProperty getDescripProperty() {
            return descrip;
        }
    }

    public static String readFileAsString(String fileName) throws Exception {
        String cData = "";
        cData = new String(Files.readAllBytes(Paths.get(fileName)));
        return cData;
    }

    public static ArrayList<Course> getArray(String data) throws Exception {
      ArrayList<Course> output = new ArrayList<>();
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
            System.err.println("error occurred during parsing");
          }
          try {
            for (String arg : args) {
              output.add(new Course(args.get(2), args.get(1), args.get(4)));
            }
          } catch (Exception e) {
            System.err.println(e.toString());
            System.err.println("error occurred during course generation");
          }
        }
      } catch (Exception e) {
        System.err.println(e.toString());
        System.err.println("unknown error");
      }
      return output;
    }

    public static void main(String[] args) throws Exception {
        String input = readFileAsString("data/data.json");
        splitData = getArray(input);
        ObservableList<Course> data = FXCollections.observableArrayList();
        data.addAll(splitData);
        launch(args);
      }
  }