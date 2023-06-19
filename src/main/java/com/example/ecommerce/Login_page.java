package com.example.ecommerce;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.sql.ResultSet;

public class Login_page {
    GridPane root;
    TextField user_field;
    TextField pass_field;
    Main_window mw_Instance;

    public GridPane create(Main_window mw_Instance){
        this.mw_Instance = mw_Instance;
        root = new GridPane();

        //Creating Username and password inputs;
        Text user_text = new Text("Username ");
        user_text.setStyle("-fx-font-weight : bold; -fx-font-stroke : grey;");
        Text pass_text = new Text("Password ");
        pass_text.setStyle("-fx-font-weight : bold; -fx-font-stroke : grey;");
        user_field = new TextField("shubhamboke");
        pass_field = new PasswordField();
        pass_field.setText("Shubham@007");
        user_field.setMaxWidth(150);
        pass_field.setMaxWidth(150);
        user_field.setPromptText("Enter Username");
        pass_field.setPromptText("Enter Password");
        root.add(user_text, 0, 2);
        root.add(user_field, 1, 2);
        root.add(pass_text, 0, 3);
        root.add(pass_field, 1, 3);
        root.setVgap(5);
        root.setHgap(5);

        //Creating Login button
        Button login_button = new Button("Login");
        root.add(login_button, 1, 4);
        login_button.setOnAction(e ->login(user_field.getText(), pass_field.getText()));

        //Creating Password reset link and new user link
        HBox reset_box = new HBox();
        Hyperlink reset_link = new Hyperlink("Reset.");
        Text reset_text = new Text("Forgot Password?");
        reset_box.getChildren().addAll(reset_text, reset_link);
        reset_box.setAlignment(Pos.CENTER_LEFT);
        root.add(reset_box, 1, 5);

        HBox register_box = new HBox();
        Text register_text = new Text("New to E-Shop?");
        Hyperlink register_link = new Hyperlink("Register here.");
        register_box.getChildren().addAll(register_text, register_link);
        register_box.setAlignment(Pos.CENTER_LEFT);
        root.add(register_box, 1, 6);

        //Adding Logo Image
        Image img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\logo.png");
        ImageView logo = new ImageView();
        logo.setImage(img);
        logo.setFitHeight(180);
        logo.setFitWidth(180);
        HBox logo_container = new HBox();
        logo_container.setAlignment(Pos.TOP_CENTER);
        logo_container.getChildren().add(logo);
        root.add(logo_container, 0, 0, 2, 1);
        root.add(new Text(""), 0, 1);
        root.setBackground(Background.fill(Color.GREENYELLOW));
        root.setAlignment(Pos.CENTER);

        //Registration page
        register_link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Registration_Page rp = new Registration_Page();
                rp.create();
            }
        });

        return root;
    }

    public void login(String username, String password){
        LoginExecution log = new LoginExecution(mw_Instance);
        log.login(username, password);
    }
}