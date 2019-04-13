package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static Controller ControllerHandle;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        //Zainicjowanie GUI
        //Cała logika programu jest wykonywana w klasie Controller
        ControllerHandle = (Controller)loader.getController();
        ControllerHandle.setStage(primaryStage);
        ControllerHandle.setGrid();
        ControllerHandle.setMouseClickedListener();
        ControllerHandle.setMouseDraggedListener();
        ControllerHandle.setTrainingArrays();

        primaryStage.setTitle("NAI");
        primaryStage.setScene(new Scene(root, 300, 600));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
