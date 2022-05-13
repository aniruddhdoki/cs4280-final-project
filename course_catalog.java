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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class course_catalog extends Application{
    private TableView<Course> table = new TableView<Course>();
    private final ObservableList<Course> data = FXCollections.observableArrayList(
        new Course("TEST123", "Test Course", "Test Description")
    );

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage stage){
        //Creating the GridPane and Setting up the Columns for the ListView
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 500, 500);
        stage.setTitle("College Course Catalog");
        stage.setWidth(500);
        stage.setHeight(500);
        final Label label = new Label("University of North Carolina - Chapel Hill Courses");
        table.setEditable(true);
        TableColumn courseNameCol = new TableColumn("Course Name");
        TableColumn courseCodeCol = new TableColumn("Course Code");
        TableColumn descripCol = new TableColumn("Description");
        table.getColumns().addAll(courseCodeCol, courseNameCol, descripCol);
        gridPane.add(table, 1, 0, 3, 1);
        
        //Creating a filtered list with the data
        FilteredList<Course> flCourse = new FilteredList(data, p -> true);

        //Adding Choice Box and Search Bar here
        //snagged my code from here, https://stackoverflow.com/questions/47559491/making-a-search-bar-in-javafx
        //there are certain things that need to be changed before it's functionable
        ChoiceBox<String> choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("Course Name", "Course Code", "Description");
        choiceBox.setValue("Course Name");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.textProperty().addListener((obs, oldValue, newValue) -> {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "Course Name":
                    flCourse.setPredicate(p -> p.getCourseName().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by Course Name
                    break;
                case "Course Code":
                    flCourse.setPredicate(p -> p.getCourseCode().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by Course Code
                    break;
                case "Description":
                    flCourse.setPredicate(p -> p.getDescrip().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by description words
                    break;
            }
        });

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                -> {//reset table and textfield when new choice is selected
            if (newVal != null) {
                searchBar.setText("");
            }
        });

        HBox hBox = new HBox(choiceBox, searchBar);//Add choiceBox and searchBar to hBox
        hBox.setAlignment(Pos.CENTER);//Center HBox
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

        stage.show();
    }

    public static class Course
    {
        private final SimpleStringProperty courseName = new SimpleStringProperty();
        private final SimpleStringProperty courseCode = new SimpleStringProperty();
        private final SimpleStringProperty descrip = new SimpleStringProperty();

        private Course(String cName, String cCode, String cDescrip)
        {
            this.courseName.setValue(cName);
            this.courseCode.setValue(cCode);
            this.descrip.setValue(cDescrip);
        }

        public String getCourseName()
        {
            return courseName.get();
        }

        public void setCourseName(String cName)
        {
            courseName.set(cName);
        }
        
        public SimpleStringProperty getCourseNameProperty()
        {
            return courseName;
        }
        
        public String getCourseCode()
        {
            return courseCode.get();
        }

        public void setCourseCode(String cCode)
        {
            courseCode.set(cCode);
        }

        public SimpleStringProperty getCourseCodeProperty()
        {
            return courseCode;
        }
        
        public String getDescrip()
        {
            return descrip.get();
        }

        public void setDescrip(String cDescrip)
        {
            descrip.set(cDescrip);
        }
        
        public SimpleStringProperty getDescripProperty()
        {
            return descrip;
        }
    }
}
