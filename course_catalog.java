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
        // Creating the the Columns for the TableView
        Scene scene = new Scene(new Group());

        stage.setTitle("College Course Catalog");
        stage.setWidth(800);
        stage.setHeight(500);
        final Label label = new Label("University of North Carolina - Chapel Hill Courses");
        table.setEditable(false);
        table.prefWidthProperty().bind(stage.widthProperty());
        TableColumn courseNameCol = new TableColumn("Course Name");
        courseNameCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        TableColumn courseCodeCol = new TableColumn("Course Code");
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseCode"));
        TableColumn descripCol = new TableColumn("Description");
        descripCol.setCellValueFactory(new PropertyValueFactory<Course, String>("descrip"));

        // Creating a filtered list with cData
        FilteredList<Course> flCourse = new FilteredList(cData, p -> true);
        table.getColumns().addAll(courseNameCol, courseCodeCol, descripCol);
        table.setItems(flCourse);

        //Choice Box and Search Bar here
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Course Name", "Course Code", "Description");
        choiceBox.setValue("Course Name");
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");

        //Allows the searchBar to read the choiceBox values
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

        // Resets table and text whenever new choice is selected
        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                searchBar.setText("");
            }
        });

        HBox hBox = new HBox(choiceBox, searchBar);
        hBox.setAlignment(Pos.CENTER);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 50, 0, 10));
        vbox.getChildren().addAll(label, table, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        scene.getStylesheets().add(getClass().getResource("course_catalog.css").toExternalForm());
        stage.setScene(scene);
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
        System.out.println("any errors that follow are from the parsing");
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
            output.add(new Course(args.get(2), args.get(1), args.get(4)));
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