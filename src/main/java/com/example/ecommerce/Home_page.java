package com.example.ecommerce;



import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.Optional;

public class Home_page {
    Main_window mw_Instance;
    HBox header;
    HBox footer;
    Button cart_button;
    TextField search_field;
    HBox logout_btn;
    public GridPane create(Main_window mw_Instance){
        this.mw_Instance = mw_Instance;
        GridPane root = new GridPane();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.setVgap(10);
        root.setBackground(Background.fill(Color.LIGHTSEAGREEN));

        header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(10);

        //Adding logo
        Image logo_img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\e_shop.png");
        ImageView logo_view = new ImageView();
        logo_view.setImage(logo_img);
        logo_view.setFitWidth(170);
        logo_view.setFitHeight(50);
        header.getChildren().add(logo_view);

        //Making Search bar
        HBox search_box = new HBox();
        search_box.setPadding(new Insets(25));
        search_field = new TextField();
        search_field.setPromptText("Type here to Search");
        search_field.setPrefHeight(27);
        search_field.setPrefWidth(500);
        Button search_button = new Button();
        Image img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\search_btn.png");
        ImageView view = new ImageView();
        view.setImage(img);
        view.setFitHeight(20);
        view.setFitWidth(20);
        search_button.setGraphic(view);
        search_box.getChildren().addAll(search_field, search_button);
        header.getChildren().addAll(search_box);

        //Adding Login Button
        Button login_button = new Button("Login");
        login_button.setOnAction(e -> login());
        header.getChildren().add(login_button);

        //Adding Cart button
        cart_button = new Button();
        Image cart_img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\cart.png");
        ImageView cart_view = new ImageView();
        cart_view.setImage(cart_img);
        cart_view.setFitWidth(25);
        cart_view.setFitHeight(25);
        cart_button.setGraphic(cart_view);


        root.getChildren().add(header);

        //Making Product table
        TableView<Product> pro_table = new TableView<>();
        VBox table_box = new VBox();

        //columns
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn rating = new TableColumn("Rating");
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        pro_table.getColumns().addAll(id, name, price, rating);

        //Adding data
        ObservableList<Product> data = fetchProducts();
        pro_table.setItems(data);
        pro_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table_box.getChildren().add(pro_table);
        root.add(table_box, 0, 1);

        //Making buy and addtocart button in footer
        footer = new HBox(0);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setSpacing(10);
        Button buy_button = new Button("Buy now");
        Button addtocart = new Button("Add to Cart");

        logout_btn = new HBox();
        logout_btn.setAlignment(Pos.CENTER_RIGHT);
        logout_btn.setPrefWidth(665);
        Button logout_button = new Button("Log Out");
        logout_btn.getChildren().add(logout_button);

        footer.getChildren().addAll(buy_button, addtocart);
        root.add(footer, 0, 2);

        //Buy button Logic
        buy_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!mw_Instance.isLoggedIn){
                    showAlert("Please Login First !");
                } else if(pro_table.getSelectionModel().getSelectedItem() == null){
                    showAlert("Please Select a Product !");
                } else{
                    Stage buy_stage = new Stage();
                    GridPane buy_page = new GridPane();
                    buy_page.setPadding(new Insets(20));
                    buy_page.setHgap(10);
                    buy_page.setVgap(10);

                    //fetching selected product
                    Product product = pro_table.getSelectionModel().getSelectedItem();

                    //Product info
                    Text name = new Text(product.getName());
                    name.setFont(Font.font("verdana", FontWeight.BOLD, 20));
                    Text rating = new Text("Rating : " + Double.toString(product.getRating()));
                    Text price = new Text("Price : " + Integer.toString(product.getPrice()) + " Rs.");
                    HBox qunt_box = new HBox();
                    qunt_box.setSpacing(10);
                    Text quantity = new Text("Quantity : ");
                    Spinner<Integer> qunt = new Spinner<>(1, 10, 1);
                    qunt.setPrefWidth(55);

                    qunt_box.getChildren().addAll(quantity, qunt);
                    Image pro_img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\pro_img.png");
                    ImageView pro_view = new ImageView();
                    pro_view.setImage(pro_img);
                    pro_view.setFitHeight(150);
                    pro_view.setFitWidth(200);

                    //Order Button
                    Button order_button = new Button("Place Order");

                    buy_page.add(name, 0, 0);
                    buy_page.add(rating, 0, 1);
                    buy_page.add(price, 0, 2);
                    buy_page.add(qunt_box, 0, 3);
                    buy_page.add(pro_view, 1, 0, 1, 4);

                    //order box
                    HBox ord_box = new HBox();
                    ord_box.setAlignment(Pos.CENTER);
                    ord_box.getChildren().add(order_button);
                    Text total = new Text("Sub-Total : " + product.getPrice() + "Rs.");
                    total.setFont(Font.font("verdana", FontWeight.BOLD, 12));
                    buy_page.add(total, 0, 4);
                    buy_page.add(ord_box, 1, 4);
                    qunt.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            total.setText("Sub-Total : " + product.getPrice() * qunt.getValue() + "Rs.");
                        }
                    });

                    //Executing order
                    order_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            OrderExecution order = new OrderExecution(mw_Instance.logged_user.getUser_id(), qunt.getValue(), product);
                            if(order.execute())
                                showAlert("Order Successful !!");
                            else
                                showAlert("Order Failed !!");
                            buy_stage.close();
                        }
                    });

                    Scene buy_scene = new Scene(buy_page);
                    buy_stage.setScene(buy_scene);
                    buy_stage.show();
                }
            }
        });

        //Add to cart button
        addtocart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!mw_Instance.isLoggedIn){
                    showAlert("Please Login First !");
                } else if(pro_table.getSelectionModel().getSelectedItem() == null){
                    showAlert("Please Select a Product !");
                } else{
                    Stage addcart_stage = new Stage();
                    GridPane addcart_page = new GridPane();
                    addcart_page.setPadding(new Insets(20));
                    addcart_page.setHgap(10);
                    addcart_page.setVgap(10);

                    //fetching selected product
                    Product product = pro_table.getSelectionModel().getSelectedItem();

                    //Product info
                    Text name = new Text(product.getName());
                    name.setFont(Font.font("verdana", FontWeight.BOLD, 20));
                    Text rating = new Text("Rating : " + Double.toString(product.getRating()));
                    Text price = new Text("Price : " + Integer.toString(product.getPrice()) + " Rs.");
                    HBox qunt_box = new HBox();
                    qunt_box.setSpacing(10);
                    Text quantity = new Text("Quantity : ");
                    Spinner<Integer> qunt = new Spinner<>(1, 10, 1);
                    qunt.setPrefWidth(55);

                    qunt_box.getChildren().addAll(quantity, qunt);
                    Image pro_img = new Image("C:\\Users\\Archisman\\Desktop\\Ecommerce\\src\\main\\java\\com\\example\\ecommerce\\pro_img.png");
                    ImageView pro_view = new ImageView();
                    pro_view.setImage(pro_img);
                    pro_view.setFitHeight(150);
                    pro_view.setFitWidth(200);

                    //Order Button
                    Button addcart_button = new Button("Add to Cart");

                    addcart_page.add(name, 0, 0);
                    addcart_page.add(rating, 0, 1);
                    addcart_page.add(price, 0, 2);
                    addcart_page.add(qunt_box, 0, 3);
                    addcart_page.add(pro_view, 1, 0, 1, 4);

                    //order box
                    HBox ord_box = new HBox();
                    ord_box.setAlignment(Pos.CENTER);
                    ord_box.getChildren().add(addcart_button);
                    Text total = new Text("Sub-Total : " + product.getPrice() + "Rs.");
                    total.setFont(Font.font("verdana", FontWeight.BOLD, 12));
                    addcart_page.add(total, 0, 4);
                    addcart_page.add(ord_box, 1, 4);
                    qunt.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            total.setText("Sub-Total : " + product.getPrice() * qunt.getValue() + "Rs.");
                        }
                    });

                    //Executing order
                    addcart_button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            OrderExecution order = new OrderExecution(mw_Instance.logged_user.getUser_id(), qunt.getValue(), product);
                            if(order.addToCart())
                                showAlert("Items Added to Cart !!");
                            else
                                showAlert("Unable to Add !!");
                            addcart_stage.close();
                        }
                    });

                    Scene buy_scene = new Scene(addcart_page);
                    addcart_stage.setScene(buy_scene);
                    addcart_stage.show();
                }
            }
        });

        //Cart page button
        cart_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Cart_Page cartPage = new Cart_Page(mw_Instance);
                cartPage.create();
            }
        });

        //logout button
        logout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setHeaderText(null);
                conf.setContentText("Are you sure you want to Logout?");
                Optional<ButtonType> choice = conf.showAndWait();
                if(choice.get() == ButtonType.OK){
                    mw_Instance.isLoggedIn = false;
                    mw_Instance.logged_user = null;
                    mw_Instance.homePageInstance = new Home_page();
                    mw_Instance.homePage = mw_Instance.homePageInstance.create(mw_Instance);
                    mw_Instance.setHome_Page();
                }
            }
        });

        //Search functionality
        search_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pro_table.setItems(searchProducts());
            }
        });

        return root;
    }

    public ObservableList<Product> fetchProducts(){
        DBconnector conn = new DBconnector();
        ObservableList<Product> data = FXCollections.observableArrayList();
        ResultSet res = conn.getQueryTable("select p_id, p_name, price, p_rating from products");
        try{
            while(res.next()){
                data.add(new Product(res.getInt("p_id"), res.getString("p_name"), res.getInt("price"), res.getDouble("p_rating")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<Product> searchProducts(){
        DBconnector conn = new DBconnector();
        ObservableList<Product> data = FXCollections.observableArrayList();
        ResultSet res = conn.getQueryTable("select p_id, p_name, price, p_rating from products where p_name like '%"+search_field.getText()+"%'");
        try{
            while(res.next()){
                data.add(new Product(res.getInt("p_id"), res.getString("p_name"), res.getInt("price"), res.getDouble("p_rating")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public void showAlert(String s){
        Alert alrt = new Alert(Alert.AlertType.INFORMATION);
        alrt.setContentText(s);
        alrt.setHeaderText(null);
        alrt.showAndWait();
    }

    public void login(){
        mw_Instance.setLogin_Page();
    }
}