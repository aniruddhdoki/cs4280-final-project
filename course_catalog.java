import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class course_catalog extends Application{
    private Stage primary;
    
    public course_catalog(){

    }

    public void start(Stage primary){
        this.primary = primary;
        primary.setTitle("College Course Catalog");


        primary.show();
    }
}
