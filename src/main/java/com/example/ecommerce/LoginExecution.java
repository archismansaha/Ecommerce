package com.example.ecommerce;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.ResultSet;

public class LoginExecution {
    Home_page homePage;
    Login_page loginPage;
    Main_window mw_Instance;
    Label alert_label = new Label("Incorrect Username or Password !!");

    LoginExecution(Main_window mw_Instance){
        this.mw_Instance = mw_Instance;
        homePage = mw_Instance.homePageInstance;
        loginPage = mw_Instance.loginPageInstance;
    }

    public void login(String username, String password){
        DBconnector conn = new DBconnector();
        ResultSet res = conn.getQueryTable("select * from users where u_name='"+username+"' and pass='"+password+"'");
        try{
            if(res.next()){
                User cur_user = new User(res.getInt("u_id"), res.getString("f_name"), res.getString("l_name"), res.getString("email"), res.getString("phone"));
                System.out.println("Welcome "+cur_user.getFirst_name()+" "+cur_user.getLast_name());
                homePage.header.getChildren().remove(2);
                mw_Instance.homePageInstance.search_field.setPrefWidth(440);
                Label wlcm = new Label("Welcome,\n"+res.getString("f_name")+" !");
                wlcm.setStyle("-fx-font-weight : bold");
                homePage.header.getChildren().addAll(wlcm, homePage.cart_button);
                User logged_user = new User(res.getInt("u_id"), res.getString("f_name"), res.getString("l_name"), res.getString("email"), res.getString("phone"));
                mw_Instance.logged_user = logged_user;
                mw_Instance.isLoggedIn = true;
                homePage.footer.getChildren().add(homePage.logout_btn);

                mw_Instance.setHome_Page();
                return;
            } else{
                alert_label.setTextFill(Color.RED);
                System.out.println("Username or  incorrect");
                loginPage.root.add(alert_label, 1, 7);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
}