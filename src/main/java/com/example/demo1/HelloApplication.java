package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

/* Author: RidoineEL (https://github.com/ridoineel)
*
* */

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        String css = getClass().getResource("style.css").toExternalForm();

        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(css);

        stage.setTitle("Solina Browser");

        stage.getIcons().add(new Image(getClass().getResource("assets/icons/solina.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}