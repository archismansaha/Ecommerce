package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class Cart_Page {
    Main_window mw_Instance;
    Cart_Page(Main_window mw_Instance){
        this.mw_Instance = mw_Instance;
    }

    public void create(){
        Stage cart_stage = new Stage();
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setVgap(15);
        root.setPrefWidth(400);
        root.setBackground(Background.fill(Color.ALICEBLUE));

        Text cart_name = new Text("Your Cart");
        cart_name.setFont(Font.font("verdana", FontWeight.BOLD, 20));
        cart_name.setFill(Color.GREEN);

        Button ord_button = new Button("View Orders");
        HBox cart_header = new HBox();
        HBox cart_text = new HBox();
        cart_text.setAlignment(Pos.CENTER_LEFT);
        cart_text.setPrefWidth(200);
        cart_text.getChildren().add(cart_name);

        HBox ord_container = new HBox();
        ord_container.setAlignment(Pos.CENTER_RIGHT);
        ord_container.getChildren().add(ord_button);
        ord_container.setPrefWidth(200);

        cart_header.getChildren().addAll(cart_text, ord_container);

        root.add(cart_header, 0, 0);

        //Making item table
        TableView<Cart_Item> pro_table = new TableView<>();
        VBox table_box = new VBox();

        //columns
        TableColumn id = new TableColumn("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn name = new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn quantity = new TableColumn("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        pro_table.getColumns().addAll(id, name, price, quantity);

        //Adding data
        pro_table.setItems(fetchProducts());
        pro_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table_box.getChildren().add(pro_table);
        root.add(table_box, 0, 1);

        Scene cart_scene = new Scene(root);
        cart_stage.setScene(cart_scene);
        cart_stage.show();

        //Adding remove item button and checkout button
        HBox footer = new HBox();
        footer.setPrefWidth(400);
        HBox btn_container = new HBox();
        HBox total_container = new HBox();
        btn_container.setAlignment(Pos.CENTER_LEFT);
        btn_container.setSpacing(10);
        total_container.setAlignment(Pos.CENTER_RIGHT);
        btn_container.setPrefWidth(200);
        total_container.setPrefWidth(200);
        footer.getChildren().addAll(btn_container, total_container);
        Text total = new Text("Total : ");
        total.setStyle("-fx-font-weight : bold; -fx-font-size : 14");
        ObservableList<Cart_Item> list = fetchProducts();
        int sum = 0;
        for(Cart_Item itm: list){
            sum += itm.getPrice() * itm.getQuantity();
        }
        total.setText("Total : "+sum+" Rs.");

        Button rem_button = new Button("Remove Item");
        Button chk_button = new Button("Checkout");
        btn_container.getChildren().addAll(rem_button, chk_button);
        total_container.getChildren().add(total);
        root.add(footer, 0, 2);

        //Removing selected item from cart
        rem_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Cart_Item item = pro_table.getSelectionModel().getSelectedItem();
                if(item == null){
                    Alert alrt = new Alert(Alert.AlertType.INFORMATION);
                    alrt.setHeaderText(null);
                    alrt.setContentText("Please select an Item to Remove !!");
                    alrt.showAndWait();
                } else{
                    DBconnector conn = new DBconnector();
                    conn.execute("delete from cart where u_id="+mw_Instance.logged_user.getUser_id()+" and p_id="+item.getId()+" and quantity="+item.getQuantity());
                    pro_table.setItems(fetchProducts());
                    ObservableList<Cart_Item> list = fetchProducts();
                    int sum = 0;
                    for(Cart_Item itm: list){
                        sum += itm.getPrice() * itm.getQuantity();
                    }
                    total.setText("Total : "+sum);
                }
            }
        });

        //Checkout button to buy;
        chk_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<Cart_Item> items = fetchProducts();
                for(Cart_Item item: items){
                    Product product = new Product(item.getId(), item.getName(), item.getPrice(), 0);
                    OrderExecution order = new OrderExecution(mw_Instance.logged_user.getUser_id(), item.getQuantity(), product);
                    order.execute();
                    //clearing cart
                    DBconnector conn = new DBconnector();
                    conn.execute("delete from cart where u_id="+mw_Instance.logged_user.getUser_id());
                    pro_table.setItems(fetchProducts());
                }
                Alert alrt = new Alert(Alert.AlertType.INFORMATION);
                alrt.setHeaderText(null);
                alrt.setContentText("Order Successful !!");
                alrt.showAndWait();
            }
        });

        //Making Orders page

        GridPane order_pane = new GridPane();
        order_pane.setPadding(new Insets(10));
        order_pane.setVgap(15);
        order_pane.setBackground(Background.fill(Color.ALICEBLUE));
        order_pane.setPrefWidth(620);

        HBox name_box = new HBox();
        name_box.setAlignment(Pos.CENTER_LEFT);
        name_box.setPrefWidth(300);

        HBox btn_box = new HBox();
        btn_box.setAlignment(Pos.CENTER_RIGHT);
        btn_box.setPrefWidth(300);

        Text order_text = new Text("Your Orders");
        order_text.setFont(Font.font("verdana", FontWeight.BOLD, 20));
        order_text.setFill(Color.GREEN);

        Button back_btn = new Button("Back");
        name_box.getChildren().add(order_text);
        btn_box.getChildren().add(back_btn);
        HBox order_header = new HBox();
        order_header.getChildren().addAll(name_box, btn_box);
        order_pane.add(order_header, 0, 0);

        //Adding table;

        TableView<Order_Item> ord_table = new TableView<>();
        VBox order_box = new VBox();

        //columns
        TableColumn o_id = new TableColumn("ID");
        o_id.setCellValueFactory(new PropertyValueFactory<>("o_id"));

        TableColumn p_name = new TableColumn("Product");
        p_name.setCellValueFactory(new PropertyValueFactory<>("p_name"));

        TableColumn ord_quantity = new TableColumn("Quantity");
        ord_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn date = new TableColumn("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn status = new TableColumn("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn bill = new TableColumn("Bill Amount");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));

        ord_table.getColumns().addAll(o_id, p_name, ord_quantity, date, status, bill);
        ord_table.setItems(fetchOrders());
        ord_table.setPrefWidth(600);
        order_pane.add(ord_table, 0, 1);

        Scene order_scene = new Scene(order_pane);

        //View Order button
        ord_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cart_stage.setScene(order_scene);
            }
        });

        //back button
        back_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cart_stage.setScene(cart_scene);
            }
        });

    }

    public ObservableList<Cart_Item> fetchProducts(){
        DBconnector conn = new DBconnector();
        ObservableList<Cart_Item> data = FXCollections.observableArrayList();
        ResultSet res = conn.getQueryTable("select products.p_id, products.p_name, products.price, cart.quantity from products, cart where cart.p_id=products.p_id and u_id="+mw_Instance.logged_user.getUser_id());
        try{
            while(res.next()){
                data.add(new Cart_Item(res.getInt("p_id"), res.getString("p_name"), res.getInt("price"), res.getInt("quantity")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public ObservableList<Order_Item> fetchOrders(){
        DBconnector conn = new DBconnector();
        ObservableList<Order_Item> data = FXCollections.observableArrayList();
        ResultSet res = conn.getQueryTable("select o_id, p_name, quantity, date, status, bill_amt from orders, products where orders.p_id = products.p_id and u_id ="+mw_Instance.logged_user.getUser_id());
        try{
            while(res.next()){
                data.add(new Order_Item(res.getInt("o_id"), res.getString("p_name"), res.getInt("quantity"), res.getString("date"), res.getString("status"), res.getInt("bill_amt")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}