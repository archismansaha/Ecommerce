package com.example.ecommerce;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Main_window {
    BorderPane root = new BorderPane();
    Home_page homePageInstance = new Home_page();
    GridPane homePage;
    Login_page loginPageInstance = new Login_page();
    GridPane loginPage;
    boolean isLoggedIn = false;
    User logged_user;
    Main_window mw_instance;

    public Main_window() {
    }

    public void setMw_instance(Main_window mw_instance) {
        this.mw_instance = mw_instance;
    }

    public BorderPane create() {
        this.homePage = this.homePageInstance.create(this.mw_instance);
        this.loginPage = this.loginPageInstance.create(this.mw_instance);
        this.root.setCenter(this.homePage);
        return this.root;
    }

    public void setLogin_Page() {
        this.root.getChildren().clear();
        this.root.setCenter(this.loginPage);
    }

    public void setHome_Page() {
        this.root.getChildren().clear();
        this.root.setCenter(this.homePage);
    }
}
