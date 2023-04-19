package game.snakegame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class GameDriver extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(GameDriver.class.getResource("game-view.fxml"));

        Parent root = loader.load();
        GameController gameController = loader.getController();

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event ->{
            switch (event.getCode()){
                case UP -> gameController.keyUp();
                case DOWN -> gameController.keyDown();
                case LEFT -> gameController.keyLeft();
                case RIGHT -> gameController.keyRight();
            }
        });
        stage.setTitle("Snake Game!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}