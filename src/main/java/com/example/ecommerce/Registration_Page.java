package com.example.ecommerce;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class Registration_Page {
    GridPane root;
    Registration_Page(){
        root = new GridPane();
    }
    public void create(){
        Stage reg_stage = new Stage();
        Scene reg_scene = new Scene(root);
        reg_stage.setScene(reg_scene);

        root.setPadding(new Insets(15));
        root.setVgap(20);
        root.setHgap(30);
        root.setBackground(Background.fill(Color.LAVENDER));

        HBox header = new HBox();
        Text heading = new Text("New User Registration");
        heading.setStyle("-fx-font-weight : bold; -fx-font-size : 21");
        heading.setFill(Color.DARKBLUE);
        header.getChildren().add(heading);

        Text uname_text = new Text("Username : ");
        uname_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        Text pass_text = new Text("Password : ");
        pass_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        Text fname_text = new Text("First name : ");
        fname_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        Text lname_text = new Text("Last name : ");
        lname_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        Text email_text = new Text("Email address : ");
        email_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        Text phone_text = new Text("Phone number : ");
        phone_text.setStyle("-fx-font-weight : bold; -fx-font-size : 14");

        TextField uname_field = new TextField();
        PasswordField pass_field = new PasswordField();
        TextField fname_field = new TextField();
        TextField lname_field = new TextField();
        TextField email_field = new TextField();
        TextField phone_field = new TextField();
        //Allowing only numeric values
        phone_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldval, String newval) {
                if (!newval.matches("\\d*")) {
                    phone_field.setText(newval.replaceAll("[^\\d]", ""));
                }
            }
        });
        //Allowing maximum of 10 digits
        phone_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (phone_field.getText().length() > 10) {
                    String s = phone_field.getText().substring(0, 10);
                    phone_field.setText(s);
                }
            }
        });

        HBox footer = new HBox();
        Button reg_button  = new Button("Register");
        reg_button.setTextFill(Color.DARKBLUE);
        footer.getChildren().add(reg_button);
        footer.setAlignment(Pos.CENTER);

        root.add(header, 0, 0, 2, 1);

        root.add(uname_text, 0, 1);
        root.add(uname_field, 1, 1);

        root.add(pass_text, 0, 2);
        root.add(pass_field, 1, 2);

        root.add(fname_text, 0, 3);
        root.add(fname_field, 1, 3);

        root.add(lname_text, 0, 4);
        root.add(lname_field, 1, 4);

        root.add(email_text, 0, 5);
        root.add(email_field, 1, 5);

        root.add(phone_text, 0, 6);
        root.add(phone_field, 1, 6);

        root.add(footer, 0, 7, 2, 1);

        //Registration Logic
        reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBconnector conn = new DBconnector();

                if(pass_field.getText().length() < 8){
                    showAlert("Password must be of Minimum 8 characters.");
                    return;
                }
                if(phone_field.getText().length() < 10){
                    showAlert("Enter valid Phone number.");
                    return;
                }
                if(uname_field.getText().length() == 0){
                    showAlert("Enter valid Username.");
                    return;
                }
                if(fname_field.getText().length() == 0){
                    showAlert("Enter valid First name.");
                    return;
                }
                if(lname_field.getText().length() == 0){
                    showAlert("Enter valid Last name.");
                    return;
                }
                if(!email_field.getText().contains(".com") || !email_field.getText().contains("@")){
                    showAlert("Enter valid Email address.");
                    return;
                }

                //checking for username uniqueness
                ResultSet res = conn.getQueryTable("select * from users where u_name = '"+uname_field.getText()+"'");
                try{
                    if(res.next()){
                        System.out.println("taken");
                    } else{
                        if(uname_field.getText().length() == 0){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("Enter Valid Username !!");
                            alert.showAndWait();
                        } else{
                            int update = conn.execute("insert into users(u_name, pass, f_name, l_name, email, phone) values('"+uname_field.getText()+"', '"+pass_field.getText()+"', '"+fname_field.getText()+"', '"+lname_field.getText()+"', '"+email_field.getText()+"', '"+phone_field.getText()+"')");
                            if(update != -1){
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText(null);
                                alert.setContentText("Registration Successful !!");
                                alert.showAndWait();
                                reg_stage.close();
                            }
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        reg_stage.show();
    }

    public void showAlert(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}