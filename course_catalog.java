import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class course_catalog extends Application{
    private TableView<Course> table = new TableView<Course>();
    private final ObservableList<Person> data = FXCollections.observableArrayList(
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
        stage.setTitle("College Course Catalog");
        stage.setWidth(500);
        stage.setHeight(500);
        final Label label = new Label("University of North Carolina - Chapel Hill Courses");
        table.setEditable(true);
        TableColumn courseName = new TableColumn("Course Name");
        TableColumn courseCode = new TableColumn("Course Code");
        TableColumn descrip = new TableColumn("Description");
        table.getColumns().addAll(courseCode, courseName, descrip);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
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
                    flPerson.setPredicate(p -> p.getFirstName().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by Course Name
                    break;
                case "Course Code":
                    flPerson.setPredicate(p -> p.getLastName().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by Course Code
                    break;
                case "Description":
                    flPerson.setPredicate(p -> p.getEmail().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by email
                    break;
            }
        });


        Scene scene = new Scene(gridPane, 500, 500);
        stage.setScene(scene);
        stage.show();

        stage.show();
    }
}
