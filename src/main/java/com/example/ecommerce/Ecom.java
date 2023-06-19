package com.example.ecommerce;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Ecom extends Application {
    public Ecom() {
    }

    public BorderPane main_panel() {
        BorderPane root = new BorderPane();
        root.setPrefSize(850.0, 550.0);
        Main_window main_page = new Main_window();
        main_page.setMw_instance(main_page);
        root.setCenter(main_page.create());
        return root;
    }

    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(this.main_panel());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}
