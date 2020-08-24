package qcontrol;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QControl extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("QControl.fxml"));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("QControl.fxml"));
        Parent root=loader.load();
        QCController c=(QCController)loader.getController();
        c.stage=primaryStage;
        c.setTitle(null);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
